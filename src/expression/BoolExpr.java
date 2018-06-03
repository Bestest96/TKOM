package expression;

import context.Type;

public class BoolExpr implements IExpression {

    private boolean value;

    public BoolExpr(boolean value) {
        this.value = value;
    }

    @Override
    public void print() {
        System.out.print(value);
    }

    @Override
    public String translate() {
        return Boolean.toString(value).toLowerCase();
    }

    @Override
    public Type type() {
        return Type.BOOLEAN;
    }
}
