package expression;

public class AddSubExpr implements IExpression {

    private IExpression e1;
    private String op;
    private IExpression e2;

    public AddSubExpr(IExpression e1, String op, IExpression e2) {
        this.e1 = e1;
        this.op = op;
        this.e2 = e2;
    }

    @Override
    public void print() {
        e1.print();
        System.out.print(" " + op + " ");
        e2.print();
    }

    @Override
    public void translate() {

    }
}
