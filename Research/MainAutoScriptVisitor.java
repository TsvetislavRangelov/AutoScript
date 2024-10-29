import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Locale;

public class MainAutoScriptVisitor extends AutoScriptBaseVisitor<String> {
    private final SymbolTable symbols = new SymbolTable();

    // TODO: ARRAYS
//    @Override
//    public String visitCollectionAssignment(AutoScriptParser.CollectionAssignmentContext ctx) {
//        String identifier = ctx.ID().getText();
//        if(symbols.containsKey(identifier)) {
//            Symbol prevValue = symbols.lookup(identifier);
//
//        }
//
//    }
    // TODO: Functions
    @Override
    public String visitIfStatement(AutoScriptParser.IfStatementContext ctx){
        SymbolTable localScope = symbols.createScope();
        for (int i=0; i < ctx.condition().size(); i++){
            boolean condition = Boolean.parseBoolean(this.visit(ctx.condition(i)));
            System.out.println("Condition " + ctx.condition(i).getText() + "->" +  condition);
            if (condition){
                for (int m=0; m < ctx.bodyList(i).body().size(); m++){
                    String res = this.visit(ctx.bodyList(i).body(m));
                    System.out.println(res);
                }
                break;
            }
        }
        localScope.free();
        return "";
    }

    @Override 
    public String visitConditionID(AutoScriptParser.ConditionIDContext ctx){
        String result = "";
        String identifier = ctx.ID().getText();
        if(symbols.containsKey(identifier)){
            Symbol symbol = symbols.lookup(identifier);
            result = (String) symbol.getValue();           
        }
        return result;
    }

    @Override
    public String visitParentheses(AutoScriptParser.ParenthesesContext ctx) {
        return this.visit(ctx.inner);
    }

    @Override
    public String visitConditionExpr(AutoScriptParser.ConditionExprContext ctx){
        return this.visit(ctx.singleExpression());
    }

    @Override
    public String visitConditionBoolean(AutoScriptParser.ConditionBooleanContext ctx){
        return ctx.getText();
    }

    @Override
    public String visitConditionAnd(AutoScriptParser.ConditionAndContext ctx){
        boolean left = Boolean.parseBoolean(this.visit(ctx.left));
        boolean right = Boolean.parseBoolean(this.visit(ctx.right));
         System.out.println("Left " +ctx.left.getText() +  "->"+ left);
         System.out.println("Right " + ctx.right.getText() + "->"+ right);
        return String.valueOf(left && right);
    }

    @Override
    public String visitConditionOr(AutoScriptParser.ConditionOrContext ctx){
        boolean left = Boolean.parseBoolean(this.visit(ctx.left));
        boolean right = Boolean.parseBoolean(this.visit(ctx.right));
         System.out.println("Left " +ctx.left.getText() +  "->"+ left);
         System.out.println("Right " + ctx.right.getText() + "->"+ right);
        return String.valueOf(left || right);
    }

    @Override
    public String visitConditionStrictEqual(AutoScriptParser.ConditionStrictEqualContext ctx){
        String left = String.valueOf(this.visit(ctx.left));
        String right = String.valueOf(this.visit(ctx.right));
        System.out.println("Left " + ctx.left.getText() +  "->"+ left);
        System.out.println("Right " + ctx.right.getText() + "->"+ right);
        return String.valueOf(left.equals(right));
    }
    @Override
    public String visitConditionBrackets(AutoScriptParser.ConditionBracketsContext ctx){
        return this.visit(ctx.inner);
    }

    @Override
    public String visitConditionNotEqual(AutoScriptParser.ConditionNotEqualContext ctx){
        String left = String.valueOf(this.visit(ctx.left));
        String right = String.valueOf(this.visit(ctx.right));
         System.out.println("Left " +ctx.left.getText() +  "->"+ left);
         System.out.println("Right " + ctx.right.getText() + "->"+ right);
        return String.valueOf(!left.equals(right));
    }

    @Override
    public String visitTerminal(TerminalNode node) {
       int lineNumber = node.getSymbol().getLine();
        System.out.println("Line: " + lineNumber);
        return "";
    }

    @Override
    public String visitWhileStatement(AutoScriptParser.WhileStatementContext ctx){
        SymbolTable localScope = symbols.createScope();
        while(Boolean.parseBoolean(this.visit(ctx.condition()))){
            System.out.println("in");
            for(int i = 0; i < ctx.body().size(); i++){
                this.visit(ctx.body(i));
            }
        }
        localScope.free();
        return "";
    }


    @Override
    public String visitForStatement(AutoScriptParser.ForStatementContext ctx) {
        SymbolTable localScope = symbols.createScope();
        for(this.visit(ctx.assignmentExpression(0)); Boolean.parseBoolean(this.visit(ctx.condition())); this.visit(ctx.assignmentExpression(1))){
            for(int i = 0; i < ctx.body().size(); i++){
                this.visit(ctx.body(i));
            }
        } 
        localScope.free();
        return "";
    }

    @Override
    public String visitAssignmentExpression(AutoScriptParser.AssignmentExpressionContext ctx){
        return assignVariableToExpression(ctx.ID().getText(), ctx);
    }

    @Override
    public String visitLessThan(AutoScriptParser.LessThanContext ctx){
        return String.valueOf(evalGreaterOrLessThan(null, ctx));
    }

    @Override
    public String visitGreater_Than(AutoScriptParser.Greater_ThanContext ctx){
        return String.valueOf(evalGreaterOrLessThan(ctx, null));
    }

    @Override
    public String visitVariable(AutoScriptParser.VariableContext ctx){
        String result = ctx.getText();
        if(symbols.containsKey(result)){
            Symbol symbol = symbols.lookup(result);
            result = String.valueOf(symbol.getValue());

            //TODO: this is also done at visitString, removing it here, does not change anything
            // if (Objects.requireNonNull(symbol.getType()) == Type.STRING){
                // String val = (String) symbol.getValue();
                // result = val.replace("\"", "");
            // } 
            // else {
            // }
        }
        else {
            throw new
                    ParseCancellationException("Invalid syntax - " + result + " is undefined.");
        }
        return result;
    }

    @Override
    public String visitNumber(AutoScriptParser.NumberContext ctx){
        return ctx.getText();
    }
    @Override
    public String visitString(AutoScriptParser.StringContext ctx){
        return ctx.getText().replace("\"", "");
    }

    @Override
    public String visitBoolean(AutoScriptParser.BooleanContext ctx){
        return ctx.getText();
    }

    @Override
    public String visitCharacter(AutoScriptParser.CharacterContext ctx){
        return ctx.getText().replace("'", "");
    }

    @Override
    public String visitAdditionSubtraction(AutoScriptParser.AdditionSubtractionContext ctx){
        int result;
        try{
            if(ctx.operator.getText().equals("+")){
                result = Integer.parseInt(this.visit(ctx.left)) + Integer.parseInt(this.visit(ctx.right));
            }
            else{
                result = Integer.parseInt(this.visit(ctx.left)) - (Integer.parseInt(this.visit(ctx.right)));
            }
        }
        catch(NumberFormatException e){
            // for when we're dealing with strings
           return this.visit(ctx.left) + this.visit(ctx.right);
        }
        return String.valueOf(result);
    }

    @Override
    public String visitMultiplicationDivision(AutoScriptParser.MultiplicationDivisionContext ctx){
        int result;
        if(ctx.operator.getText().equals("*")){
            result = Integer.parseInt(this.visit(ctx.left)) * Integer.parseInt(this.visit(ctx.right));
        }
        else{
            result = Integer.parseInt(this.visit(ctx.left)) / (Integer.parseInt(this.visit(ctx.right)));
        }
        return String.valueOf(result);
    }

    @Override
    public String visitFunctionCall(AutoScriptParser.FunctionCallContext ctx){
        return "";
    }

    @Override
    public String visitArrowFunction(AutoScriptParser.ArrowFunctionContext ctx){
        SymbolTable localScope = symbols.createScope();
        if(ctx.TYPE() != null && ctx.returnStatement() == null){
            throw new ParseCancellationException("Invalid function. Must return a statement of type " + ctx.TYPE().getText());
        }
        if(ctx.VOID() != null && ctx.returnStatement() != null){
            throw new ParseCancellationException("Invalid function. Function is of type void, can not use return");
        }

        localScope.free();

        return "";
    }

    private String assignVariableToExpression(String varName, AutoScriptParser.AssignmentExpressionContext ctx){
        if(symbols.containsKey(varName)) {
            Symbol symbol = symbols.lookup(varName);
            String parseTreeRes = this.visit(ctx.singleExpression());
            Type varType = symbol.getType();
            // Strings can not be reassigned
            if (varType == Type.BOOLEAN && !parseTreeRes.matches("true|false")) {
                throw new ParseCancellationException("Invalid variable reassignment. Type does not match. Expected: " +
                        String.valueOf(symbol.getType()).toLowerCase(Locale.ROOT));
            }
            else if (varType == Type.INTEGER && !parseTreeRes.matches("[0-9]*")) {
                throw new ParseCancellationException("Invalid variable reassignment. Type does not match. Expected: " +
                        String.valueOf(symbol.getType()).toLowerCase(Locale.ROOT));
            }
            else if (varType == Type.CHARACTER) {
                String formatted = "'" + parseTreeRes + "'";
                if(!formatted.matches("[0-9]*")){
                    throw new ParseCancellationException("Invalid variable reassignment. Type does not match. Expected: " +
                            String.valueOf(symbol.getType()).toLowerCase(Locale.ROOT));
                }
            }
            symbol.setValue(parseTreeRes);
            symbols.update(varName, symbol);
        }
        else{
            String type = ctx.TYPE().getText();
            symbols.insert(varName, new Symbol(
                    symbols.hasParent() ? Scope.LOCAL : Scope.GLOBAL,
                    varName,
                    Type.valueOf(type.toUpperCase(Locale.ROOT)),
                    this.visit(ctx.singleExpression())
            ));
        }
        System.out.println("Assignment:" + varName + "->" + symbols.lookup(varName).getValue());
        return String.valueOf(symbols.lookup(varName).getValue());
    }

    private boolean evalGreaterOrLessThan(AutoScriptParser.Greater_ThanContext ctx1,
                                          AutoScriptParser.LessThanContext ctx2){
        if(ctx1 != null){
            return Integer.parseInt(this.visit(ctx1.left)) > Integer.parseInt(this.visit(ctx1.right));
        }
        return Integer.parseInt(this.visit(ctx2.left)) < Integer.parseInt(this.visit(ctx2.right));
    }
}
