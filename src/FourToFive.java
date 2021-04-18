import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FourToFive {
    private static String[][] Formulas;
    public static void ThreeToFour() {
        try (
        var result = new PrintWriter(new OutputStreamWriter(new FileOutputStream("data/5一级MP定理.txt"),
                StandardCharsets.UTF_8),true);//autoFlush很重要！
             var in = new Scanner(new File("data/4一级代入公理.txt"));
        ) {
//            var result = new PrintWriter("data/5一级MP定理.txt");
            Formulas = ThreeToFour.scanSub(in);
            for (var axiomsInOneFormula:Formulas){
                for (int i = 0; i < axiomsInOneFormula.length; i++) {
                    for (int j = i+1; j < axiomsInOneFormula.length; j++) {
//                        if (i==j)continue;
                        String theorem = Deduce(axiomsInOneFormula[i],axiomsInOneFormula[j]);
                        if (theorem!=null) {
                            result.println(theorem);
//                            System.err.println("我推出来啦！");
                            System.err.println("我推出了："+theorem);
                        }
                        theorem = Deduce(axiomsInOneFormula[j],axiomsInOneFormula[i]);
                        if (theorem!=null){
                            result.println(theorem);
//                            System.err.println("我推出来啦！");
                            System.err.println("我推出了："+theorem);
                        }
                    }
                }
                result.println("---");
//                result.close();
                System.out.println("已完成一道题的定理");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String Deduce(String A, String A_implies_B) {
        String[] subs = TwoToThree.sub(TwoToThree.预处理输入(A_implies_B));
        if (IsSameFormula(A,subs[0]))
            return subs[1];
        return null;
    }

    private static boolean IsSameFormula(String A, String B) {
        return TwoToThree.预处理输入2(A).equals(TwoToThree.预处理输入2(B))||
                TwoToThree.预处理输入(A).equals(TwoToThree.预处理输入2(B))||
                TwoToThree.预处理输入2(A).equals(TwoToThree.预处理输入(B))||
                TwoToThree.预处理输入(A).equals(TwoToThree.预处理输入(B))
                ;
    }

    public static void main(String[] args) {
        ThreeToFour();
    }
}
