package expression;

import context.Type;
import exceptions.TranslationException;

public class LogicalExpr implements IExpression {

    private IExpression expr1;
    private String op;
    private IExpression expr2;

    public LogicalExpr(IExpression expr1, String op, IExpression expr2) {
        this.expr1 = expr1;
        this.op = op;
        this.expr2 = expr2;
    }

    @Override
    public String translate() throws TranslationException {
        if (type() != Type.BOOLEAN)
            throw new TranslationException("Incompatible addition/subtraction types!");
        return expr1.translate() + " " + op + " " + expr2.translate();
    }

    @Override
    public Type type() {
        if (expr1.type().equals(Type.BOOLEAN) && expr2.type().equals(Type.BOOLEAN))
            return Type.BOOLEAN;
        else
            return null;
    }
}
