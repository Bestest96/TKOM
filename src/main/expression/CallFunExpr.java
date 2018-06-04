package main.expression;

import main.argument.ExprArgument;
import main.argument.IArgument;
import main.context.ContextHolder;
import main.context.Type;
import main.exceptions.TranslationException;

import java.util.ArrayList;
import java.util.List;

public class CallFunExpr implements IExpression {

    private IExpression function;
    private List<IArgument> args;

    private final String BREAK = "break;\n";

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
    public String translate() throws TranslationException {
        String nameR;
        if (function instanceof AssignmentExpr)
            nameR = ((IDExpr)((AssignmentExpr) function).getVarValue()).getId();
        else if (function instanceof IDExpr)
            nameR = ((IDExpr) function).getId();
        else
            throw new TranslationException("Wrong function usage!");

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
            case "switch": {
                toRet = ContextHolder.addIndents();
                int argsSize = args.size();
                if (argsSize < 2 && !(args.get(0) instanceof ExprArgument))
                    throw new TranslationException("Wrong args count for switch!");
                IExpression switchArg = ((ExprArgument) args.get(0)).getValue();
                if (switchArg instanceof IDExpr) {
                    String mappedName = ContextHolder.getLocalSymbolsMapper().get(((IDExpr) switchArg).getId());
                    if (mappedName != null)
                        switchArg = new IDExpr(mappedName);
                }
                if (switchArg.type() != Type.STRING && switchArg.type() != Type.INTEGER)
                    throw new TranslationException("Wrong switch condition!");
                toRet.append("switch (");
                if (switchArg.type() == Type.STRING)
                    toRet.append("str2int(").append(switchArg.translate()).append("))\n").append(ContextHolder.addIndents().toString()).append("{\n");
                else
                    toRet.append(switchArg.translate()).append(")\n").append(ContextHolder.addIndents().toString()).append("{\n");
                ContextHolder.changeContext();
                for (int i = 1; i < argsSize - 1; ++i) {
                    toRet.append(ContextHolder.addIndents().toString()).append("case ");
                    if (!(args.get(i) instanceof ExprArgument))
                        throw new TranslationException("Wrong switch argument!");
                    IExpression caseArg = ((ExprArgument) args.get(i)).getValue();
                    if (caseArg instanceof AssignmentExpr) {
                        IExpression caseValue = ((AssignmentExpr) caseArg).getVarName();
                        IExpression caseExpr = ((AssignmentExpr) caseArg).getVarValue();
                        if (switchArg.type() == Type.STRING)
                            toRet.append("str2int(").append(caseValue.translate()).append(")");
                        else
                            toRet.append(caseValue.translate());
                        toRet.append(":\n");
                        toRet.append(returnCaseExpr(caseExpr));
                    }
                    else if (caseArg instanceof CompoundExpr) {
                        toRet.append(i).append(":\n");
                        toRet.append(caseArg.translate()).append("\n").append(ContextHolder.addIndents().toString());
                        toRet.append("\t").append(BREAK);
                    }
                }
                toRet.append(ContextHolder.addIndents().toString()).append("default:\n");
                if (!(args.get(argsSize - 1) instanceof ExprArgument))
                    throw new TranslationException("Wrong default argument!");
                IExpression caseArg = ((ExprArgument) args.get(argsSize - 1)).getValue();
                if (caseArg instanceof AssignmentExpr) {
                    IExpression caseValue = ((AssignmentExpr) caseArg).getVarName();
                    IExpression caseExpr = ((AssignmentExpr) caseArg).getVarValue();
                    toRet.append(caseValue.translate()).append(":\n");
                    toRet.append(returnCaseExpr(caseExpr));
                }
                else if (caseArg instanceof CompoundExpr) {
                    toRet.append(caseArg.translate()).append("\n").append(ContextHolder.addIndents().toString());
                    toRet.append("\t").append(BREAK);
                }
                ContextHolder.restoreContext();
                toRet.append(ContextHolder.addIndents().toString()).append("}");
                break;
            }
            case "tryCatch": {
                toRet = ContextHolder.addIndents();
                int argsSize = args.size();
                if (argsSize != 2 || (!(args.get(0) instanceof ExprArgument) || !(args.get(1) instanceof ExprArgument)))
                    throw new TranslationException("Wrong try/catch argument count!");
                IExpression tryExpr = ((ExprArgument) args.get(0)).getValue();
                IExpression catchExpr = ((ExprArgument) args.get(1)).getValue();
                toRet.append("try\n");
                if (!(tryExpr instanceof CompoundExpr)) {
                    toRet.append(ContextHolder.addIndents().toString()).append("{\n");
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
                    throw new TranslationException("Wrong catch expression argument!");
                AssignmentExpr expr = (AssignmentExpr) catchExpr;
                if (!((IDExpr)expr.getVarName()).getId().equals("error"))
                    throw new TranslationException("Wrong catch clause name!");
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
                    throw new TranslationException("Wrong arguments count for length!");
                toRet.append(args.get(0).translate()).append(".n_elem");
                return returnDataType(toRet);
            }
            case "seq": {
                toRet.append("arma::regspace<arma::vec>(");
                String delta = null;
                int argsSize = args.size();
                if (argsSize < 2 || argsSize > 3)
                    throw new TranslationException("Wrong arguments count for seq!");
                toRet.append(args.get(0).translate()).append(", ");
                if (argsSize == 2)
                    toRet.append(args.get(argsSize - 1).translate()).append(")");
                else {
                    ExprArgument exprArg;
                    if (args.get(2) instanceof ExprArgument)
                        exprArg = (ExprArgument) args.get(2);
                    else
                        throw new TranslationException("Wrong delta argument!");
                    String argumentId = ((AssignmentExpr) exprArg.getValue()).getVarName().translate();
                    String argumentValue = ((AssignmentExpr) exprArg.getValue()).getVarValue().translate();
                    if (!argumentId.equals("by"))
                        throw new TranslationException("Wrong delta argument name!");
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
                    throw new TranslationException("Wrong matrix arguments!");
                else if (argsSize != 3 && argsSize != 4)
                    throw new TranslationException("Wrong matrix arguments count");
                if (!(((ExprArgument) args.get(0)).getValue() instanceof CallFunExpr))
                    throw new TranslationException("Missing matrix elements argument!");
                List<String> matrixElements = ((CallFunExpr) ((ExprArgument) args.get(0)).getValue()).getArgsInArray();
                Boolean byRow = false;
                String numRows = "1";
                String numCols = "1";
                Boolean nrowUsed = false;
                Boolean ncolUsed = false;
                Boolean byrowUsed = false;
                for (int i = 1; i < argsSize; ++i) {
                    IArgument arg = args.get(i);
                    ExprArgument exprArg;
                    if (arg instanceof ExprArgument)
                        exprArg = (ExprArgument) arg;
                    else
                        throw new TranslationException("Wrong matrix argument type!");
                    if (!(exprArg.getValue() instanceof AssignmentExpr))
                        throw new TranslationException("Wrong matrix argument type!");
                    String argumentId = ((IDExpr)((AssignmentExpr) exprArg.getValue()).getVarName()).getId();
                    String argumentValue = ((AssignmentExpr) exprArg.getValue()).getVarValue().translate();
                    switch (argumentId) {
                        case "nrow": {
                            if (nrowUsed)
                                throw new TranslationException("nrow already used as Matrix argument!");
                            else if (Integer.parseInt(argumentValue) < 1)
                                throw new TranslationException("Wrong nrow number!");
                            numRows = argumentValue;
                            nrowUsed = true;
                            break;
                        }
                        case "ncol": {
                            if (ncolUsed)
                                throw new TranslationException("ncol already used as matrix argument!");
                            else if (Integer.parseInt(argumentValue) < 1)
                                throw new TranslationException("Wrong ncol number!");
                            numCols = argumentValue;
                            ncolUsed = true;
                            break;
                        }
                        case "byrow": {
                            if (byrowUsed)
                                throw new TranslationException("byrow already used as matrix argument!");
                            else if (!argumentValue.equals("true") && !argumentValue.equals("false"))
                                throw new TranslationException("Wrong byrow value!");
                            byRow = Boolean.parseBoolean(argumentValue);
                            byrowUsed = true;
                            break;
                        }
                        default:
                            throw new TranslationException("Wrong matrix argument!");
                    }
                }
                if (!ncolUsed || !nrowUsed)
                    throw new TranslationException("No row/column parameter(s) for matrix!");
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

    private String returnCaseExpr(IExpression caseExpr) throws TranslationException {
        StringBuilder toRet = new StringBuilder();
        if (!(caseExpr instanceof CompoundExpr)) {
            ContextHolder.changeContext();
            toRet.append(caseExpr.translate());
            toRet.append("\n").append(ContextHolder.addIndents().toString()).append(BREAK);
            ContextHolder.restoreContext();
        }
        else {
            toRet.append(caseExpr.translate()).append("\n").append(ContextHolder.addIndents().toString());
            toRet.append("\t").append(BREAK);
        }
        return toRet.toString();
    }

    private List<String> getArgsInArray() throws TranslationException {
        List<String> arguments = new ArrayList<>();
        for (IArgument arg : args)
            arguments.add(arg.translate());
        return arguments;
    }

    private String returnDataType(StringBuilder sb) throws TranslationException {
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
