import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ThreeToFour {
    private static String[] axiomSchemata;
    private static String[][] subFormulas;
    private static String[] axiomVariables;
    public static void ThreeToFour() {
        try (var result = new PrintWriter("data/4一级代入公理.txt");
             var in1 = new Scanner(new File("data/3子公式.txt"));
             var in2 = new Scanner(new File("data/0公理系统.txt"));
        ) {
            subFormulas = scanSub(in1);
            axiomSchemata = scanAxiom(in2);
            for (var subsFromOneFormula:subFormulas){
                for (var OneAxiomSchemata:axiomSchemata){
                    ArrayList<String> DeducedAxioms = new ArrayList<>();
                    axiomVariables = TwoToThree.scan(OneAxiomSchemata);
                    search(DeducedAxioms,axiomVariables,subsFromOneFormula,OneAxiomSchemata);//DeducedAxioms是可变变量，不是String那种不可变变量，所以修改每一个DeducedAxiom都是修改外面的这个
                    for (var OneDeducedAxiom:DeducedAxioms)
                        result.println(OneDeducedAxiom);
                }
                result.println("---");
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void search(ArrayList<String> deducedAxioms, String[] axiomVariables, String[] subsFromOneFormula, String oneAxiomSchemata) {
        search(deducedAxioms,axiomVariables,subsFromOneFormula,oneAxiomSchemata,0);//i表示公理变量正在替换哪一个
    }

    private static void search(ArrayList<String> deducedAxioms, String[] axiomVariables, String[] subsFromOneFormula, String oneAxiomSchemata, int i) {
        if (i==axiomVariables.length) {
            deducedAxioms.add(oneAxiomSchemata);
            return;
        }
        for (var OneFormula:subsFromOneFormula){
            search(deducedAxioms,axiomVariables,subsFromOneFormula,oneAxiomSchemata.replace(axiomVariables[i],OneFormula),i+1);
            //这个就是无损前进，因为里面String变量是新的，回来是旧的
        }
    }

    private static String[] scanAxiom(Scanner axiomIn) {
        ArrayList<String> schemata = new ArrayList<>();
        while(axiomIn.hasNextLine())
            schemata.add(axiomIn.nextLine());
        return schemata.toArray(schemata.toArray(new String[0]));
    }

    private static String[][] scanSub(Scanner subIn) {//这段代码要收藏，经常扫描东西的时候不知道大小，也不知道怎么分割
        ArrayList<String[]> formulas = new ArrayList<>();
        while(subIn.hasNextLine()){
            String NextLine;
            ArrayList<String> current_subFormulas_for_a_formula = new ArrayList<>();
            while( !(NextLine = subIn.nextLine() ).equals("---") ) {//这个写得太好了，我要记住这种写法，之前想了很久
                current_subFormulas_for_a_formula.add(NextLine);
            }
            formulas.add(current_subFormulas_for_a_formula.toArray(new String[0]));
        }
        return formulas.toArray(new String[0][]);
    }

    public static void main(String[] args) {
        ThreeToFour();
        System.out.println(Arrays.deepToString(subFormulas));
    }
}
