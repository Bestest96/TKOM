package expression;

public class IntExpr implements IExpression {

    private int value;

    public IntExpr(Integer value) {
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
