package main.expression;

import main.context.ContextHolder;
import main.context.Type;
import main.exceptions.TranslationException;

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
    public String translate() throws TranslationException {
        if (condition.type() != Type.BOOLEAN && condition.type() != Type.INTEGER && condition.type() != Type.DOUBLE)
            throw new TranslationException("Wrong if expression type!");
        StringBuilder sb = ContextHolder.addIndents();
        sb.append("if (").append(condition.translate()).append(")");
        sb.append("\n");
        ContextHolder.changeContext();
        sb.append(expr.translate());
        sb.append("\n");
        sb.append(ContextHolder.restoreContext());
        return sb.toString();
    }
}
