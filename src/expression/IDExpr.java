package expression;

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
    public void translate() {

    }
}
