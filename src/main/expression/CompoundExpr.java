package main.expression;

import main.context.ContextHolder;
import main.exceptions.TranslationException;

import java.util.List;

public class CompoundExpr implements IExpression {

    private List<IExpression> exprlist;

    public CompoundExpr(List<IExpression> exprlist) {
        this.exprlist = exprlist;
    }

    @Override
    public void print() {
        System.out.println("{");
        for (IExpression e : exprlist) {
            System.out.print("\t");
            e.print();
            System.out.println();
        }
        System.out.println("}");
    }

    @Override
    public String translate() throws TranslationException {
        StringBuilder sb = ContextHolder.addIndents();
        sb.append("{\n");
        ContextHolder.changeContext();
        for (IExpression expr: exprlist)
            sb.append(expr.translate()).append("\n");
        ContextHolder.restoreContext();
        sb.append(ContextHolder.addIndents().toString()).append("}");
        if (ContextHolder.getVarsToPrint().length() != 0)
            sb.append("\n").append(ContextHolder.getVarsToPrint());
        return sb.toString();
    }
}
