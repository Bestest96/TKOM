package expression;

import context.Type;
import exceptions.TranslationException;

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
    public String translate() throws TranslationException {
        if (type() == null)
            throw new TranslationException("Unrecognized range data type!");
        return e1.translate() + ";" + e2.translate();
    }

    IExpression getE1() {
        return e1;
    }

    IExpression getE2() {
        return e2;
    }

    @Override
    public Type type() {
        if ((e1.type().equals(Type.INTEGER) || e1.type().equals(Type.DOUBLE)) && (e2.type().equals(Type.INTEGER) || e2.type().equals(Type.DOUBLE)))
            return Type.VECTOR;
        else
            return null;
    }
}
