import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Main
{
    public static void main(String[] args) throws Exception {
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
        // MainBinaryListener m = new MainBinaryListener(vocab);
        //ParseTreeWalker.DEFAULT.walk(m, start);


        // we pass the vocab to the visitor.
        MainBinaryVisitor visitor = new MainBinaryVisitor(vocab);
        visitor.visit(start);
        
    }
}


final class MainBinaryListener extends BinaryBaseListener {

    // we pass the vocabulary to the listener from the lexer in Main.
    // Vocabulary is an interface so it is optimal to DI.
    private final Vocabulary _vocab;
    MainBinaryListener(Vocabulary vocab) {
        _vocab = vocab;
    }

    private List<Integer> nums=new ArrayList<Integer>();
    private List<String> binary_ops=new ArrayList<String>();

    @Override public void enterLine(BinaryParser.LineContext ctx) {
        nums.clear();
        binary_ops.clear();
        displayInfo(ctx.getText());
    }

    @Override public void exitLine(BinaryParser.LineContext ctx) {
        System.out.println();
        if(isCalculationSupported()){
            int result = processCalculation();
            System.out.println("\033[1mResult is:\033[0m " + result );
        }
        else{
            System.out.println("\033[1mResult:\033[0m expression is not supported");

        }
        System.out.println("\033[1mExiting Line\033[0m " + ctx.getText());
        System.out.println("---------------------------");
    }

    @Override public void exitExpr(BinaryParser.ExprContext ctx) {
        // has reached last leaf
        if(ctx.getChildCount() == 1){
            nums.add(Integer.parseInt(ctx.getText()));
        }
    }

    @Override public void exitBinary_op(BinaryParser.Binary_opContext ctx) {
        binary_ops.add(ctx.getText());
    }

    /**
     * Visits a terminal node in the parse tree.
     * @param node The current node in the tree.
     */
    @Override public void visitTerminal(TerminalNode node) {
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
    private void displayInfo(String parseLine) {
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
    private int getLargestNumber(String expression) {
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
    private int countAdditions(String line) {
        int result = 0;
        for(char c : line.toCharArray()){
            if(c == '+'){
                result++;
            }
        }
        return result;
    }

    /**
     * Goes through the collected numbers and operations and calculates them.
     * @return the result from the expression.
     */
    private int processCalculation() {
        int result = 0; 
        for (int i = 0; i< nums.size(); i++) {
            if (i == 0){
                result = nums.get(i); 
            }
            else{
                result = calculate(result, nums.get(i), binary_ops.get(i-1));
            }
        }
        return result; 
    }

    /**
     * Checks if the current expression can be calculated.
     * @return true if the expression is supported.
     */
    private boolean isCalculationSupported() {
        boolean hasDivOrMul = binary_ops.contains("/") || binary_ops.contains("*");
        boolean hasAddOrSub = binary_ops.contains("+") || binary_ops.contains("-");
        boolean hasNegation = binary_ops.contains("~");

        if(hasNegation || (hasAddOrSub && hasDivOrMul)){
            return false;
        }
        return true;
    }

    /**
     * Calculates the expression based on the operation.
     * @param a first digit.
     * @param b second digit.
     * @param op operation.
     * @return the calculated result.
     */
    private int calculate(int a, int b, String op) {
        switch(op) {
            case "+":
              return a + b; 
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                return a / b;
            case "<<":
                return a << b;
            case "v": 
                return a ^ b;
            default:
                return 0;                          
          }       
    }
}

final class MainBinaryVisitor extends BinaryBaseVisitor<Integer> {

    private final Vocabulary _vocab;
    MainBinaryVisitor(Vocabulary vocab) {
        _vocab = vocab;
    }

    @Override
    public Integer visitLine(BinaryParser.LineContext ctx) {
        if(ctx.variable_expr() != null || ctx.expr().size() > 0 ){
            System.out.print(ctx.expr().size());
            displayInfo(ctx.getText());
        }
        return super.visitLine(ctx);
    }

    /**
     * Counts the additions in a line.
     * @param line The line containing the expression.
     * @return The number of additions
     */
    private int countAdditions(String line) {
        int result = 0;
        for(char c : line.toCharArray()){
            if(c == '+'){
                result++;
            }
        }
        return result;
    }

    /**
     * Gets the largest integer from a list of integers.
     * @param expression The expression we're parsing.
     * @return The highest integer in the list
     */
    private int getLargestNumber(String expression) {
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

    private void displayInfo(String parseLine) {
        System.out.println("\033[1mInput line(s):\033[0m " + parseLine);
        int additions = countAdditions(parseLine);
        System.out.println("\033[1mAddition count:\033[0m " + additions);

        int highestNumber = getLargestNumber(parseLine);
        System.out.println("\033[1mLargest integer is:\033[0m " + highestNumber);
        System.out.println();
    }
}