package expression;

import context.ContextHolder;

public class IfElseExpr implements IExpression {

    private IExpression condition;
    private IExpression ifExpr;
    private IExpression elseExpr;

    public IfElseExpr(IExpression condition, IExpression ifExpr, IExpression elseExpr) {
        this.condition = condition;
        this.ifExpr = ifExpr;
        this.elseExpr = elseExpr;
    }

    @Override
    public String translate() {
        StringBuilder sb = ContextHolder.addIndents();
        sb.append("if (").append(condition.translate()).append(")");
        if (!(ifExpr instanceof CompoundExpr)) {
            sb.append("\n{\n");
            ContextHolder.setNumOfIndents(ContextHolder.getNumOfIndents() + 1);
            sb.append(ifExpr.translate());
            sb.append("\n");
            ContextHolder.setNumOfIndents(ContextHolder.getNumOfIndents() - 1);
            sb.append(ContextHolder.addIndents().toString()).append("}\n");
        }
        else {
            sb.append("\n");
            sb.append(ifExpr.translate());
        }
        sb.append("else ").append(elseExpr.translate());
        return sb.toString();
    }
}
