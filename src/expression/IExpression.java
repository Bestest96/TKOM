package expression;

import context.Type;

public interface IExpression {

    default void print() {
        System.out.println("TODO");
    }

    default String translate() {
        System.out.println("TODO");
        return "TODO";
    }

    default Type type() {
        return null;
    }
}