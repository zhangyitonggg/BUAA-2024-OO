import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Optimizer {
    private ArrayList<Mono> monos = new ArrayList<>();
    
    public String preProduce(String str) {
        return mergeSign(deleteSpace(str));
    }
    
    public String laterProduce(ArrayList<Mono> monos) {
        this.monos = monos;
        this.deleteZero();
        this.changeOrder();
        return mergeSign(getString());
    }
    
    public String deleteSpace(String str) {
        return str.replaceAll("\\s+", "");
    }
    
    public String mergeSign(String withoutSpace) {
        StringBuilder sb = new StringBuilder();
        int flag = 0;
        int sign = 1;
        for (int iter = 0; iter < withoutSpace.length(); iter++) {
            char ch = withoutSpace.charAt(iter);
            if (ch == '+') {
                flag = 1;
            } else if (ch == '-') {
                flag = 1;
                sign *= -1;
            } else {
                if (flag == 1 && sign == 1) {
                    sb.append('+');
                } else if (flag == 1 && sign == -1) {
                    sb.append('-');
                }
                sb.append(ch);
                flag = 0;
                sign = 1;
            }
        }
        return sb.toString();
    }
    
    public void deleteZero() {
        for (Iterator<Mono> iterator = monos.iterator();iterator.hasNext();) {
            Mono mono = iterator.next();
            if (mono.getCoef().compareTo(BigInteger.ZERO) == 0) {
                iterator.remove();
            }
        }
        if (monos.isEmpty()) {
            monos.add(new Mono("0","0"));
        }
    }
    
    public void changeOrder() {
        for (int i = 0; i < monos.size(); i++) {
            if (monos.get(i).getCoef().toString().charAt(0) != '-') {
                Collections.swap(monos, 0, i);
                break;
            }
        }
    }
    
    public String getString() {
        StringBuilder sb = new StringBuilder();
        sb.append(monos.get(0).toString());
        for (int i = 1; i < monos.size(); i++) {
            sb.append("+").append(monos.get(i).toString());
        }
        return sb.toString();
    }
}
