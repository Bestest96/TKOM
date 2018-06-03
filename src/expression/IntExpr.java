package expression;

import context.Type;

public class IntExpr implements IExpression {

    private int value;

    public IntExpr(Integer value) {
        this.value = value;
    }

    @Override
    public void print() {
        System.out.print(value);
    }

    @Override
    public String translate() {
        return String.valueOf(value);
    }

    @Override
    public Type type() {
        return Type.INTEGER;
    }
}
