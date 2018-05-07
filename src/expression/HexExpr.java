package expression;

public class HexExpr implements IExpression {

    private String value;

    public HexExpr(String value) {
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
