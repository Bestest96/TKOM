package expression;

import argument.IArgument;

import java.util.List;

public class CallFunExpr implements IExpression {

    private IExpression function;
    private List<IArgument> args;

    public CallFunExpr(IExpression function, List<IArgument> args) {
        this.function = function;
        this.args = args;
    }

    @Override
    public void print() {
        function.print();
        System.out.print("(");
        for (IArgument i : args) {
            i.print();
            if (args.indexOf(i) != args.size() - 1)
                System.out.print(", ");
        }
        System.out.print(")");
    }

    @Override
    public void translate() {

    }
}
