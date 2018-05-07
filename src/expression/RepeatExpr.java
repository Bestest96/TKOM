package expression;

public class RepeatExpr implements IExpression {

    private IExpression expr;

    public RepeatExpr(IExpression expr) {
        this.expr = expr;
    }

    @Override
    public void print() {
        System.out.print("repeat ");
        expr.print();
    }

    @Override
    public void translate() {

    }
}
