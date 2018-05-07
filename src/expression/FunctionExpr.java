package expression;

import argument.IArgument;

import java.util.List;

public class FunctionExpr implements IExpression {

    private List<IArgument> args;
    private IExpression expr;

    public FunctionExpr(List<IArgument> args, IExpression expr) {
        this.args = args;
        this.expr = expr;
    }

    @Override
    public void print() {
        System.out.print("function(");
        for (IArgument i : args) {
            i.print();
            if (args.indexOf(i) != args.size() - 1)
                System.out.print(", ");
        }
        System.out.print(")");
        System.out.println();
        expr.print();
    }

    @Override
    public void translate() {

    }
}
