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
        System.out.println("\033[1mLexer vocabulary:\033[0m");
        // we start looping from 1 since token enumeration starts from 1.
        for(int i = 1; i < vocab.getMaxTokenType(); i++){
            System.out.println(vocab.getDisplayName(i));
        }

        System.out.println();

        BinaryParser parser = new BinaryParser(tokens);

        ParseTree start = parser.start();

        // we pass the vocab to the listener.
        MainBinaryListener m = new MainBinaryListener(vocab);
        ParseTreeWalker.DEFAULT.walk(m, start);
    }
}

final class MainBinaryListener extends BinaryBaseListener
{

    // we pass the vocabulary to the listener from the lexer in Main.
    // Vocabulary is an interface so it is optimal to DI.
    private final Vocabulary _vocab;
    MainBinaryListener(Vocabulary vocab){
        _vocab = vocab;
    }

    @Override public void enterLine(BinaryParser.LineContext ctx)
    {
        displayInfo(ctx.getText());
    }

    @Override public void exitLine(BinaryParser.LineContext ctx)
    {
        System.out.println("\033[1mExiting Line\033[0m " + ctx.getText());
        System.out.println();
    }

    /**
     * Visits a terminal node in the parse tree.
     * @param node The current node in the tree.
     */
    @Override public void visitTerminal(TerminalNode node)
    {
        // we use the vocab here to get the symbolic name for the rule number
        String symbolicName = _vocab.getSymbolicName(node.getSymbol().getType());
        int lineNumber = node.getSymbol().getLine();

        System.out.println("Line: " + lineNumber
                + ' '
                + '|' + " Type: " + symbolicName);
    }

    /**
     * Displays the number of addition (+) operations,
     * as well as the highest integer in the parse line.
     * @param parseLine the line the parser is reading.
     */
    private void displayInfo(String parseLine){
        System.out.println("\033[1mInput line(s):\033[0m " + parseLine);
        int additions = countAdditions(parseLine);
        System.out.println("\033[1mAddition count:\033[0m " + additions);

        int highestNumber = getLargestNumber(parseLine);
        System.out.println("\033[1mLargest integer is:\033[0m " + highestNumber);
        System.out.println();
    }

    /**
     * Gets the largest integer from a list of integers.
     * @param expression The expression we're parsing.
     * @return The highest integer in the list
     */
    private int getLargestNumber(String expression){
        int highest = 0;
        if(expression.isEmpty()){
            return highest;
        }
        // we need only the numbers from within the expression.
        String[] numbers = expression.split("[^0-9]+");
        for(String number : numbers){
            if(!number.isEmpty()){
                int curr = Integer.parseInt(number);
                if(curr > highest){
                    highest = curr;
                }
            }
        }
        return highest;
    }

    /**
     * Counts the additions in a line.
     * @param line The line containing the expression.
     * @return The number of additions
     */
    private int countAdditions(String line){
        int result = 0;
        for(char c : line.toCharArray()){
            if(c == '+'){
                result++;
            }
        }
        return result;
    }

}