package expression;

import context.Type;
import exceptions.TranslationException;

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
        if (expr.type().equals(Type.BOOLEAN))
            return expr.type();
        else
            return null;
    }
}
