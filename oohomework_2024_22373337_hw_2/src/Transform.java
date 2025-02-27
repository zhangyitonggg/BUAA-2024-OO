import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Transform {
    private static HashMap<String, String> funcToExpr = new HashMap<>();
    private static HashMap<String, ArrayList<String>> funcToParas = new HashMap<>();
    
    public static void addFunc(Scanner scanner) {
        int n = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < n; i++) {
            String function = Optimizer.preProduce(scanner.nextLine());
            function = function.replace("exp","e");
            function = function.replace("x","a");
            function = function.replace("e","exp");
            String funcName = function.substring(0,1);
            ArrayList<String> paras = new ArrayList<>();
            for (int index = 1; function.charAt(index) != '='; index++) {
                if (function.charAt(index) == ',' || function.charAt(index) == ')') {
                    paras.add(String.valueOf(function.charAt(index - 1)));
                }
            }
            funcToParas.put(funcName, paras);
            funcToExpr.put(funcName, function.substring(function.indexOf("=") + 1));
        }
    }
    
    public static String replaceParas(String funcName, ArrayList<String> args) {
        String funExpr = funcToExpr.get(funcName);
        ArrayList<String> paras = funcToParas.get(funcName);
        for (int iter = 0; iter < paras.size(); iter++) {
            funExpr = funExpr.replace(paras.get(iter), "(" + args.get(iter) + ")");
        }
        return Optimizer.preProduce(funExpr);
    }
    
}
