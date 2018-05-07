package expression;

public class FloatExpr implements IExpression {

    private double value;

    public FloatExpr(double value) {
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
