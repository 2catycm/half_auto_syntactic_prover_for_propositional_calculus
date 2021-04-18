import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class TwoToThree {
    private static HashSet<String> subformulas = new HashSet<>();
    public static void TwoToThree() {
        try (var result = new PrintWriter("data/3子公式.txt");
             var in = new Scanner(new File("data/2单符号语言公式.txt"));
        ) {
            while(in.hasNextLine()){
                var nextLine = in.nextLine();
                if (nextLine.replaceAll("\\s+", "").equals(""))
                    continue;//不处理空白行

                var formula = 预处理输入(nextLine);
                boolean isTautology = true;
                String[] variables = scan(formula);HeadVariable = variables[0].charAt(0);
                boolean[][] matrix = combination(variables);
                for (boolean[] va:matrix) {
                    if (solve(formula,va)) continue;
                    isTautology = false;
                    System.out.println("算出假！在真值指派："+ Arrays.toString(va)+", ");
                }
                if (!isTautology){
                    System.err.println("在以上真值指派中，所给公式"+formula+"为假。故公式不是重言式！");
                    throw new InputMismatchException();
                }
                for (var subformula:subformulas)
                    result.println(subformula);
                result.println("---");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static char HeadVariable = 'A'; //因为对相关技术不熟悉，我不知道怎么动态生成变量，以及如何弄不是单字符的变量，只好默认从A开始。
    // 不巧第七次作业后面几题要求从B开始
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String formula = 预处理输入(in.nextLine());
        String[] variables = scan(formula); HeadVariable = variables[0].charAt(0);//因为用了tree map,所以排好啦！
//        System.out.println(HeadVariable);//debug
        boolean[][] matrix = combination(variables);
        for (boolean[] va:matrix) {
            System.out.println(solve(formula,va));
        }
        System.out.println(subformulas);
    }
    private static String 预处理输入(String formula) {
        formula = "("+formula+")";//假设输入没有多余括号，最外层是省略的
//        //避免引入多余括号
//        if(formula.startsWith("((")&&formula.endsWith("))"))
//            return 预处理输入(formula.substring(0,formula.length()-2).substring(2));
        return formula.replaceAll(" ","");
    }
    private static String 预处理输入2(String formula) {
        formula = "("+formula+")";
        //避免引入多余括号
        if(formula.startsWith("((")&&formula.endsWith("))"))
            return 预处理输入(formula.substring(0,formula.length()-2).substring(2));
        return formula.replaceAll(" ","");
    }

    private static boolean[][] combination(String[] variables) {
        int combinationCnt = pow(2, variables.length),variablesCnt = variables.length;
        boolean[][] result = new boolean[combinationCnt][variablesCnt];
        for (int j = 0; j < variablesCnt; j++) {
            int 交替TF的周期 = pow(2,variablesCnt-j-1);
            for (int i = 0; i < combinationCnt; i++)
                result[i][j] = (i / 交替TF的周期) % 2 == 0;
        }
        return result;
    }
//    private static boolean search

    private static int pow(int a, int b){
        int c=1;
        for (int i = 0; i < b; i++) {
            c*=a;
        }
        return c;
    }

    static String[] scan(String formula) {
        Map<String, Integer> map = new TreeMap<>();//按照String来升序排序
        for (int i = 0; i <formula.length() ; i++) {
            String a = ""+formula.charAt(i);
            if (map.containsKey(a)|| (!Character.isAlphabetic(a.charAt(0))) )
                continue;
            map.put(a,1);
        }
        return map.keySet().toArray(new String[0]);
    }

    private static boolean solve(String formula, boolean[] va) {
        if (formula==null) return false;//为了处理一元运算符
        if (formula.length()==3&&Character.isAlphabetic(formula.charAt(1))){
            subformulas.add(formula.substring(1,2));
            return va[formula.charAt(1)-HeadVariable];}//看看我是第几个变量，然后找到属于我的真值指派。
        else {
            String[] subs = sub(formula);
            Operation operation = OperationFactory.create(subs[2]);

            subformulas.add(operation.toString(subs));
            return operation.operate(solve(subs[0], va),solve(subs[1], va));
        }
    }
    private static String[] sub(String formula) {
        for(String operator:OperationFactory.alphabet){

            int ParenthesisFlux = 0;
            for (int index = 0; index < formula.length(); index++) {
                ParenthesisFlux+=formula.charAt(index)=='('?1: (formula.charAt(index)==')'?-1:0);
                if (formula.charAt(index)!=operator.charAt(0)) continue;
                if (ParenthesisFlux!=1)continue;
                //有且只有一个这样的位置
                if (OperationFactory.setOfUnaryOperators.contains(operator))
                    return new String[]{null,预处理输入2(formula.substring(index+1,formula.length()-1)),""+formula.charAt(index)};
                return new String[]{预处理输入2(formula.substring(1,index)),预处理输入2(formula.substring(index+1,formula.length()-1)),""+formula.charAt(index)};
            }
        }
        throw new InputMismatchException("找不到运算符！");
    }

}
