import gen.RFilter;
import gen.RLexer;
import gen.RParser;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.*;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        String inputFile = null;
        if ( args.length>0 ) inputFile = args[0];
        InputStream is = System.in;
        if ( inputFile!=null ) {
            is = new FileInputStream(inputFile);
        }
        CharStream input = CharStreams.fromStream(is);
        RLexer lexer = new RLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // Print tokens BEFORE filtering
//		tokens.fill();
//		for (Object tok : tokens.getTokens()) {
//			System.out.println(tok);
//		}
        RFilter filter = new RFilter(tokens);
        filter.stream(); // call start rule: stream
        tokens.seek(0);
        // Print tokens AFTER filtering
		for (Object tok : tokens.getTokens()) {
			System.out.println(tok);
		}
        RParser parser = new RParser(tokens);
        parser.setBuildParseTree(true);
        RuleContext tree = parser.prog();
        List<RParser.ExprContext> expr = ((RParser.ProgContext) tree).expr();
        for (RParser.ExprContext exp : expr)
            if (exp.exp != null) {
                exp.exp.print();
                System.out.println();
            }
        //tree.save(parser, "/tmp/R.ps"); // Generate postscript
        System.out.println(tree.toStringTree(parser));
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
}
