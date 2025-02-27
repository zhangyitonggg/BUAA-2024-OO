import java.util.ArrayList;

public class Expr {
    private final ArrayList<Term> terms;
    
    public Expr() {
        this.terms = new ArrayList<>();
    }
    
    public void addTerm(Term term) {
        this.terms.add(term);
    }
    
    public Poly toPoly() {
        Poly poly = new Poly();
        for (Term term : terms) {
            poly = poly.addPoly(term.toPoly());
        }
        return poly;
    }
    
    public Poly toDerivative() {
        Poly poly = new Poly();
        for (Term term : terms) {
            poly = poly.addPoly(term.toDerivative());
        }
        return poly;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (terms.get(0).getSign() == -1) {
            sb.append("-");
        }
        sb.append(terms.get(0).toString());
        for (int i = 1; i < terms.size(); i++) {
            if (terms.get(i).getSign() == -1) {
                sb.append("-");
            }
            else {
                sb.append("+");
            }
            sb.append(terms.get(i).toString());
        }
        return sb.toString();
    }
}
