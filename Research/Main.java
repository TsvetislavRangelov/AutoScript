
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;


public class Main
{
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromStream(System.in);
        AutoScriptLexer lexer = new AutoScriptLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AutoScriptParser parser = new AutoScriptParser(tokens);

        ParseTree start = parser.entry();
        MainAutoScriptVisitor visitor = new MainAutoScriptVisitor();
        visitor.visit(start);
    }
}


