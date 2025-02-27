import java.util.ArrayList;

public class Lexer {
    private final String input;
    private int iter;
    private int pos;
    private final ArrayList<String> tokenList = new ArrayList<>();
    private final ArrayList<Type> typeList = new ArrayList<>();

    public enum Type {
        NUMBER, X, ADD, SUB, MULTI, POWEXP, L, R, EXPFUN, SELFFUN, DOT, DX,END
    }
    
    public Lexer(String input) {
        this.input = input;
        getList();
        this.pos = 0;
    }
    
    public void next() {
        ++pos;
    }
    
    private void getList() {
        for (iter = 0; iter < input.length();) {
            char ch = input.charAt(iter);
            if (Character.isDigit(ch)) {
                meetDigit();
            }
            else if (ch == 'f' || ch == 'g' || ch == 'h') {
                meetFunc(ch);
            }
            else if (ch == 'e') {
                meetExp();
            }
            else if (ch == ',') {
                meetDot();
            }
            else if (ch == '+') {
                meetAdd();
            }
            else if (ch == '-') {
                meetSub();
            }
            else if (ch == '(') {
                meetLP();
            }
            else if (ch == ')') {
                meetRP();
            }
            else if (ch == '*') {
                meetMul();
            }
            else if (ch == '^') {
                meetPow();
            }
            else if (ch == 'x') {
                meetVar();
            }
            else if (ch == 'd') {
                meetDerivative();
            }
        }
        typeList.add(Type.END);
    }
    
    private void meetDerivative() {
        iter += 3;
        tokenList.add("dx(");
        typeList.add(Type.DX);
    }
    
    private void meetDigit() {
        tokenList.add(getNumber());
        typeList.add(Type.NUMBER);
    }
    
    private void meetFunc(char ch) {
        iter += 2;
        tokenList.add(String.valueOf(ch));
        typeList.add(Type.SELFFUN);
    }
    
    private void meetExp() {
        iter += 4;
        tokenList.add("exp");
        typeList.add(Type.EXPFUN);
    }
    
    private void meetDot() {
        iter++;
        tokenList.add(",");
        typeList.add(Type.DOT);
    }
    
    private void meetAdd() {
        int size = typeList.size();
        if (size > 0 && (typeList.get(size - 1).equals(Type.MULTI) ||
                typeList.get(size - 1).equals(Type.POWEXP) ||
                typeList.get(size - 1).equals(Type.DOT) ||
                typeList.get(size - 1).equals(Type.SELFFUN) ||
                typeList.get(size - 1).equals(Type.EXPFUN))) {
            iter++;
            tokenList.add(getNumber());
            typeList.add(Type.NUMBER);
        }
        else {
            iter++;
            tokenList.add("+");
            typeList.add(Type.ADD);
        }
    }
    
    private void meetSub() {
        int size = typeList.size();
        if (size > 0 && ((typeList.get(size - 1).equals(Type.MULTI) ||
                typeList.get(size - 1).equals(Type.DOT)) ||
                typeList.get(size - 1).equals(Type.SELFFUN) ||
                typeList.get(size - 1).equals(Type.EXPFUN))) {
            iter++;
            tokenList.add("-" + getNumber());
            typeList.add(Type.NUMBER);
        }
        else {
            iter++;
            tokenList.add("-");
            typeList.add(Type.SUB);
        }
    }
    
    private void meetLP() {
        iter++;
        tokenList.add("(");
        typeList.add(Type.L);
    }
    
    private void meetRP() {
        iter++;
        tokenList.add(")");
        typeList.add(Type.R);
    }
    
    private void meetMul() {
        iter++;
        tokenList.add("*");
        typeList.add(Type.MULTI);
    }
    
    private void meetPow() {
        iter++;
        tokenList.add("^");
        typeList.add(Type.POWEXP);
    }
    
    private void meetVar() {
        iter++;
        tokenList.add("x");
        typeList.add(Type.X);
    }
    
    public String getToken() {
        return tokenList.get(pos);
    }
    
    public Type getType() { return typeList.get(pos); }
    
    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (iter < input.length() && Character.isDigit(input.charAt(iter))) {
            sb.append(input.charAt(iter));
            ++iter;
        }
        String str = sb.toString();
        str = str.replaceFirst("^0+(?!$)","");
        return str;
    }
}
