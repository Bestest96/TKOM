package expression;

public class RepeatExpr implements IExpression {

    private IExpression expr;

    public RepeatExpr(IExpression expr) {
        this.expr = expr;
    }
}
