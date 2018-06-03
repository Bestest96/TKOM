package expression;

import context.ContextHolder;
import context.Type;

public class MulDivExpr implements IExpression {

    private IExpression e1;
    private String op;
    private IExpression e2;

    public MulDivExpr(IExpression e1, String op, IExpression e2) {
        this.e1 = e1;
        this.op = op;
        this.e2 = e2;
    }

    @Override
    public void print() {
        e1.print();
        System.out.print(" " + op + " ");
        e2.print();
    }

    @Override
    public String translate() {
        StringBuilder sb = ContextHolder.addIndents();
        sb.append(e1.translate()).append(" ").append(op).append(" ").append(e2.translate());
        return sb.toString();
    }

    @Override
    public Type type() {
        return e1.type() == e2.type() ? e1.type() : null;
    }

    public IExpression getE1() {
        return e1;
    }

    public String getOp() {
        return op;
    }

    public IExpression getE2() {
        return e2;
    }
}
