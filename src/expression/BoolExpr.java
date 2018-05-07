package expression;

public class BoolExpr implements IExpression {

    private boolean value;

    public BoolExpr(boolean value) {
        this.value = value;
    }

    @Override
    public void print() {
        System.out.print(value);
    }

    @Override
    public void translate() {

    }
}
