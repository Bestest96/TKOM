package main.argument;

import main.context.Type;
import main.exceptions.TranslationException;

public interface IArgument {

    default void print() {
        System.out.print("TO-DO");
    }

    default String translate() throws TranslationException { return null; }

    default Type type() {
        return null;
    }
}
