import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OneToTwo {
    private static Map<String,String> Dictionary1;
    private static Map<String,String> Dictionary2;//暂时不知道正则表达式怎么编,只好多写几个字典
    static{
        Dictionary1 = new HashMap<>();
        String[] alphabet = {"∨","¬","∧","→","↔"};
        String[] Meanings = {"or","not","and","im","eq"};
        for (int i = 0; i < alphabet.length; i++) {
            Dictionary1.put(alphabet[i], Meanings[i]);
        }
        Dictionary2 = new HashMap<>();
        String[] alphabet2 = {"∨","¬","∧","→","↔"};
        String[] Meanings2 = {"\\or","\\lns","\\and","\\im","\\eq"};
        for (int i = 0; i < alphabet2.length; i++) {
            Dictionary2.put(alphabet2[i], Meanings2[i]);
        }
    }
    public static String meaning2Word(String args) {
        for (var s: Dictionary1.keySet()){//字典有相同的keyset
            args = args.replace(Dictionary1.get(s),s);//暂时不知道正则表达式怎么编，无法用replaceAll
            args = args.replace(Dictionary2.get(s),s);//问题，如果输入java的string，\\换不干净
        }
        return args.replace('\\',' ');
    }

    public static boolean OneToTwo() {
        try (var result = new PrintWriter("data/2单符号语言公式.txt");
             var in = new Scanner(new File("data/1待分析公式们.md"));
        ){
            while(in.hasNextLine())
                result.println(meaning2Word(in.nextLine()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(meaning2Word("A im B"));
        System.out.println(meaning2Word("A \\im B"));
    }
}
