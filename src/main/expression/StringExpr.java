package main.expression;

import main.context.Type;

public class StringExpr implements IExpression {

    private String string;

    public StringExpr(String string) {
        this.string = string;
    }

    @Override
    public void print() {
        System.out.print(string);
    }

    @Override
    public String translate() {
        return string;
    }

    @Override
    public Type type() {
        return Type.STRING;
    }
}
