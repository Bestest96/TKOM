package main.expression;

import main.context.ContextHolder;
import main.exceptions.TranslationException;

public class RepeatExpr implements IExpression {

    private IExpression expr;

    public RepeatExpr(IExpression expr) {
        this.expr = expr;
    }

    @Override
    public void print() {
        System.out.print("repeat ");
        expr.print();
    }

    @Override
    public String translate() throws TranslationException {
        StringBuilder sb = ContextHolder.addIndents();
        sb.append("do").append("\n");
        ContextHolder.changeContext();
        if (!(expr instanceof CompoundExpr))
            sb.append(ContextHolder.addIndents().toString()).deleteCharAt(sb.length() - 1).append("{\n");
        sb.append(expr.translate());
        if (!(expr instanceof CompoundExpr))
            sb.append("\n").append(ContextHolder.addIndents().toString()).deleteCharAt(sb.length() - 1).append("}");
        sb.append(" while(true);\n");
        sb.append(ContextHolder.restoreContext());
        return sb.toString();
    }
}
