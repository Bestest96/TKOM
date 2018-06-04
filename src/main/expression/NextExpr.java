package main.expression;

import main.context.ContextHolder;

public class NextExpr implements IExpression {

    public NextExpr() {
    }

    @Override
    public void print() {
        System.out.print("next");
    }

    @Override
    public String translate() {
        return ContextHolder.addIndents().toString() +  "continue;";
    }
}
