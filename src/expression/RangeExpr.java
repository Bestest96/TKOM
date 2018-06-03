package expression;

import context.Type;

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
    public String translate() {
        return e1.translate() + ";" + e2.translate();
    }

    public IExpression getE1() {
        return e1;
    }

    public IExpression getE2() {
        return e2;
    }

    @Override
    public Type type() {
        return Type.VECTOR;
    }
}
