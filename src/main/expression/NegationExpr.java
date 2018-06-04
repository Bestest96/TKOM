package main.expression;

import main.context.Type;
import main.exceptions.TranslationException;

public class NegationExpr implements IExpression {

    private IExpression expr;

    public NegationExpr(IExpression expr) {
        this.expr = expr;
    }

    @Override
    public String translate() throws TranslationException {
        if (type() != Type.BOOLEAN)
            throw new TranslationException("Wrong negation expression type!");
        return "!" + expr.translate();
    }

    @Override
    public Type type() {
        if (expr.type() == Type.BOOLEAN || expr.type() == Type.INTEGER || expr.type() == Type.DOUBLE)
            return Type.BOOLEAN;
        else
            return null;
    }
}
