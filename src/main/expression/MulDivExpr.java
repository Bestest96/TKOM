package main.expression;

import main.context.Type;
import main.exceptions.TranslationException;

public class MulDivExpr implements IExpression {

    private IExpression e1;
    private String op;
    private IExpression e2;

    public MulDivExpr(IExpression e1, String op, IExpression e2) {
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
            throw new TranslationException("Wrong multiply/division data types!");
        else if ((e1.type() == Type.MATRIX || e1.type() == Type.VECTOR) && (e2.type() == Type.MATRIX || e2.type() == Type.VECTOR) && op.equals("*"))
            op = "%";
        return e1.translate() + " " + op + " " + e2.translate();
    }

    @Override
    public Type type() {
        if (e1.type() == e2.type())
            return e1.type();
        else if ((e1.type() == Type.DOUBLE || e1.type() == Type.INTEGER) && (e2.type() == Type.MATRIX || e2.type() == Type.VECTOR) && op.equals("*"))
            return e2.type();
        else if ((e2.type() == Type.DOUBLE || e2.type() == Type.INTEGER) && (e1.type() == Type.MATRIX || e1.type() == Type.VECTOR))
            return e1.type();
        else if ((e1.type() == Type.DOUBLE && e2.type() == Type.INTEGER) || (e1.type() == Type.INTEGER && e2.type() == Type.DOUBLE))
            return Type.DOUBLE;
        else
            return null;
    }

    public IExpression getE1() {
        return e1;
    }

    public String getOp() {
        return op;
    }

    public IExpression getE2() {
        return e2;
    }
}
