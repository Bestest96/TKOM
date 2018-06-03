package argument;

import context.Type;
import exceptions.TranslationException;
import expression.IExpression;

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
