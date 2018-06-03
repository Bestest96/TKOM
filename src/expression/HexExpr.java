package expression;

import context.Type;

public class HexExpr implements IExpression {

    private String value;

    public HexExpr(String value) {
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
