package main.expression;

import main.context.Type;
import main.exceptions.TranslationException;

public class UserOpExpr implements IExpression {

    private IExpression e1;
    private String userOp;
    private IExpression e2;

    public UserOpExpr(IExpression e1, String userOp, IExpression e2) {
        this.e1 = e1;
        this.userOp = userOp;
        this.e2 = e2;
    }

    @Override
    public String translate() throws TranslationException {
        if (type() == null || (!userOp.equals("%*%") && !userOp.equals("%/%")))
            throw new TranslationException("Unrecognized elements type or wrong user operator!");
        userOp = userOp.replace("%", "");
        return e1.translate() + " " + userOp + " " + e2.translate();

    }

    @Override
    public Type type() {
        if ((e1.type() == Type.MATRIX && e2.type() == Type.VECTOR) || e1.type() == Type.VECTOR && e2.type() == Type.MATRIX)
            return Type.MATRIX;
        else if (e1.type() == Type.VECTOR && e2.type() == Type.VECTOR)
            return Type.VECTOR;
        else if (e1.type() == Type.MATRIX && e2.type() == Type.MATRIX)
            return Type.MATRIX;
        else
            return null;

    }
}
