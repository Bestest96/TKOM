package expression;

import context.ContextHolder;

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
    public String translate() {
        return id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
