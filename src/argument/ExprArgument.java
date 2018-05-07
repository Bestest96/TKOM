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
}
