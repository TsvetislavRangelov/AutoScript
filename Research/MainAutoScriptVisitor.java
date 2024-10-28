import org.antlr.v4.parse.ANTLRParser.blockEntry_return;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Locale;
import java.util.Objects;

public class MainAutoScriptVisitor extends AutoScriptBaseVisitor<String> {
    private final SymbolTable symbols = new SymbolTable();

    @Override
    public String visitIfStatement(AutoScriptParser.IfStatementContext ctx){
        SymbolTable localScope = symbols.createScope();
        for (int i=0; i < ctx.condition().size(); i++){
            boolean condition = Boolean.valueOf(this.visit(ctx.condition(i)));
            if (condition){
                System.out.println(condition);
                String res = this.visit(ctx.body(i));
                System.out.println(res);
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
            if(symbol.getType() == Type.BOOLEAN){
                result = (String) symbol.getValue();
            }
            else{
                throw new ParseCancellationException("Identifier does not resolve to a boolean type" + identifier);                
            }
        }
        return result;
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
        boolean left = Boolean.valueOf(this.visit(ctx.left));
        boolean right = Boolean.valueOf(this.visit(ctx.right));
        return String.valueOf(left && right);
    }

    @Override
    public String visitConditionOr(AutoScriptParser.ConditionOrContext ctx){
        boolean left = Boolean.valueOf(this.visit(ctx.left));
        boolean right = Boolean.valueOf(this.visit(ctx.right));
        return String.valueOf(left || right);
    }

    @Override
    public String visitConditionStrictEqual(AutoScriptParser.ConditionStrictEqualContext ctx){
        boolean left = Boolean.valueOf(this.visit(ctx.left));
        boolean right = Boolean.valueOf(this.visit(ctx.right));
        return String.valueOf(left == right);
    }

    @Override
    public String visitConditionNotEqual(AutoScriptParser.ConditionNotEqualContext ctx){
        boolean left = Boolean.valueOf(this.visit(ctx.left));
        boolean right = Boolean.valueOf(this.visit(ctx.right));
        return String.valueOf(left != right);
    }

    @Override
    public String visitTerminal(TerminalNode node) {
       // we use the vocab here to get the symbolic name for the rule number
       int lineNumber = node.getSymbol().getLine();
        System.out.println("Line: " + lineNumber);
        return "";
    }



    @Override
    public String visitAssignmentExpression(AutoScriptParser.AssignmentExpressionContext ctx){
        String identifier = ctx.ID().getText();
        if(symbols.containsKey(identifier)){
            Symbol symbol = symbols.lookup(identifier);
            //String parseTreeRes = this.visit(ctx.singleExpression());
            // if(symbol.getType() == Type.BOOLEAN){
                // if(parseTreeRes.matches("regex za boolean (true ili false)")){

                // }
                // 
            // }
            // TODO: Do type checking here to make sure variables can't be reassigned to different types
            symbol.setValue(this.visit(ctx.singleExpression()));
            symbols.update(identifier, symbol);
        }
        else{
            String type = ctx.TYPE().getText();
            symbols.insert(identifier, new Symbol(null,
                    identifier,
                    Type.valueOf(type.toUpperCase(Locale.ROOT)),
                    this.visit(ctx.singleExpression())
            ));
        }
        System.out.println("Ass:" +String.valueOf(symbols.lookup(identifier).getValue()));
        return String.valueOf(symbols.lookup(identifier).getValue());
    }

    @Override
    public String visitVariable(AutoScriptParser.VariableContext ctx){
        String result = ctx.getText();
        if(symbols.containsKey(result)){
            Symbol symbol = symbols.lookup(result);
            if (Objects.requireNonNull(symbol.getType()) == Type.STRING) {
                String val = (String) symbol.getValue();
                result = val.replace("\"", "");
            } else {
                result = String.valueOf(symbol.getValue());
            }
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
        return ctx.getText();
    }

    @Override
    public String visitAdditionSubtraction(AutoScriptParser.AdditionSubtractionContext ctx){
        int result = 0;
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
        int result = 0;
        if(ctx.operator.getText().equals("*")){
            result = Integer.parseInt(this.visit(ctx.left)) * Integer.parseInt(this.visit(ctx.right));
        }
        else{
            result = Integer.parseInt(this.visit(ctx.left)) / (Integer.parseInt(this.visit(ctx.right)));
        }
        return String.valueOf(result);
    }

}
