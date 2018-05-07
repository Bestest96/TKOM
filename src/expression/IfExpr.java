package expression;

public class IfExpr implements IExpression {

    private IExpression condition;
    private IExpression expr;

    public IfExpr(IExpression condition, IExpression expr) {
        this.condition = condition;
        this.expr = expr;
    }

    @Override
    public void print() {
        System.out.print("if (");
        condition.print();
        System.out.println(")");
        expr.print();
        System.out.println();
    }

    @Override
    public void translate() {

    }
}
