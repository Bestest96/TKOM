package expression;

public class WhileExpr implements IExpression {

    private IExpression condition;
    private IExpression expr;

    public WhileExpr(IExpression condition, IExpression expr) {
        this.condition = condition;
        this.expr = expr;
    }

    @Override
    public void print() {
        System.out.print("while (");
        condition.print();
        System.out.print(") ");
        expr.print();
    }

    @Override
    public void translate() {

    }
}
