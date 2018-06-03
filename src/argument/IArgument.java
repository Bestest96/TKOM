package argument;

public interface IArgument {

    default void print() {
        System.out.print("TO-DO");
    }

    default String translate() { return null; }
}
