import java.util.HashMap;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
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

        // we pass the vocab to the visitor.
        MainBinaryVisitor visitor = new MainBinaryVisitor(vocab);
        visitor.visit(start);
    }
}


final class MainBinaryVisitor extends BinaryBaseVisitor<String> {

    private final Vocabulary _vocab;
    private final HashMap<String, String> vars = new HashMap<>();
    MainBinaryVisitor(Vocabulary vocab) {
        _vocab = vocab;
    }

    /**
    * Visits a terminal node in the parse tree.
    * @param node The current node in the tree.
    */
   @Override public String visitTerminal(TerminalNode node) {
       // we use the vocab here to get the symbolic name for the rule number
       String symbolicName = _vocab.getSymbolicName(node.getSymbol().getType());
       int lineNumber = node.getSymbol().getLine();
        System.out.println("Line: " + lineNumber
               + ' '
               + '|' + " Type: " + symbolicName);
        return "";
    }

   @Override
   public String visitAssignment(BinaryParser.AssignmentContext ctx){
    int length = ctx.expr().children.size() - 1;
    String result = "";
    String pt = this.visit(ctx.expr());
    result = pt;

    if(length == 0){
        result = ctx.expr().getText();
    }
    vars.put(ctx.identifier().getText(), result);
    System.out.println("Value of " + 
    ctx.identifier().getText() + " is " + result);
    return result;
   }

    @Override
    public String visitLine(BinaryParser.LineContext ctx) {
        if( ctx.expr().size() > 0 ){
            displayExpressionInfo(ctx.getText());
        }
        else{
            System.out.println("------------------------------------");
        }
        return super.visitLine(ctx);
    }

    @Override 
    public String visitBinary_num(BinaryParser.Binary_numContext ctx){
        String result = ctx.BINARY_NUM().getText(); 
        return result;
    }

    @Override 
    public String visitPrint(BinaryParser.PrintContext ctx){
        if(vars.containsKey(ctx.identifier().getText()) == false){
            throw new ParseCancellationException("Variable " + 
            ctx.identifier().getText() + "is undefined");
        }
        String variable = vars.get(ctx.identifier().getText());
        System.out.println("print(" + ctx.identifier().getText() + ")" + ":   "  + variable);
        return variable;
    }

    @Override
    public String visitVariable(BinaryParser.VariableContext ctx){
        String result = ctx.getText();
        if(vars.containsKey(result)){
            result = vars.get(result);
        }
        else {
            throw new 
            ParseCancellationException("Invalid syntax - " + result + " is undefined.");
        }
        
        return result;
    }

    @Override
    public String visitExclusiveOr(BinaryParser.ExclusiveOrContext ctx){
        int left = Integer.parseInt(this.visit(ctx.left), 2); 
        int right = Integer.parseInt(this.visit(ctx.right),2);
        String result = Integer.toBinaryString(left ^ right);
        printResult(Integer.toBinaryString(left), Integer.toBinaryString(right), result, " XOR ");
        return result;
    }

    @Override
    public String visitBitShift(BinaryParser.BitShiftContext ctx){
        int left = Integer.parseInt(this.visit(ctx.left), 2);
        int right = Integer.parseInt(this.visit(ctx.right), 2); 
        String result = Integer.toBinaryString(left << right); 
        printResult(Integer.toBinaryString(left), Integer.toBinaryString(right), result, " << ");
        return result;
    }

    @Override
    public String visitNegation(BinaryParser.NegationContext ctx) {
        String result = Integer.toBinaryString(
            ~(Integer.parseInt(this.visit(ctx.right), 2)));

        printResult( ctx.right.getText(),"",result, "~ ");
        return result;
    }

    @Override
    public String visitParentheses(BinaryParser.ParenthesesContext ctx){
        int result = Integer.parseInt(this.visit(ctx.inner),2);
        return Integer.toBinaryString(result);
    }

    @Override
    public String visitMultiplicationDivision
    (BinaryParser.MultiplicationDivisionContext ctx){
        String result = "";
       
        if(ctx.operator.getText().equals("*")){
            result = Integer.toBinaryString(
                Integer.parseInt(this.visit(ctx.left),2) * 
                Integer.parseInt(this.visit(ctx.right),2));
            printResult(ctx.left.getText(),ctx.right.getText(),
                result, " * ");
        }else{
            result = Integer.toBinaryString(
                Integer.parseInt(this.visit(ctx.left),2) / 
                Integer.parseInt(this.visit(ctx.right),2));
            printResult(ctx.left.getText(),ctx.right.getText(),
                result, " / ");
        }

        return result;
    }

    @Override
    public String visitAdditionSubtraction 
    (BinaryParser.AdditionSubtractionContext ctx){
        String result = "";
        if(ctx.operator.getText().equals("+")){
            result = Integer.toBinaryString(
                Integer.parseInt(this.visit(ctx.left),2) +
                Integer.parseInt(this.visit(ctx.right),2));
            printResult(ctx.left.getText(),ctx.right.getText(),
                result, " + ");
        }
        else{
            result = Integer.toBinaryString(
                Integer.parseInt(this.visit(ctx.left), 2) - 
                Integer.parseInt(this.visit(ctx.right),2));
            printResult(ctx.left.getText(),ctx.right.getText(),
                result, " - ");

        }
        return result;

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
    private String getLargestNumber(String expression) {
        int highest = 0;
        if(expression.isEmpty()){
            return Integer.toBinaryString(highest);
        }
        // we need only the numbers from within the expression.
        String[] numbers = expression.split("[^0-1]+");
        for(String number : numbers){
            if(!number.isEmpty()){
                int curr = Integer.parseInt(number, 2);
                if(curr > highest){
                    highest = curr;
                }
            }
        }
        return Integer.toBinaryString(highest);
    }

    private void printResult(String leftNum, String rightNum, String result, String opName){
        if(rightNum != ""){        
            System.out.println(leftNum + opName + rightNum + " = " + result);
        }
        else {
            System.out.println(opName + leftNum + "=" + result);
        }
    }

    private void displayExpressionInfo(String parseLine) {
        System.out.println("------------------------------------");
        System.out.println("Processing line: " + parseLine);
        int additions = countAdditions(parseLine);
        System.out.println("Addition count: " + additions);
        String highestNumber = getLargestNumber(parseLine);
        System.out.println("Largest binary number is: " + highestNumber);
        System.out.println("Calculating ...");
    }
}