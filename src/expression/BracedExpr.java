package expression;

import context.Type;
import exceptions.TranslationException;

public class BracedExpr implements IExpression {

    private IExpression expression;

    public BracedExpr(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public String translate() throws TranslationException {
        return "(" + expression.translate() + ")";
    }

    @Override
    public Type type() {
        return expression.type();
    }
}
