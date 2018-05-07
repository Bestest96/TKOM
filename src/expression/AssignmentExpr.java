package expression;

public class AssignmentExpr implements IExpression {

    private IExpression varName;
    private IExpression varValue;

    public AssignmentExpr(IExpression varName, IExpression varValue) {
        this.varName = varName;
        this.varValue = varValue;
    }

    @Override
    public void print() {
        varName.print();
        System.out.print(" = ");
        varValue.print();
        System.out.println();
    }
}
