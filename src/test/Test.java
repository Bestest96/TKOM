package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import main.argument.ExprArgument;
import main.exceptions.TranslationException;
import main.expression.*;

import java.util.Arrays;
import java.util.Collections;

class Test {

    @org.junit.jupiter.api.Test
    void testAddSubExpr() throws TranslationException {
        AddSubExpr expr = new AddSubExpr(new IntExpr(1), "+", new IntExpr(2));
        assertEquals("1 + 2", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testAssignmentExpr() throws TranslationException {
        AssignmentExpr expr = new AssignmentExpr(new IDExpr("id"), new FloatExpr(3.5));
        assertEquals("auto id = 3.5;", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testBoolExpr() {
        BoolExpr expr = new BoolExpr(false);
        assertEquals("false", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testBracedExpr() throws TranslationException {
        BracedExpr expr = new BracedExpr(new AddSubExpr(new IntExpr(1), "+", new IntExpr(2)));
        assertEquals("(1 + 2)", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testBreakExpr() {
        BreakExpr expr = new BreakExpr();
        assertEquals("break;", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testCallFunExpr() throws TranslationException {
        CallFunExpr expr = new CallFunExpr(new AssignmentExpr(new IDExpr("id2"), new IDExpr("c")), Collections.singletonList(new ExprArgument(new IntExpr(5))));
        assertEquals("auto id2 = arma::vec({5});", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testCompareExpr() throws TranslationException {
        CompareExpr expr = new CompareExpr(new IntExpr(10), "==", new IntExpr(11));
        assertEquals("10 == 11", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testCompoundExpr() throws TranslationException {
        AssignmentExpr e1 = new AssignmentExpr(new IDExpr("id3"), new FloatExpr(3.5));
        AssignmentExpr e2 = new AssignmentExpr(new IDExpr("id3"), new FloatExpr(3.5));
        CompoundExpr expr = new CompoundExpr(Arrays.asList(e1, e2));
        assertEquals("{\n\tauto id3 = 3.5;\n\tid3 = 3.5;\n}\nauto id3 = 3.5;", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testFloatExpr() {
        FloatExpr expr = new FloatExpr(33.3);
        assertEquals("33.3", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testForExpr() throws TranslationException {
        ForExpr expr = new ForExpr("i", new RangeExpr(new IntExpr(1), new IntExpr(10)), new AssignmentExpr(new IDExpr("id5"), new FloatExpr(3.5)));
        assertEquals("auto i = 1;\nfor (i = 1; i < 10; ++i)\n\tauto id5 = 3.5;\nauto id5 = 3.5;", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testHexExpr() {
        HexExpr expr = new HexExpr("0xA");
        assertEquals("0xA", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testIdExpr() throws TranslationException {
        IDExpr expr = new IDExpr("id");
        assertEquals("id", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testIfExpr() throws TranslationException {
        IfExpr expr = new IfExpr(new BoolExpr(true), new AssignmentExpr(new IDExpr("id6"), new IntExpr(10)));
        assertEquals("if (true)\n\tauto id6 = 10;\nauto id6 = 10;", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testIndexExpr() throws TranslationException {
        IndexExpr expr = new IndexExpr(new CallFunExpr(new AssignmentExpr(new IDExpr("id7"), new IDExpr("c")), Collections.singletonList(new ExprArgument(new IntExpr(5)))),
                Collections.singletonList(new ExprArgument(new IntExpr(1))));
        assertEquals("auto id7 = arma::vec({5})(1 - 1);", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testIntExpr() {
        IntExpr expr = new IntExpr(12137);
        assertEquals("12137", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testLogicalExpr() throws TranslationException {
        LogicalExpr expr = new LogicalExpr(new IntExpr(1), "&", new IntExpr(2));
        assertEquals("1 && 2", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testMulDivExpr() throws TranslationException {
        MulDivExpr expr = new MulDivExpr(new IntExpr(1), "*", new IntExpr(2));
        assertEquals("1 * 2", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testNegationExpr() throws TranslationException {
        NegationExpr expr = new NegationExpr(new BoolExpr(false));
        assertEquals("!false", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testNextExpr() {
        NextExpr expr = new NextExpr();
        assertEquals("continue;", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testPlusMinusExpr() throws TranslationException {
        PlusMinusExpr expr = new PlusMinusExpr("-", new IntExpr(10));
        assertEquals("-10", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testRangeExpr() throws TranslationException {
        RangeExpr expr = new RangeExpr(new IntExpr(10), new IntExpr(20));
        assertEquals("10:20", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testRepeatExpr() throws TranslationException {
        RepeatExpr expr = new RepeatExpr(new AssignmentExpr(new IDExpr("id8"), new IntExpr(101)));
        assertEquals("do\n{\n\tauto id8 = 101;\n} while(true);\nauto id8 = 101;", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testStringExpr() {
        StringExpr expr = new StringExpr("TKOM");
        assertEquals("TKOM", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testUserOpExpr() throws TranslationException {
        UserOpExpr expr = new UserOpExpr(new IDExpr("id2"), "%*%", new IDExpr("id7"));
        assertEquals("id2 * id7", expr.translate());
    }

    @org.junit.jupiter.api.Test
    void testWhileExpr() throws TranslationException {
        WhileExpr expr = new WhileExpr(new BoolExpr(true), new AssignmentExpr(new IDExpr("id66"), new IntExpr(10)));
        assertEquals("while (true)\n\tauto id66 = 10;\nauto id66 = 10;", expr.translate());
    }

}

