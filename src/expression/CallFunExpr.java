package expression;

import argument.ExprArgument;
import argument.IArgument;
import context.ContextHolder;
import context.Type;

import java.util.ArrayList;
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
    public String translate() {
        String nameR;
        if (function instanceof AssignmentExpr)
            nameR = ((AssignmentExpr) function).getVarValue().translate();
        else if (function instanceof IDExpr)
            nameR = ((IDExpr) function).getId();
        else
            return null;

        StringBuilder toRet = new StringBuilder();

        switch (nameR) {
            case "c": {
                toRet.append("arma::vec({");
                int argsSize = args.size();
                for (int i = 0; i < argsSize - 1; ++i)
                    toRet.append(args.get(i).translate()).append(", ");
                if (argsSize != 0)
                    toRet.append(args.get(argsSize - 1).translate());
                toRet.append("})");
                return returnDataType(toRet);
            }
            case "tryCatch": {
                toRet = ContextHolder.addIndents();
                int argsSize = args.size();
                if (argsSize != 2 || (!(args.get(0) instanceof ExprArgument) || !(args.get(1) instanceof ExprArgument)))
                    throw new RuntimeException();
                IExpression tryExpr = ((ExprArgument) args.get(0)).getValue();
                IExpression catchExpr = ((ExprArgument) args.get(1)).getValue();
                toRet.append("try");
                if (!(tryExpr instanceof CompoundExpr)) {
                    toRet.append(" {\n");
                    ContextHolder.changeContext();
                    toRet.append(tryExpr.translate());
                    toRet.append("\n");
                    ContextHolder.restoreContext();
                    toRet.append(ContextHolder.addIndents().toString()).append("}\n");
                }
                else
                    toRet.append(tryExpr.translate()).append("\n");
                toRet.append(ContextHolder.addIndents().toString()).append("catch ");
                if (!(catchExpr instanceof AssignmentExpr))
                    throw new RuntimeException();
                AssignmentExpr expr = (AssignmentExpr) catchExpr;
                if (!expr.getVarName().translate().equals("error"))
                    throw new RuntimeException();
                toRet.append(expr.getVarValue().translate());
                break;
            }
            case "print": {
                toRet = ContextHolder.addIndents();
                toRet.append("std::cout << ");
                for (IArgument arg : args)
                    toRet.append(arg.translate().replace("\t", "")).append(" << ");
                toRet.append("std::endl;");
                break;
            }
            case "length": {
                if (args.size() != 1)
                    throw new RuntimeException();
                toRet.append(args.get(0).translate()).append(".get_n_elem()");
                return returnDataType(toRet);
            }
            case "seq": {
                toRet.append("arma::regspace<arma::vec>(");
                String delta = null;
                int argsSize = args.size();
                if (argsSize < 2 || argsSize > 3)
                    throw new RuntimeException();
                toRet.append(args.get(0).translate()).append(", ");
                if (argsSize == 2)
                    toRet.append(args.get(argsSize - 1).translate()).append(")");
                else {
                    ExprArgument exprArg;
                    if (args.get(2) instanceof ExprArgument)
                        exprArg = (ExprArgument) args.get(2);
                    else
                        throw new RuntimeException();
                    String argumentId = ((AssignmentExpr) exprArg.getValue()).getVarName().translate();
                    String argumentValue = ((AssignmentExpr) exprArg.getValue()).getVarValue().translate();
                    if (!argumentId.equals("by"))
                        throw new RuntimeException();
                    delta = argumentValue;
                    toRet.append(delta).append(", ");
                    toRet.append(args.get(argsSize - 2).translate()).append(")");
                }
                return returnDataType(toRet);
            }
            case "matrix": {
                toRet.append("arma::mat");
                toRet.append("(std::vector<double>{");
                int argsSize = args.size();
                if (argsSize > 0 && !(args.get(0) instanceof ExprArgument))
                    throw new RuntimeException();
                else if (argsSize != 3 && argsSize != 4)
                    throw new RuntimeException();
                List<String> matrixElements = ((CallFunExpr) ((ExprArgument) args.get(0)).getValue()).getArgsInArray();
                Boolean byRow = false;
                String numRows = "1";
                String numCols = "1";
                Boolean nrowUsed = false;
                Boolean ncolUsed = false;
                Boolean byrowUsed = false;
                for (int i = 1; i < argsSize; ++i) {
                    IArgument arg = args.get(i);
                    ExprArgument exprArg = null;
                    if (arg instanceof ExprArgument)
                        exprArg = (ExprArgument) arg;
                    else
                        throw new RuntimeException();
                    String argumentId = ((AssignmentExpr) exprArg.getValue()).getVarName().translate();
                    String argumentValue = ((AssignmentExpr) exprArg.getValue()).getVarValue().translate();
                    switch (argumentId) {
                        case "nrow": {
                            if (nrowUsed)
                                throw new RuntimeException();
                            else if (Integer.parseInt(argumentValue) < 1)
                                throw new RuntimeException();
                            numRows = argumentValue;
                            nrowUsed = true;
                            break;
                        }
                        case "ncol": {
                            if (ncolUsed)
                                throw new RuntimeException();
                            else if (Integer.parseInt(argumentValue) < 1)
                                throw new RuntimeException();
                            numCols = argumentValue;
                            ncolUsed = true;
                            break;
                        }
                        case "byrow": {
                            if (byrowUsed)
                                throw new RuntimeException();
                            else if (!argumentValue.equals("true") && !argumentValue.equals("false"))
                                throw new RuntimeException();
                            byRow = Boolean.parseBoolean(argumentValue);
                            byrowUsed = true;
                            break;
                        }
                        default:
                            throw new RuntimeException();
                    }
                }
                if (!byRow) {
                    for (int i = 0; i < matrixElements.size() - 1; ++i)
                        toRet.append(matrixElements.get(i)).append(", ");
                    toRet.append(matrixElements.get(matrixElements.size() - 1));
                }
                else {
                    int rowCounter = 0;
                    while (rowCounter != Integer.parseInt(numRows)) {
                        int columnCounter = 0;
                        while (columnCounter < Integer.parseInt(numCols) - 1) {
                            toRet.append(matrixElements.get(rowCounter + columnCounter * Integer.parseInt(numRows))).append(", ");
                            ++columnCounter;
                        }
                        if (rowCounter != Integer.parseInt(numRows) - 1)
                            toRet.append(matrixElements.get(rowCounter + columnCounter * Integer.parseInt(numRows))).append(", ");
                        else
                            toRet.append(matrixElements.get(rowCounter + columnCounter * Integer.parseInt(numRows)));
                        ++rowCounter;
                    }
                }
                toRet.append("}.data(), ");
                toRet.append(numRows).append(", ").append(numCols).append(")");
                return returnDataType(toRet);
            }
            default:
                return null;
        }
        return toRet.toString();
    }

    private List<String> getArgsInArray() {
        List<String> arguments = new ArrayList<>();
        for (IArgument arg : args)
            arguments.add(arg.translate());
        return arguments;
    }

    private String returnDataType(StringBuilder sb) {
        if (function instanceof AssignmentExpr) {
            AssignmentExpr expr = (AssignmentExpr) function;
            expr.setVarValue(new IDExpr(sb.toString()));
            return expr.translate();
        }
        else if (function instanceof IDExpr) {
            IDExpr expr = (IDExpr) function;
            expr.setId(sb.toString());
            return expr.translate();
        }
        return null;
    }

    @Override
    public Type type() {
        return function.type();
    }

    public IExpression getFunction() {
        return function;
    }
}
