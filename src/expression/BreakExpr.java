package expression;

import context.ContextHolder;

public class BreakExpr implements IExpression {

    public BreakExpr() {}

    @Override
    public void print() {
        System.out.print("break");
    }

    @Override
    public String translate() {
        return ContextHolder.addIndents().toString() + "break;";
    }
}
