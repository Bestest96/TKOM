package expression;

public class StringExpr implements IExpression {

    private String string;

    public StringExpr(String string) {
        this.string = string;
    }

    @Override
    public void print() {
        System.out.print(string);
    }

    @Override
    public void translate() {

    }
}
