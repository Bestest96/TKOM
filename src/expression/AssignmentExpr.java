package expression;

import context.ContextHolder;
import context.Type;
import context.VariableData;
import exceptions.TranslationException;
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
    public String translate() throws TranslationException {
        String varID = varName.translate();
        String value = varValue.translate();
        Map<String, VariableData> symbols = ContextHolder.getSymbolsTable();
        Map<String, String> mapper = ContextHolder.getLocalSymbolsMapper();
        Set<String> localVars = ContextHolder.getActualContextVariables();
        StringBuilder sb = ContextHolder.addIndents();
        Type type = type();
        if (type == null)
            throw new TranslationException("Unrecognized data type!");
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
                    data.setGloballyUsed(data.getGloballyUsed() ? true : ContextHolder.getIsGlobalContext());
                    saveData(varID, data, symbols, localVars);
                }
            }
            else {
                data.setValue(value);
                data.setType(type);
                data.setGloballyUsed(data.getGloballyUsed() ? true : ContextHolder.getIsGlobalContext());
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
                newData.setGloballyUsed(newData.getGloballyUsed() ? true : ContextHolder.getIsGlobalContext());
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

    IExpression getVarName() {
        return varName;
    }


    IExpression getVarValue() {
        return varValue;
    }

    void setVarValue(IExpression varValue) {
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
}
