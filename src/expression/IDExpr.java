package expression;

import context.ContextHolder;
import context.Type;
import context.VariableData;
import exceptions.TranslationException;

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
        return id;
    }

    @Override
    public Type type() {
        if (id.startsWith("arma::vec") || id.startsWith("arma::regspace"))
            return Type.VECTOR;
        else if (id.startsWith("arma::mat"))
            return Type.MATRIX;
        else if (id.endsWith(".get_n_elem()") || id.startsWith("length"))
            return Type.INTEGER;
        VariableData data = ContextHolder.getSymbolsTable().getOrDefault(id, null);
        return data != null ? data.getType() : null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
