package main.expression;

import main.context.ContextHolder;
import main.context.Type;
import main.context.VariableData;
import main.exceptions.TranslationException;
import main.utilities.RandomString;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AssignmentExpr implements IExpression {

    private IExpression varName;
    private IExpression varValue;
    private RandomString rs = new RandomString();

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
        final String AUTO = "auto ";
        if (!(varName instanceof IDExpr))
            throw new TranslationException("Unrecognized id expression for a variable!");
        String varID = ((IDExpr) varName).getId();
        String value = varValue.translate();
        Map<String, VariableData> symbols = ContextHolder.getSymbolsTable();
        Map<String, String> mapper = ContextHolder.getLocalSymbolsMapper();
        List<String> localVars = ContextHolder.getActualContextVariables();
        StringBuilder sb = ContextHolder.addIndents();
        Type type = type();
        if (type == null)
            throw new TranslationException("Unrecognized data type!");
        while (mapper.containsKey(varID))
            varID = mapper.get(varID);
        Boolean higher = false;
        if (symbols.containsKey(varID)) {
            VariableData data = symbols.get(varID);
            for (List higherVars : ContextHolder.getContextVariablesList()) {
                if (higherVars.contains(varID)) {
                    higher = true;
                    break;
                }
            }
            if (higher || localVars.contains(varID)) {
                if (data.getType() != type) {
                    varID = generateChangedTypeVar(varID, value, type, symbols, mapper, localVars);
                    sb.append(AUTO);
                }
                else {
                    data.setValue(value);
                    data.setType(type);
                    saveData(varID, data, symbols, localVars);
                }
            }
            else {
                data.setValue(value);
                data.setType(type);
                saveData(varID, data, symbols, localVars);
                sb.append(AUTO);
            }
        }
        else {
            VariableData data = new VariableData();
            data.setType(type);
            data.setValue(value);
            symbols.put(varID, data);
            localVars.add(varID);
            sb.append(AUTO);
        }
        sb.append(varID).append(" = ").append(value).append(";");
        return sb.toString();
    }

    private String generateChangedTypeVar(String varID, String value, Type type, Map<String, VariableData> symbols,
                                        Map<String, String> mapper, List<String> localVars) {
        Boolean goodNewName = false;
        String newName = null;
        while (!goodNewName) {
            newName = getNewName(varID);
            if (!symbols.containsKey(newName)) {
                VariableData newData = symbols.getOrDefault(varID, new VariableData());
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
        return "_" + rs.nextString() + originalName;
    }

    private void saveData(String varID, VariableData data, Map<String, VariableData> symbols, List<String> localVars) {
        symbols.put(varID, data);
        localVars.add(varID);
    }
}
