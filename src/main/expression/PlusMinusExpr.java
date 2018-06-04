package main.expression;

import main.context.Type;
import main.exceptions.TranslationException;

public class PlusMinusExpr implements IExpression {

    private String op;
    private IExpression expression;

    public PlusMinusExpr(String op, IExpression expression) {
        this.op = op;
        this.expression = expression;
    }

    @Override
    public String translate() throws TranslationException {
        if (type() == null)
            throw new TranslationException("Wrong logical expression type!");
        return op + expression.translate();
    }

    @Override
    public Type type() {
        if (expression.type() == Type.INTEGER || expression.type() == Type.DOUBLE)
            return expression.type();
        return null;
    }
}
