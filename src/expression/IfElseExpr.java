package expression;

public class IfElseExpr implements IExpression {

    private IExpression condition;
    private IExpression ifExpr;
    private IExpression elseExpr;

    public IfElseExpr(IExpression condition, IExpression ifExpr, IExpression elseExpr) {
        this.condition = condition;
        this.ifExpr = ifExpr;
        this.elseExpr = elseExpr;
    }
}
