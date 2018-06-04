package main.expression;

import main.context.ContextHolder;
import main.context.Type;
import main.exceptions.TranslationException;

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
    public String translate() throws TranslationException {
        if (condition.type() != Type.BOOLEAN)
            throw new TranslationException("Wrong if/else expression type!");
        StringBuilder sb = ContextHolder.addIndents();
        sb.append("if (").append(condition.translate()).append(")");
        sb.append("\n");
        ContextHolder.changeContext();
        sb.append(ifExpr.translate());
        sb.append("\n");
        ContextHolder.restoreContext();
        sb.append(ContextHolder.addIndents().toString()).append("}\n");
        sb.append("else ").append(elseExpr.translate());
        return sb.toString();
    }
}
