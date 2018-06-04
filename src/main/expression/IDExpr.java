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
        else if (id.startsWith("arma::det") || id.startsWith("det"))
            return Type.DOUBLE;
        VariableData data = ContextHolder.getSymbolsTable().getOrDefault(id, null);
        return data != null ? data.getType() : null;
    }

    public String getId() {
        while (ContextHolder.getLocalSymbolsMapper().get(id) != null)
            id = ContextHolder.getLocalSymbolsMapper().get(id);
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
