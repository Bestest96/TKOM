package main.context;

public class VariableData {
    private String value;
    private Type type;

    private VariableData(String value, Type type) {
        this.value = value;
        this.type = type;
    }

    public VariableData() {
        this("", null);
    }

    String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
