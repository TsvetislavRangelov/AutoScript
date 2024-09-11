// Generated from Binary.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link BinaryParser}.
 */
public interface BinaryListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link BinaryParser#line}.
	 * @param ctx the parse tree
	 */
	void enterLine(BinaryParser.LineContext ctx);
	/**
	 * Exit a parse tree produced by {@link BinaryParser#line}.
	 * @param ctx the parse tree
	 */
	void exitLine(BinaryParser.LineContext ctx);
	/**
	 * Enter a parse tree produced by {@link BinaryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(BinaryParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link BinaryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(BinaryParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link BinaryParser#binary_op}.
	 * @param ctx the parse tree
	 */
	void enterBinary_op(BinaryParser.Binary_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link BinaryParser#binary_op}.
	 * @param ctx the parse tree
	 */
	void exitBinary_op(BinaryParser.Binary_opContext ctx);
}