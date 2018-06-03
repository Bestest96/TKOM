package argument;

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
    public String translate() {
        return value.translate();
    }

    public IExpression getValue() {
        return value;
    }
}
