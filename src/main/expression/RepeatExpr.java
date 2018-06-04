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
        if (!(expr instanceof CompoundExpr)) {
            sb.append("{\n");
            ContextHolder.changeContext();
            sb.append(expr.translate());
            ContextHolder.restoreContext();
            sb.append("\n").append(ContextHolder.addIndents().toString()).append("} while(true);");
            sb.append("\n").append(ContextHolder.getVarsToPrint());
        }
        else {
            sb.append(expr.translate());
            sb.delete(sb.length() - 1 - ContextHolder.getVarsToPrint().length(), sb.length());
            sb.append(" while(true);\n");
            sb.append(ContextHolder.getVarsToPrint());
        }
        return sb.toString();
    }
}
