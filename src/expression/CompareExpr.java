package expression;

import context.Type;
import exceptions.TranslationException;

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
    public String translate() throws TranslationException {
        if (type() == null)
            throw new TranslationException("Wrong data types for comparison");
        return e1.translate() + " " + op + " " + e2.translate();
    }

    @Override
    public Type type() {
        if (e1.type().equals(e2.type()))
            return Type.BOOLEAN;
        else return null;
    }
}
