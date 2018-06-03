package expression;

import context.ContextHolder;

public class WhileExpr implements IExpression {

    private IExpression condition;
    private IExpression expr;

    public WhileExpr(IExpression condition, IExpression expr) {
        this.condition = condition;
        this.expr = expr;
    }

    @Override
    public void print() {
        System.out.print("while (");
        condition.print();
        System.out.print(") ");
        expr.print();
    }

    @Override
    public String translate() {
        StringBuilder sb = ContextHolder.addIndents();
        sb.append("while (").append(condition.translate()).append(")");
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
