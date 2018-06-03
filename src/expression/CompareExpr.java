package expression;

import context.Type;

public class CompareExpr implements IExpression {

    private IExpression e1;
    private String op;
    private IExpression e2;

    public CompareExpr(IExpression e1, String op, IExpression e2) {
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
    public String translate() {
        StringBuilder sb = new StringBuilder();
        sb.append(e1.translate()).append(" ").append(op).append(" ").append(e2.translate());
        return sb.toString();
    }

    @Override
    public Type type() {
        return Type.BOOLEAN;
    }
}
