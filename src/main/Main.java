package main;

import main.context.ContextHolder;
import main.exceptions.TranslationException;
import main.expression.IExpression;
import main.gen.RFilter;
import main.gen.RLexer;
import main.gen.RParser;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.*;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        String inputFile = null;
        Boolean continueOnError = false;
        Boolean printParseTree = false;
        if (args.length > 0) {
            if (args[0].equals("help")) {
                printHelp();
                return;
            }
            else
                inputFile = args[0];
        }
        if (args.length > 1)
            ContextHolder.setWriter(args[1]);
        if (args.length > 2)
            continueOnError = Boolean.parseBoolean(args[2]);
        if (args.length > 3)
            printParseTree = Boolean.parseBoolean(args[3]);
        InputStream is = System.in;
        if (inputFile != null) {
            try {
                is = new FileInputStream(inputFile);
            }
            catch (Exception e) {
                System.err.println("Error while reading file! Enabling stdin input.");
                is = System.in;
            }
        }
        CharStream input = CharStreams.fromStream(is);
        RLexer lexer = new RLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RFilter filter = new RFilter(tokens);
        filter.stream();
        tokens.seek(0);
        RParser parser = new RParser(tokens);
        parser.setBuildParseTree(true);
        RuleContext tree = parser.prog();
        List<RParser.ExprContext> expr = ((RParser.ProgContext) tree).expr();
        List<IExpression> iexpr = expr.stream().map(exp -> exp.exp).collect(Collectors.toList());
        ContextHolder.printBeginning();
        PrintWriter printer = ContextHolder.getFileWriter();
        for (IExpression ex : iexpr) {
            try {
                printer.println(ex.translate());
                printer.flush();
            }
            catch (TranslationException e) {
                System.err.println(e.getMessage());
                if (!continueOnError) {
                    System.err.println("Ending translation on this point!");
                    break;
                }
                else
                    System.out.println("Continuing translation without the wrong expression");
            }
            catch (Exception e) {
                System.err.println("Unknown error!");
                System.err.println(e.getMessage());
                System.err.println("Ending translation on this point!");
                break;
            }
        }
        ContextHolder.printEnding();
        if (printParseTree)
            generateParseTree(parser, tree);
    }

    private static void generateParseTree(RParser parser, RuleContext tree) {
        JFrame frame = new JFrame("Antlr AST");
        JPanel panel = new JPanel();
        TreeViewer viewr = new TreeViewer(Arrays.asList(
                parser.getRuleNames()),tree);
        viewr.setScale(1.5);
        panel.add(viewr);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280,720);
        frame.setVisible(true);
    }

    private static void printHelp() {
        System.out.println("----------R -> C++ Translator----------");
        System.out.println("----------       Usage       ----------");
        System.out.println("javac Main.java");
        System.out.println("java Main in out continue tree");
        System.out.println("----------       Params      ----------");
        System.out.println("in - input file to translate, stdin when not provided");
        System.out.println("out - output file for results, stdout when not provided");
        System.out.println("continue - true if translating is to continue after error, false otherwise");
        System.out.println("tree - true if a program should show a parse tree, false otherwise");
    }
}
