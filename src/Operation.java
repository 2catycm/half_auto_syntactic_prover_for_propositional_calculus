import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class Operation {
    abstract boolean operate(boolean a, boolean b);

    public String toString(String[] subformula) {
        return subformula[0]+subformula[2]+subformula[1];
    }
}

class OperationFactory{
    //example: (A→B)→(B→A) ((A→B)→(B→A))→((A→B)→(B→A)) ((C→B)→(B→C))→((C→B)→(B→C))
    public static String[] alphabet = {"∨","¬","∧","→","↔"};
    public static String[] UnaryOperators = {"¬"};
    public static final Set<String> setOfUnaryOperators = new HashSet<>(Arrays.asList(UnaryOperators));
    public static Operation create(String operator) {
        switch (operator){
            case "→" -> {
                return new Operation() {
                    @Override
                    public boolean operate(boolean a, boolean b) {
//!错误警示，java的^是异或                        return a == !b;//!(a^(!b))//
                        return (!a)||(b);
                    };
                };
            }
            case "↔" -> {
                return new Operation() {
                    @Override
                    public boolean operate(boolean a, boolean b) {
                        return a==b;
                    }
                };
            }
            case "¬" -> {//a是分割公式为null，a的solve值是false
                return new Operation() {
                    @Override
                    public boolean operate(boolean a, boolean b) {
                        assert !a;//方便调试程序
                        return !b;
                    }

                    @Override
                    public String toString(String[] subformula) {
                        return subformula[2]+subformula[1];
                    }
                };
            }
            case "∨" -> {
                return new Operation() {
                    @Override
                    public boolean operate(boolean a, boolean b) {
                        return a||b;
                    }
                };
            }
            case "∧" -> {
                return new Operation() {
                    @Override
                    public boolean operate(boolean a, boolean b) {
                        return a&&b;
                    }
                };
            }
            default -> throw new IllegalStateException("Unexpected value: " + operator);
        }
    }
}