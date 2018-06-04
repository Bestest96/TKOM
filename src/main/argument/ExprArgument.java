package main.argument;

import main.context.Type;
import main.exceptions.TranslationException;
import main.expression.IDExpr;
import main.expression.IExpression;

public class ExprArgument implements IArgument {

    private IExpression value;

    public ExprArgument(IExpression value) {
        this.value = value;
    }

    @Override
    public void print() {
        value.print();
    }

    @Override
    public String translate() throws TranslationException {
        if (value instanceof IDExpr)
            return ((IDExpr) value).getId();
        else
            return value.translate();
    }

    public IExpression getValue() {
        return value;
    }

    @Override
    public Type type() {
        return value.type();
    }
}
