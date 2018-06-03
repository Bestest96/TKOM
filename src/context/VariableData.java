package context;

import java.util.Objects;

public class VariableData {
    private String value;
    private Type type;
    private Boolean isGloballyUsed;

    public VariableData(String value, Type type, Boolean isGloballyUsed) {
        this.value = value;
        this.type = type;
        this.isGloballyUsed = isGloballyUsed;
    }

    public VariableData() {
        this("", null, true);
    }

    public String getValue() {
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

    public Boolean getGloballyUsed() {
        return isGloballyUsed;
    }

    public void setGloballyUsed(Boolean globallyUsed) {
        isGloballyUsed = globallyUsed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableData that = (VariableData) o;
        return Objects.equals(value, that.value) &&
                type == that.type &&
                Objects.equals(isGloballyUsed, that.isGloballyUsed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type, isGloballyUsed);
    }
}
