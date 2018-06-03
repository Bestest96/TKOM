package expression;

import context.ContextHolder;
import context.Type;
import context.VariableData;
import utilities.RandomString;

import java.util.Map;
import java.util.Set;

public class AssignmentExpr implements IExpression {

    private IExpression varName;
    private IExpression varValue;
    private RandomString rs = new RandomString();
    private final String VECTOR = "arma::vec";
    private final String MATRIX = "arma::mat";

    public AssignmentExpr(IExpression varName, IExpression varValue) {
        this.varName = varName;
        this.varValue = varValue;
    }

    @Override
    public void print() {
        varName.print();
        System.out.print(" = ");
        varValue.print();
    }

    @Override
    public String translate() {
        String varID = varName.translate();
        String value = varValue.translate();
        Map<String, VariableData> symbols = ContextHolder.getSymbolsTable();
        Map<String, String> mapper = ContextHolder.getLocalSymbolsMapper();
        Set<String> localVars = ContextHolder.getActualContextVariables();
        StringBuilder sb = ContextHolder.addIndents();
        Type type = null;
        if (value.startsWith("arma::vec") || value.startsWith("arma::regspace"))
            type = Type.VECTOR;
        else if (value.startsWith("arma::mat"))
            type = Type.MATRIX;
        else if (value.endsWith("get_n_elem()"))
            type = Type.INTEGER;
        else if (varValue instanceof IDExpr) {
            VariableData valueData = symbols.getOrDefault(value, null);
            if (valueData != null)
                type = valueData.getType();
            else
                throw new RuntimeException();
        }
        else if (varValue instanceof CallFunExpr) {
            CallFunExpr expr = (CallFunExpr) varValue;
            if (!(expr.getFunction() instanceof IDExpr))
                throw new RuntimeException();
            IDExpr funId = (IDExpr) expr.getFunction();
            String same = funId.getId();
            if (funId.equals("length"))
                type = Type.INTEGER;
        }
        else if (varValue instanceof AddSubExpr) {
            AddSubExpr expr = (AddSubExpr) varValue;
            IExpression value1 = expr.getE1();
            IExpression value2 = expr.getE2();
            String op = expr.getOp();
            Type type1 = null;
            Type type2 = null;
            if (value1 instanceof IDExpr) {
                IDExpr iDExpr = (IDExpr) value1;
                VariableData vData = symbols.getOrDefault(iDExpr.getId(), null);
                if (vData != null)
                    type1 = vData.getType();
            }
            if (value2 instanceof IDExpr) {
                IDExpr iDExpr = (IDExpr) value2;
                VariableData vData = symbols.getOrDefault(iDExpr.getId(), null);
                if (vData != null)
                    type2 = vData.getType();
            }
            if (type1 != type2 || type1 == null)
                throw new RuntimeException();
            else {
                type = type1;
                value = value1.translate() + " " + op + " " + value2.translate();
            }

        }
        if (type == null)
            type = getType(value);
        if (type == null)
            throw new RuntimeException();
        while (mapper.containsKey(varID))
            varID = mapper.get(varID);
        if (symbols.containsKey(varID)) {
            VariableData data = symbols.get(varID);
            if (data.getGloballyUsed() || localVars.contains(varID)) {
                if (!data.getType().equals(type)) {
                    varID = generateChangedTypeVar(varID, value, type, symbols, mapper, localVars);
                    sb.append("auto ");
                }
                else {
                    data.setValue(value);
                    data.setType(type);
                    data.setGloballyUsed(ContextHolder.getIsGlobalContext());
                    saveData(varID, data, symbols, localVars);
                }
            }
            else {
                data.setValue(value);
                data.setType(type);
                data.setGloballyUsed(ContextHolder.getIsGlobalContext());
                saveData(varID, data, symbols, localVars);
                sb.append("auto ");
            }
        }
        else {
            VariableData data = new VariableData();
            data.setGloballyUsed(ContextHolder.getIsGlobalContext());
            data.setType(type);
            data.setValue(value);
            symbols.put(varID, data);
            localVars.add(varID);
            sb.append("auto ");
        }
        sb.append(varID).append(" = ").append(value).append(";");
        return sb.toString();
    }

    private String generateChangedTypeVar(String varID, String value, Type type, Map<String, VariableData> symbols,
                                        Map<String, String> mapper, Set<String> localVars) {
        Boolean goodNewName = false;
        String newName = null;
        while (!goodNewName) {
            newName = getNewName(varID);
            if (!symbols.containsKey(newName)) {
                VariableData newData = symbols.getOrDefault(varID, new VariableData());
                newData.setGloballyUsed(ContextHolder.getIsGlobalContext());
                newData.setType(type);
                newData.setValue(value);
                symbols.put(newName, newData);
                mapper.put(varID, newName);
                localVars.add(newName);
                goodNewName = true;
            }
        }
        return newName;
    }

    @Override
    public Type type() {
        return varValue.type();
    }

    public IExpression getVarName() {
        return varName;
    }

    public void setVarName(IExpression varName) {
        this.varName = varName;
    }

    public IExpression getVarValue() {
        return varValue;
    }

    public void setVarValue(IExpression varValue) {
        this.varValue = varValue;
    }

    private String getNewName(String originalName) {
        StringBuilder sb = new StringBuilder();
        sb.append("gen_").append(rs.nextString()).append("_").append(originalName);
        return sb.toString();
    }

    private void saveData(String varID, VariableData data, Map<String, VariableData> symbols, Set<String> localVars) {
        symbols.put(varID, data);
        localVars.add(varID);
    }

    private Type getType(String value) {
        if (varValue instanceof IntExpr || varValue instanceof HexExpr)
            return Type.INTEGER;
        else if (varValue instanceof FloatExpr)
            return Type.DOUBLE;
        else if (varValue instanceof StringExpr)
            return Type.STRING;
        else if (value.equals(VECTOR))
            return Type.VECTOR;
        else if (value.equals(MATRIX))
            return Type.MATRIX;
        else
            return null;
    }
}
