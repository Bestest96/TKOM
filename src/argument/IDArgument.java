package argument;

import expression.IExpression;

public class IDArgument implements IArgument {

    private String id;
    private IExpression value;

    public IDArgument(String id, IExpression value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public void print() {
        if (value == null)
            System.out.print(id);
        else {
            System.out.print(id + " = ");
            value.print();
        }
    }
}
