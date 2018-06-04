package main.expression;

import main.context.Type;
import main.exceptions.TranslationException;

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
    public String translate() throws TranslationException {
        if (type() == null)
            throw new TranslationException("Incompatible addition/subtraction types!");
        return e1.translate() + " " + op + " " + e2.translate();
    }

    @Override
    public Type type() {
        if (e1.type() == Type.STRING || e2.type() == Type.STRING)
            return null;
        else if ((e1.type() == Type.DOUBLE && e2.type() == Type.INTEGER) || (e1.type() == Type.INTEGER && e2.type() == Type.DOUBLE))
            return Type.DOUBLE;
        return e1.type() == e2.type() ? e1.type() : null;
    }

    public String getOp() {
        return op;
    }

}
