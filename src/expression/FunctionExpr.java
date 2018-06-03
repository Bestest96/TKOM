package expression;

import argument.IArgument;
import context.ContextHolder;

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
    public String translate() {
        StringBuilder sb = new StringBuilder();
        sb.append("(std::exception& ");
        if (args.size() > 1)
            throw new RuntimeException();
        sb.append(args.get(0).translate()).append(")");
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
}
