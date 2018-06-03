package argument;

import context.Type;
import exceptions.TranslationException;

public interface IArgument {

    default void print() {
        System.out.print("TO-DO");
    }

    default String translate() throws TranslationException { return null; }

    default Type type() {
        return null;
    }
}
