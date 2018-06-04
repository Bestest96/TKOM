package main.expression;

import main.context.ContextHolder;
import main.context.Type;
import main.context.VariableData;
import main.exceptions.TranslationException;

public class IDExpr implements IExpression {

    private String id;

    public IDExpr(String id) {
        this.id = id;
    }

    @Override
    public void print() {
        System.out.print(id);
    }

    @Override
    public String translate() throws TranslationException {
        if (type() == null)
            throw new TranslationException("Variable type unrecognized!");
        while (ContextHolder.getLocalSymbolsMapper().get(id) != null)
            id = ContextHolder.getLocalSymbolsMapper().get(id);
        VariableData data = ContextHolder.getSymbolsTable().get(id);
        if (data == null)
            return id;
        if (!data.getGloballyUsed() && !ContextHolder.getActualContextVariables().contains(id)) {
            StringBuilder sb = ContextHolder.addIndents();
            sb.append("auto ").append(id).append(" = ").append(data.getValue()).append(";");
            data.setGloballyUsed(data.getGloballyUsed() ? true : ContextHolder.getIsGlobalContext());
            ContextHolder.getSymbolsTable().put(id, data);
            ContextHolder.getActualContextVariables().add(id);
            ContextHolder.getFileWriter().println(sb.toString());
        }
        return id;
    }

    @Override
    public Type type() {
        if (id.startsWith("arma::vec") || id.startsWith("arma::regspace"))
            return Type.VECTOR;
        else if (id.startsWith("arma::mat"))
            return Type.MATRIX;
        else if (id.endsWith(".n_elem") || id.startsWith("length"))
            return Type.INTEGER;
        VariableData data = ContextHolder.getSymbolsTable().getOrDefault(id, null);
        return data != null ? data.getType() : null;
    }

    public String getId() {
        while (ContextHolder.getLocalSymbolsMapper().get(id) != null)
            id = ContextHolder.getLocalSymbolsMapper().get(id);
        VariableData data = ContextHolder.getSymbolsTable().get(id);
        if (data == null)
            return id;
        if (!data.getGloballyUsed() && !ContextHolder.getActualContextVariables().contains(id)) {
            StringBuilder sb = ContextHolder.addIndents();
            sb.append("auto ").append(id).append(" = ").append(data.getValue()).append(";");
            data.setGloballyUsed(data.getGloballyUsed() ? true : ContextHolder.getIsGlobalContext());
            ContextHolder.getSymbolsTable().put(id, data);
            ContextHolder.getActualContextVariables().add(id);
            ContextHolder.getFileWriter().println(sb.toString());
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
