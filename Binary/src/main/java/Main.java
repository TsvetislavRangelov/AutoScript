import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.*;

class MyListener extends BinaryBaseListener
{
    @Override public void enterLine(BinaryParser.LineContext ctx)
    {
        // TODO: investigate contents of 'ctx'
        System.err.println("enterMyStart()");
    }

    @Override public void exitLine(BinaryParser.LineContext ctx)
    {
        // TODO: investigate contents of 'ctx'
        System.err.println("exitMyStart()");
    }

    @Override public void visitTerminal(TerminalNode node)
    {
        System.err.println("terminal-node: '" + node.getText() + "'");
        // TODO: print line+column, token's type, etc.
    }
    // TODO: override other methods of 'MyGrammarBaseListener'
}

public class Main
{
    public static void main(String[] args) throws Exception
    {
        CharStream input = CharStreams.fromStream(System.in);
        BinaryLexer lexer = new BinaryLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // TODO: print the lexer's vocabulary and the actual list of tokens

        BinaryParser parser = new BinaryParser(tokens);

        ParseTree tree = parser.line();

        MyListener m = new MyListener();
        ParseTreeWalker.DEFAULT.walk(m, tree);
    }
}