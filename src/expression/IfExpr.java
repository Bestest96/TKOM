package expression;

import context.ContextHolder;

public class IfExpr implements IExpression {

    private IExpression condition;
    private IExpression expr;

    public IfExpr(IExpression condition, IExpression expr) {
        this.condition = condition;
        this.expr = expr;
    }

    @Override
    public void print() {
        System.out.print("if (");
        condition.print();
        System.out.println(")");
        expr.print();
        System.out.println();
    }

    @Override
    public String translate() {
        StringBuilder sb = ContextHolder.addIndents();
        sb.append("if (").append(condition.translate()).append(")");
        sb.append("\n");
        if (!(expr instanceof CompoundExpr)) {
            ContextHolder.changeContext();
            sb.append(expr.translate());
            sb.append("\n");
            ContextHolder.restoreContext();
        }
        else
            sb.append(expr.translate());
        return sb.toString();
    }
}
