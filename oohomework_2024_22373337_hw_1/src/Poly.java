import java.math.BigInteger;
import java.util.ArrayList;

public class Poly {
    private ArrayList<Mono> monos;
    
    public Poly(ArrayList<Mono> monos) {
        this.monos = monos;
    }
    
    // 构造一个 常数poly, 用于得到1和-1
    public Poly(int a) {
        Mono mono = new Mono(String.valueOf(a),"0");
        this.monos = new ArrayList<>();
        this.monos.add(mono);
    }
    
    public Poly() {
        this.monos = new ArrayList<>();
    }
    
    public ArrayList<Mono> getMonos() {
        return this.monos;
    }
    
    public Poly deepClone() {
        ArrayList<Mono> newMonos = new ArrayList<>();
        for (Mono mono : this.monos) {
            newMonos.add(new Mono(mono.getCoef(),mono.getExp()));
        }
        return new Poly(newMonos);
    }
    
    public Poly addPoly(Poly elsePoly) {
        Poly ansPoly = this.deepClone();
        ArrayList<Mono> ansMonos = ansPoly.getMonos();
        ArrayList<Mono> elseMonos = elsePoly.getMonos();
        for (Mono elseMono : elseMonos) {
            int flag = 0;
            for (Mono ansMono : ansMonos) {
                if (elseMono.getExp() == ansMono.getExp()) {
                    flag = 1;
                    ansMono.setCoef(elseMono.getCoef().add(ansMono.getCoef()));
                    break;
                }
            }
            if (flag == 0) {
                ansMonos.add(new Mono(elseMono.getCoef(),elseMono.getExp()));
            }
        }
        return new Poly(ansMonos);
    }
    
    public Poly multiPoly(Poly elsePoly) {
        Poly ansPoly = new Poly();
        ArrayList<Mono> elseMonos = elsePoly.getMonos();
        
        for (Mono mono: monos) {
            ArrayList<Mono> tempMonos = new ArrayList<>();
            for (Mono elseMono : elseMonos) {
                BigInteger tempCoef = mono.getCoef().multiply(elseMono.getCoef());
                int tempExp = mono.getExp() + elseMono.getExp();
                tempMonos.add(new Mono(tempCoef,tempExp));
            }
            Poly tempPoly = new Poly(tempMonos);
            ansPoly = ansPoly.addPoly(tempPoly);
        }
        return ansPoly;
    }
    
    public Poly powPoly(int exp) {
        if (exp == 0) {
            return new Poly(1);
        }
        
        Poly ansPoly = new Poly(1);
        for (int i = 0; i < exp; i++) {
            ansPoly = ansPoly.multiPoly(this);
        }
        return ansPoly;
    }
}
