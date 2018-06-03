package argument;

import context.Type;
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

    @Override
    public String translate() {
        return id;
    }

    public String getId() {
        return id;
    }

    @Override
    public Type type() {
        return value.type();
    }
}
