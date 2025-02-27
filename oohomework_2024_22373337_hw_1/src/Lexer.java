import java.util.ArrayList;

public class Lexer {
    private final String input;
    private int iter;
    private int pos;
    private final ArrayList<String> tokenList = new ArrayList<>();
    private final ArrayList<Type> typeList = new ArrayList<>();

    public enum Type {
        NUMBER, X, ADD, SUB, MULTI, EXP, L, R, END
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
                tokenList.add(getNumber());
                typeList.add(Type.NUMBER);
            }
            else if (ch == '+') {
                int size = typeList.size();
                if (size > 0) {
                    Type topType = typeList.get(size - 1);
                    if (topType.equals(Type.MULTI) || topType.equals(Type.EXP)) {
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
                else {
                    iter++;
                    tokenList.add("+");
                    typeList.add(Type.ADD);
                }
            }
            else if (ch == '-') {
                int size = typeList.size();
                if (size > 0 && typeList.get(size - 1).equals(Type.MULTI)) {
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
            else if (ch == '(' || ch == ')' || ch == '^' || ch == '*' || ch == 'x') {
                tackleEasy(ch);
            }
        }
        typeList.add(Type.END);
    }
    
    private void tackleEasy(char ch) {
        if (ch == '(') {
            iter++;
            tokenList.add("(");
            typeList.add(Type.L);
        }
        else if (ch == ')') {
            iter++;
            tokenList.add(")");
            typeList.add(Type.R);
        }
        else if (ch == '*') {
            iter++;
            tokenList.add("*");
            typeList.add(Type.MULTI);
        }
        else if (ch == '^') {
            iter++;
            tokenList.add("^");
            typeList.add(Type.EXP);
        }
        else if (ch == 'x') {
            iter++;
            tokenList.add("x");
            typeList.add(Type.X);
        }
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
