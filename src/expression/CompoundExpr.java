package expression;

import java.util.List;

public class CompoundExpr implements IExpression {

    private List<IExpression> exprlist;

    public CompoundExpr(List<IExpression> exprlist) {
        this.exprlist = exprlist;
    }

    @Override
    public void print() {
        System.out.println("{");
        for (IExpression e : exprlist) {
            System.out.print("\t");
            e.print();
            System.out.println();
        }
        System.out.println("}");
    }
}
