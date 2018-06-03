package expression;

import context.Type;

public class FloatExpr implements IExpression {

    private double value;

    public FloatExpr(double value) {
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
        return Type.DOUBLE;
    }
}
