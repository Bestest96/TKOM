package main.expression;

import main.argument.IArgument;
import main.argument.IDArgument;
import main.context.ContextHolder;
import main.exceptions.TranslationException;

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
    public String translate() throws TranslationException {
        StringBuilder sb = new StringBuilder();
        sb.append("(std::exception& ");
        if (args.size() > 1)
            throw new TranslationException("Too much arguments for exception!");
        IArgument arg = args.get(0);
        if (!(arg instanceof IDArgument))
            throw new TranslationException("Wrong argument type for tryCatch!");
        IDArgument idArg = (IDArgument) arg;
        sb.append(idArg.getId()).append(")\n");
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
