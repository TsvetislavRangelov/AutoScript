import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.Locale;
import java.util.Objects;

public class MainAutoScriptVisitor extends AutoScriptBaseVisitor<String> {
    private final SymbolTable symbols = new SymbolTable();

    @Override
    public String visitAssignmentExpression(AutoScriptParser.AssignmentExpressionContext ctx){
        String identifier = ctx.ID().getText();
        if(symbols.containsKey(identifier)){
            Symbol symbol = symbols.lookup(identifier);
            // TODO: Do type checking here to make sure variables can't be reassigned to different types
            symbol.setValue(this.visit(ctx.singleExpression()));
            symbols.update(identifier, symbol);
        }
        else{
            String type = ctx.TYPE().getText();
            symbols.insert(identifier, new Symbol(
                    identifier,
                    Type.valueOf(type.toUpperCase(Locale.ROOT)),
                    this.visit(ctx.singleExpression())
            ));
        }
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
