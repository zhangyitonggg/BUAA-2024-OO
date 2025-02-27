import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Optimizer {
    public static String preProduce(String str) {
        return mergeSign(deleteSpace(str));
    }
    
    public static String laterProduce(Poly poly) {
        ArrayList<Mono> monos = poly.getMonos();
        deleteZero(monos);
        changeOrder(monos);
        deleteZeroExp(poly);
        extractGcd(poly);
        return mergeSign(getString(poly.getMonos()));  //getString()得到的是所有Mono相加的形式
    }
    
    public static String deleteSpace(String str) {
        return str.replaceAll("\\s+", "");
    }
    
    public static String mergeSign(String withoutSpace) {
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
    
    public static ArrayList<Mono> deleteZero(ArrayList<Mono> monos) {
        for (Iterator<Mono> iterator = monos.iterator(); iterator.hasNext(); ) {
            Mono mono = iterator.next();
            if (mono.getCoef().compareTo(BigInteger.ZERO) == 0) {
                iterator.remove();
            }
        }
        if (monos.isEmpty()) {
            monos.add(new Mono("0", "0", new Poly()));
        }
        return monos;
    }
    
    public static ArrayList<Mono> changeOrder(ArrayList<Mono> monos) {
        for (int i = 0; i < monos.size(); i++) {
            monos.get(0).setExp(new Poly(monos.get(0).getExp().getMonos()));
        }
        for (int i = 0; i < monos.size(); i++) {
            if (monos.get(i).getCoef().toString().charAt(0) != '-') {
                Collections.swap(monos, 0, i);
                break;
            }
        }
        return monos;
    }
    
    public static void deleteZeroExp(Poly poly) {
        if (poly.getMonos().isEmpty()) {
            return;
        }
        for (Mono mono : poly.getMonos()) {
            Poly nextPoly = mono.getExp();
            if (nextPoly.getMonos().isEmpty()) {
                continue;
            }
            else if (nextPoly.getMonos().size() == 1 &&
                    nextPoly.getMonos().get(0).getCoef().equals(BigInteger.ZERO)) {
                mono.setExp(new Poly());
            }
        }
    }
    
    // 如果应该提取gcd返回gcd，否则返回0
    public static BigInteger getGcd(ArrayList<Mono> monos) {
        if (monos.isEmpty()) {
            return BigInteger.ZERO;
        }
        BigInteger bigGcd = monos.get(0).getCoef();
        if (bigGcd.compareTo(BigInteger.ZERO) < 0) {
            bigGcd = bigGcd.multiply(new BigInteger("-1"));
        }
        if (monos.size() == 1) {
            if (monos.get(0).getPowExp().equals(BigInteger.ZERO) &&
                    monos.get(0).getExp().getMonos().isEmpty()) {
                return BigInteger.ZERO;
            }
            else if (!(monos.get(0).getCoef().equals(new BigInteger("1")) ||
                    monos.get(0).getCoef().equals(new BigInteger("-1")))) {
                return bigGcd;
            }
        }

        for (int iter = 1; iter < monos.size(); ++iter) {
            bigGcd = monos.get(iter).getCoef().gcd(bigGcd);
        }
        
        if (bigGcd.equals(BigInteger.ONE)) {
            return BigInteger.ZERO;
        }
        
        int posNum = 1 + bigGcd.toString().length();
        int negNum = 0; // 实际上是最小缩短的长度
        for (int iter = 0; iter < monos.size(); ++iter) {
            BigInteger quotient = monos.get(iter).getCoef().divide(bigGcd);
            int change = monos.get(iter).getCoef().toString().length()
                        - quotient.toString().length();
            negNum += change;  // 懒得优化了
        }
        if (negNum >= posNum) {
            return bigGcd;
        }
        return BigInteger.ZERO;
    }
    
    public static void extractGcd(Poly poly) {
        ArrayList<Mono> monos = poly.getMonos();
        if (monos.isEmpty()) {
            return;
        }
        for (int iter = 0; iter < monos.size(); ++iter) {
            Mono mono = monos.get(iter);
            if (!mono.getExp().getMonos().isEmpty()) {
                BigInteger bigGcd = getGcd(mono.getExp().getMonos());
                if (!bigGcd.equals(BigInteger.ZERO)) {
                    mono.setPolyExp(bigGcd);
                    for (Mono monoSon : mono.getExp().getMonos()) {
                        BigInteger bigInteger = monoSon.getCoef();
                        monoSon.setCoef(bigInteger.divide(bigGcd));
                    }
                }
            }
        }
    }
    
    public static String getString(ArrayList<Mono> monos) {
        StringBuilder sb = new StringBuilder();
        sb.append(monos.get(0).toString());
        for (int i = 1; i < monos.size(); i++) {
            sb.append("+").append(monos.get(i).toString());
        }
        return sb.toString();
    }
}

