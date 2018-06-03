package expression;

import argument.ExprArgument;
import argument.IArgument;
import context.ContextHolder;

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
    public String translate() {
        StringBuilder sb = new StringBuilder();
        sb.append(expr.translate()).append("(");
        for (IArgument arg : args) {
            if (!(arg instanceof ExprArgument))
                throw new RuntimeException();
            ExprArgument argument = (ExprArgument) arg;
            if (!(argument.getValue() instanceof IDExpr) && !(argument.getValue() instanceof IntExpr))
                throw new RuntimeException();
            sb.append(argument.getValue().translate()).append(" - 1, ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(")");
        return sb.toString();
    }
}
