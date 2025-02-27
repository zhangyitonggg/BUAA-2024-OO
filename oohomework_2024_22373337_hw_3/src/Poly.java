import java.math.BigInteger;
import java.util.ArrayList;

public class Poly {
    private ArrayList<Mono> monos;
    
    public Poly(ArrayList<Mono> monos) {
        this.monos = monos;
    }
    
    // 构造一个 常数poly, 用于得到1和-1
    public Poly(int a) {
        Mono mono = new Mono(String.valueOf(a),"0", new Poly());
        this.monos = new ArrayList<>();
        this.monos.add(mono);
    }
    
    public Poly(BigInteger a) {
        Mono mono = new Mono(a, BigInteger.ZERO, new Poly());
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
            if (mono.getExp().monos.isEmpty()) {
                newMonos.add(new Mono(mono.getCoef(),mono.getPowExp(), new Poly()));
            }
            else {
                newMonos.add(new Mono(mono.getCoef(),mono.getPowExp(), mono.getExp().deepClone()));
            }
        }
        return new Poly(newMonos);
    }
    
    public Poly addPoly(Poly elsePoly) {
        Poly ansPoly = this.deepClone();
        ArrayList<Mono> ansMonos = ansPoly.getMonos();
        ArrayList<Mono> elseMonos = elsePoly.getMonos();
        if (elseMonos.size() == 0) {
            return ansPoly;
        }
        else if (ansMonos.size() == 0) {
            return elsePoly.deepClone();
        }
        for (Mono elseMono : elseMonos) {
            int flag = 0;
            for (Mono ansMono : ansMonos) {
                if (ansMono.canAdd(elseMono)) {
                    flag = 1;
                    ansMono.setCoef(elseMono.getCoef().add(ansMono.getCoef()));
                    break;
                }
            }
            if (flag == 0) {
                ansMonos.add(new Mono(elseMono.getCoef(),elseMono.getPowExp(),
                        elseMono.getExp().deepClone()));
            }
        }
        return new Poly(Optimizer.deleteZero(ansMonos));
    }
    
    public Poly subPoly(Poly elsePoly) {
        Poly ansPoly = this.deepClone();
        ArrayList<Mono> ansMonos = ansPoly.getMonos();
        ArrayList<Mono> elseMonos = elsePoly.getMonos();
        for (Mono elseMono : elseMonos) {
            int flag = 0;
            for (Mono ansMono : ansMonos) {
                if (ansMono.canAdd(elseMono)) {
                    flag = 1;
                    ansMono.setCoef(elseMono.getCoef().subtract(ansMono.getCoef()));
                    break;
                }
            }
            if (flag == 0) {
                ansMonos.add(new Mono(new BigInteger("0").subtract(elseMono.getCoef()),
                        elseMono.getPowExp(), elseMono.getExp()));
            }
        }
        return new Poly(Optimizer.deleteZero(ansMonos));
    }
    
    public Poly multiPoly(Poly elsePoly) {
        Poly ansPoly = new Poly();
        ArrayList<Mono> elseMonos = elsePoly.getMonos();
        
        for (Mono mono: monos) {
            ArrayList<Mono> tempMonos = new ArrayList<>();
            for (Mono elseMono : elseMonos) {
                BigInteger tempCoef = mono.getCoef().multiply(elseMono.getCoef());
                BigInteger tempPowExp = mono.getPowExp().add(elseMono.getPowExp());
                Poly tempExp = mono.getExp().addPoly(elseMono.getExp());
                tempMonos.add(new Mono(tempCoef,tempPowExp, tempExp));
            }
            Poly tempPoly = new Poly(Optimizer.deleteZero(tempMonos));
            ansPoly = ansPoly.addPoly(tempPoly);
        }
        return ansPoly;
    }
    
    public Poly powPoly(BigInteger powExp) {
        if (powExp.equals(BigInteger.ZERO)) {
            return new Poly(1);
        }
        Poly ansPoly = new Poly(1);
        for (BigInteger i = BigInteger.ZERO; i.compareTo(powExp) < 0;i = i.add(BigInteger.ONE)) {
            ansPoly = ansPoly.multiPoly(this);
        }
        return ansPoly;
    }
    
    public boolean equals(Poly other) {
        Poly tempPoly = this.subPoly(other);
        ArrayList<Mono> tempMono = tempPoly.getMonos();
        return tempMono.size() == 1 && tempMono.get(0).getCoef().equals(BigInteger.ZERO);
    }
    
}
