package main.expression;

import main.context.ContextHolder;
import main.context.Type;
import main.exceptions.TranslationException;

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
    public String translate() throws TranslationException {
        if (condition.type() != Type.BOOLEAN)
            throw new TranslationException("Wrong while condition type!");
        StringBuilder sb = ContextHolder.addIndents();
        sb.append("while (").append(condition.translate()).append(")");
        sb.append("\n");
        if (!(expr instanceof CompoundExpr)) {
            ContextHolder.changeContext();
            sb.append(expr.translate());
            ContextHolder.restoreContext();
            if (ContextHolder.getVarsToPrint().length() != 0)
                sb.append("\n").append(ContextHolder.getVarsToPrint());
        }
        else
            sb.append(expr.translate());
        return sb.toString();
    }
}
