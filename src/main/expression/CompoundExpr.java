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
        StringBuilder sb = ContextHolder.addIndents().deleteCharAt(0);
        if (!sb.toString().contains("\t")) {
            sb.append(ContextHolder.addIndents().toString());
            sb.append(ContextHolder.addIndents().toString()).append("{\n");
            for (IExpression expr: exprlist)
                sb.append(ContextHolder.addIndents().toString()).append(expr.translate()).append("\n");
            sb.append(ContextHolder.addIndents().toString()).append("}");
        }
        else {
            sb.append("{\n");
            for (IExpression expr : exprlist)
                sb.append(expr.translate()).append("\n");
            sb.append(ContextHolder.addIndents().deleteCharAt(0).toString()).append("}");
            return sb.toString();
        }
        return sb.toString();
    }
}
