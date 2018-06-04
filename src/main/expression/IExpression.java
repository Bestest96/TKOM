package main.expression;

import main.context.Type;
import main.exceptions.TranslationException;

public interface IExpression {

    default void print() {
        System.out.println("TODO");
    }

    default String translate() throws TranslationException {
        System.out.println("TODO");
        return "TODO";
    }

    default Type type() {
        return null;
    }
}