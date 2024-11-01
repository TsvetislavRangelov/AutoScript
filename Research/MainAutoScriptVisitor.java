import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Locale;

public class MainAutoScriptVisitor extends AutoScriptBaseVisitor<String> {
    private final SymbolTable symbols = new SymbolTable();

    @Override
    public String visitPrint(AutoScriptParser.PrintContext ctx) {
        if(ctx.singleExpression() != null){
            Utils.Log(this.visit(ctx.singleExpression()));
        }
        else{
            Utils.Log(this.visit(ctx.collectionIndex()));
        }
        return "";
    }

    @Override
    public String visitCollectionAssignment(AutoScriptParser.CollectionAssignmentContext ctx) {
        return assignVariableToCollection(ctx.ID().getText(), ctx);
    }

    @Override
    public String visitCollectionIndexAssignment(AutoScriptParser.CollectionIndexAssignmentContext ctx) {
        if(ctx.collectionIndex().size() > 1){
            int indexLeft = Integer.parseInt(this.visit(ctx.collectionIndex(0).singleExpression()));
            String identifierLeft = ctx.collectionIndex(0).ID().getText();
            String identifierRight = ctx.collectionIndex(1).ID().getText();
            if(symbols.containsKey(identifierLeft) && symbols.containsKey(identifierRight)){
                Symbol leftArr = symbols.lookup(identifierLeft);
                Symbol rightArr = symbols.lookup(identifierRight);
                if(leftArr.getType().equals(Type.INTEGER_ARRAY) && rightArr.getType().equals(Type.INTEGER_ARRAY)){
                    int val = Integer.parseInt(this.visit(ctx.collectionIndex(1).singleExpression()));
                    int[] intArray = (int[]) leftArr.getValue();
                    intArray[indexLeft] = val;
                }
                else if(leftArr.getType().equals(Type.STRING_ARRAY) && rightArr.getType().equals(Type.STRING_ARRAY)){
                    String val = String.valueOf(this.visit(ctx.collectionIndex(1).singleExpression()));
                    String[] stringArray = (String[]) leftArr.getValue();
                    stringArray[indexLeft] = val;
                }
                else if(leftArr.getType().equals(Type.CHARACTER_ARRAY) && rightArr.getType().equals(Type.CHARACTER_ARRAY)){
                    char val = ctx.collectionIndex(1).singleExpression().getText().charAt(0);
                    char[] charArray = (char[]) leftArr.getValue();
                    charArray[indexLeft] = val;
                }
                else if(leftArr.getType().equals(Type.BOOLEAN_ARRAY) && rightArr.getType().equals(Type.BOOLEAN_ARRAY)){
                    boolean val = Boolean.parseBoolean(this.visit(ctx.collectionIndex(1).singleExpression()));
                    boolean[] intArray = (boolean[]) leftArr.getValue();
                    intArray[indexLeft] = val;
                }
            }
        }
        else{
            String identifier = ctx.collectionIndex(0).ID().getText();
            int index = Integer.parseInt(this.visit(ctx.collectionIndex(0).singleExpression()));
            if(symbols.containsKey(identifier)) {
                Symbol arrayData = symbols.lookup(identifier);
                if(arrayData.getType().equals(Type.INTEGER_ARRAY)){
                    int val = Integer.parseInt(this.visit(ctx.singleExpression()));
                    int[] intArray = (int[]) arrayData.getValue();
                    intArray[index] = val;
                }
                else if(arrayData.getType().equals(Type.STRING_ARRAY)){
                    String val = this.visit(ctx.singleExpression());
                    String[] strArray = (String[]) arrayData.getValue();
                    strArray[index] = val;
                }
                else if(arrayData.getType().equals(Type.BOOLEAN_ARRAY)){
                    boolean val = Boolean.parseBoolean(this.visit(ctx.singleExpression()));
                    boolean[] strArray = (boolean[]) arrayData.getValue();
                    strArray[index] = val;
                }
                else if(arrayData.getType().equals(Type.CHARACTER_ARRAY)){
                    char val = ctx.singleExpression().getText().charAt(0);
                    char[] strArray = (char[]) arrayData.getValue();
                    strArray[index] = val;
                }
            }
        }
        return "";
    }

    @Override
    public String visitCollectionIndex(AutoScriptParser.CollectionIndexContext ctx) {
        String identifier = ctx.ID().getText();
        int index = Integer.parseInt(this.visit(ctx.singleExpression()));
        if(symbols.containsKey(identifier)) {
            Symbol arrayData = symbols.lookup(identifier);
            if(arrayData.getType().equals(Type.INTEGER_ARRAY)){
                int[] intArray = (int[])arrayData.getValue();
                return String.valueOf(intArray[index]);
            }
            if(arrayData.getType().equals(Type.CHARACTER_ARRAY)){
                char[] charArray = (char[])arrayData.getValue();
                return String.valueOf(charArray[index]);
            }
            if(arrayData.getType().equals(Type.BOOLEAN_ARRAY)){
                boolean[] booleanArray = (boolean[])arrayData.getValue();
                return String.valueOf(booleanArray[index]);
            }
            if(arrayData.getType().equals(Type.STRING_ARRAY)){
                String[] stringArray = (String[])arrayData.getValue();
                return String.valueOf(stringArray[index]);
            }
        }
        return null;
    }

    @Override
    public String visitIfStatement(AutoScriptParser.IfStatementContext ctx){
        symbols.createScope();
        for (int i=0; i < ctx.condition().size(); i++){
            boolean condition = Boolean.parseBoolean(this.visit(ctx.condition(i)));
            if (condition){
                for (int m=0; m < ctx.bodyList(i).body().size(); m++){
                    String res = this.visit(ctx.bodyList(i).body(m));
                }
                break;
            }
        }
        symbols.free();
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
        return String.valueOf(left && right);
    }

    @Override
    public String visitConditionOr(AutoScriptParser.ConditionOrContext ctx){
        boolean left = Boolean.parseBoolean(this.visit(ctx.left));
        boolean right = Boolean.parseBoolean(this.visit(ctx.right));
        return String.valueOf(left || right);
    }

    @Override
    public String visitConditionStrictEqual(AutoScriptParser.ConditionStrictEqualContext ctx){
        String left = String.valueOf(this.visit(ctx.left));
        String right = String.valueOf(this.visit(ctx.right));
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
        return String.valueOf(!left.equals(right));
    }

    @Override
    public String visitTerminal(TerminalNode node) {
       int lineNumber = node.getSymbol().getLine();
        return "";
    }

    @Override
    public String visitWhileStatement(AutoScriptParser.WhileStatementContext ctx){
        symbols.createScope();
        while(Boolean.parseBoolean(this.visit(ctx.condition()))){
            for(int i = 0; i < ctx.body().size(); i++){
                this.visit(ctx.body(i));
            }
        }
        symbols.free();
        return "";
    }


    @Override
    public String visitForStatement(AutoScriptParser.ForStatementContext ctx) {
        symbols.createScope();
        for(this.visit(ctx.assignmentExpression(0)); Boolean.parseBoolean(this.visit(ctx.condition())); this.visit(ctx.assignmentExpression(1))){
            for(int i = 0; i < ctx.body().size(); i++){
                this.visit(ctx.body(i));
            }
        } 
        symbols.free();
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
    public String visitReturnStatement(AutoScriptParser.ReturnStatementContext ctx) { 
        String res = ""; 
        if (ctx.singleExpression() != null){
            res = String.valueOf(this.visit(ctx.singleExpression()));
        }else if (ctx.NULL() != null){
            res = "null";
        }
        return res;
    }

    @Override
    public String visitVariable(AutoScriptParser.VariableContext ctx){
        String result = ctx.getText();
        if(symbols.containsKey(result)){
            Symbol symbol = symbols.lookup(result);
            result = String.valueOf(symbol.getValue());
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
        String id = ctx.ID().getText();
        if(!symbols.containsKey(id)) {
            throw new ParseCancellationException("Function is not declared");
        }
        Symbol symbol = symbols.lookup(id);
        
        return this.visitFunctionBody((AutoScriptParser.FunctionBodyContext)symbol.getValue());
    }

    @Override
    public String visitArrowFunction(AutoScriptParser.ArrowFunctionContext ctx){
        String id = ctx.functionDeclaration().ID().getText();
        if(ctx.functionDeclaration().TYPE() != null && ctx.functionBody().returnStatement() == null){
            throw new ParseCancellationException("Function must return a statement");
        }
    
        if(ctx.functionDeclaration().VOID() != null && ctx.functionBody().returnStatement() != null){
            throw new ParseCancellationException("Function is void, can not return statement");
        }

        if(symbols.containsKey(id)) {
            throw new ParseCancellationException("Function with the same name already exist");
        }
        String type = Type.FUNCTION.toString();
        symbols.insert(id, new Symbol(
            symbols.hasParent() ? Scope.LOCAL : Scope.GLOBAL,
            id,
            Type.valueOf(type.toUpperCase(Locale.ROOT)),
            ctx.functionBody()
        ));
        return "";
    }

    @Override
    public String visitFunctionBody(AutoScriptParser.FunctionBodyContext ctx){
        String res = "";
        symbols.createScope();
        for(int i = 0; i< ctx.bodyList().size(); i++){
            this.visit(ctx.bodyList(i));
        }
        if(ctx.returnStatement() != null){
            res = this.visitReturnStatement(ctx.returnStatement());
        }
        symbols.free();
        return res;
    }

    private String assignVariableToCollection(String varName,
                                              AutoScriptParser.CollectionAssignmentContext ctx){
        if(symbols.containsKey(varName)){
            throw new ParseCancellationException("Array " + varName + "is immutable.");
        }

        int arraySize = Integer.parseInt(this.visit(ctx.singleExpression()));
        Type arrayType = Type.stringToTypedArray(ctx.TYPE().getText());
        if(arrayType == null){
            throw new ParseCancellationException("Missing type for array " + varName);
        }

        if(arrayType.equals(Type.INTEGER_ARRAY)){
            symbols.insert(varName, new Symbol(Scope.GLOBAL, varName,
                    arrayType,
                    new int[arraySize]));
        }
        else if(arrayType.equals(Type.STRING_ARRAY)){
            symbols.insert(varName, new Symbol(Scope.GLOBAL, varName,
                    arrayType,
                    new String[arraySize]));
        }
        else if(arrayType.equals(Type.BOOLEAN_ARRAY)){
            symbols.insert(varName, new Symbol(Scope.GLOBAL, varName,
                    arrayType,
                    new boolean[arraySize]));
        }
        else if(arrayType.equals(Type.CHARACTER_ARRAY)){
            symbols.insert(varName, new Symbol(Scope.GLOBAL, varName,
                    arrayType,
                    new char[arraySize]));
        }
        return "";
    }

    private String assignVariableToExpression(String varName,
                                              AutoScriptParser.AssignmentExpressionContext ctx){
            if(symbols.containsKey(varName) && ctx.TYPE() == null) {
            Symbol symbol = symbols.lookup(varName);
            String parseTreeRes;
            if(ctx.singleExpression() != null){
                parseTreeRes = this.visit(ctx.singleExpression());
            }else if (ctx.functionCall() != null){
                parseTreeRes = this.visit(ctx.functionCall());
            }
            else{
                parseTreeRes = this.visit(ctx.collectionIndex());
            }
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
            else if (varType == Type.STRING){
                throw new ParseCancellationException("Strings are immutable. Variable can not be reassigned " + varName );
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
                    this.visit(ctx.singleExpression()!= null ? ctx.singleExpression() :
                            ctx.functionCall() != null ? ctx.functionCall() : ctx.collectionIndex())
            ));
        }
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
