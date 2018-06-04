package main.expression;

import main.context.ContextHolder;
import main.context.Type;
import main.exceptions.TranslationException;

public class ForExpr implements IExpression {

    private String id;
    private IExpression values;
    private IExpression expr;

    public ForExpr(String id, IExpression values, IExpression expr) {
        this.id = id;
        this.values = values;
        this.expr = expr;
    }

    @Override
    public void print() {
        System.out.print("for (" + id + " in ");
        values.print();
        System.out.print(") ");
        expr.print();
    }

    @Override
    public String translate() throws TranslationException {
        if (values.type() != Type.VECTOR && values.type() != Type.INTEGER && values.type() != Type.DOUBLE)
            throw new TranslationException("Wrong for iteration range!");
        StringBuilder sb = new StringBuilder();
        if (values instanceof RangeExpr) {
            RangeExpr rangeExpr = (RangeExpr) values;
            IExpression e1 = rangeExpr.getE1();
            IExpression e2 = rangeExpr.getE2();
            sb.append(new AssignmentExpr(new IDExpr(id), new IntExpr(Integer.parseInt(e1.translate()))).translate()).append("\n");
            sb.append(ContextHolder.addIndents().toString());
            sb.append("for (").append(id).append(" = ").append(e1.translate());
            sb.append("; ").append(id).append(" < ").append(e2.translate()).append("; ++").append(id).append(")");
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
        }
        else
            throw new TranslationException("Unsupported for collection!");
        return sb.toString();
    }
}
