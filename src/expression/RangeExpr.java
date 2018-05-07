package expression;

public class RangeExpr implements IExpression {

    private IExpression e1;
    private IExpression e2;

    public RangeExpr(IExpression e1, IExpression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public void print() {
        e1.print();
        System.out.print(":");
        e2.print();
    }

    @Override
    public void translate() {

    }
}
