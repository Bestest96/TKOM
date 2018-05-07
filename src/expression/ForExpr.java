package expression;

public class ForExpr implements IExpression {

    private String id;
    private IExpression values;
    private IExpression expr;

    public ForExpr(String id, IExpression values, IExpression expr) {
        this.id = id;
        this.values = values;
        this.expr = expr;
    }

    @Override
    public void print() {
        System.out.print("for (" + id + " in ");
        values.print();
        System.out.print(") ");
        expr.print();
    }

    @Override
    public void translate() {

    }
}
