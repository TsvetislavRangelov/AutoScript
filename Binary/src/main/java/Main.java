import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;



public class Main
{
    public static void main(String[] args) throws Exception
    {
        CharStream input = CharStreams.fromStream(System.in);
        BinaryLexer lexer = new BinaryLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Prints the lexer's vocabulary up to WHITESPACE
        Vocabulary vocab = lexer.getVocabulary();
        for(int i = 0; i < vocab.getMaxTokenType(); i++){
            System.out.println(vocab.getDisplayName(i));
        }
        System.out.println("Tokens: " + tokens.getText());

        BinaryParser parser = new BinaryParser(tokens);

        ParseTree tree = parser.line();

        MainBinaryListener m = new MainBinaryListener(vocab);
        ParseTreeWalker.DEFAULT.walk(m, tree);
    }
}

final class MainBinaryListener extends BinaryBaseListener
{

    // we pass the vocabulary to the listener from the lexer in Main
    private final Vocabulary _vocab;
    MainBinaryListener(Vocabulary vocab){
        _vocab = vocab;
    }

    @Override public void enterLine(BinaryParser.LineContext ctx)
    {
        // TODO: investigate contents of 'ctx'
        System.out.println(ctx.getText());
    }

    @Override public void exitLine(BinaryParser.LineContext ctx)
    {

        // TODO: investigate contents of 'ctx'
        System.out.println("Exiting Line " + ctx.getText());
    }

    // we use the vocab here to get the symbolic name for the rule number
    @Override public void visitTerminal(TerminalNode node)
    {

        System.out.println("Line: " + node.getSymbol().getLine()
                + ' '
                + '|' + " Type: " + _vocab.getSymbolicName(node.getSymbol().getType()));
        // TODO: print line+column, token's type, etc.
    }
    // TODO: override other methods of 'MyGrammarBaseListener'
}