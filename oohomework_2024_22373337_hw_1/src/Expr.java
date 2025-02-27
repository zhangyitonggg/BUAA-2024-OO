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
}
