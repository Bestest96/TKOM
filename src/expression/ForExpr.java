package expression;

import context.ContextHolder;

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
    public String translate() {
        if (!(values instanceof RangeExpr) && !(values instanceof CallFunExpr) && !(values instanceof IDExpr))
            throw new RuntimeException();
        if (values instanceof RangeExpr) {
            RangeExpr rangeExpr = (RangeExpr) values;
            IExpression e1 = rangeExpr.getE1();
            IExpression e2 = rangeExpr.getE2();
            if (!(e1 instanceof IntExpr) && !(e1 instanceof HexExpr) && !(e1 instanceof FloatExpr)
                    && !(e1 instanceof IDExpr))
                throw new RuntimeException();



            StringBuilder sb = new StringBuilder();
            sb.append(new AssignmentExpr(new IDExpr(id), new IntExpr(Integer.parseInt(e1.translate()))).translate()).append("\n");
            sb.append(ContextHolder.addIndents().toString());
            sb.append("for (").append(id).append(" = ").append(e1.translate());
            sb.append("; ").append(id).append(" < ").append(e2.translate()).append("; ++").append(id).append(")");
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
        return null;
    }
}
