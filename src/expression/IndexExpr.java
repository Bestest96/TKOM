package expression;

import argument.IArgument;

import java.util.List;

public class IndexExpr implements IExpression {

    private IExpression expr;
    private List<IArgument> args;

    public IndexExpr(IExpression expr, List<IArgument> args) {
        this.expr = expr;
        this.args = args;
    }

    @Override
    public void print() {
        expr.print();
        System.out.print("[");
        for (IArgument i : args)
            i.print();
        System.out.print("]");
    }

    @Override
    public void translate() {

    }
}
