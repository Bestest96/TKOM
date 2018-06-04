package main.expression;

import main.argument.ExprArgument;
import main.argument.IArgument;
import main.context.Type;
import main.exceptions.TranslationException;

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
    public String translate() throws TranslationException {
        StringBuilder sb = new StringBuilder();
        Boolean isAssignment = false;
        sb.append(expr.translate());
        if (sb.charAt(sb.length() - 1) == ';') {
            sb.deleteCharAt(sb.length() - 1);
            isAssignment = true;
        }
        sb.append("(");
        for (IArgument arg : args) {
            if (arg.type() != Type.INTEGER && arg.type() != Type.DOUBLE)
                throw new TranslationException("Wrong index type!");
            if (!(arg instanceof ExprArgument))
                throw new TranslationException("Wrong argument!");
            ExprArgument argument = (ExprArgument) arg;
            if (!(argument.getValue() instanceof IDExpr) && !(argument.getValue() instanceof IntExpr))
                throw new TranslationException("Wrong argument type!");
            sb.append(argument.getValue().translate()).append(" - 1, ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(")");
        if (isAssignment)
            sb.append(";");
        return sb.toString();
    }
}
