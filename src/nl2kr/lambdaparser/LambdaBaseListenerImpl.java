package nl2kr.lambdaparser;

import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import nl2kr.lambdaterm.Constant;
import nl2kr.lambdaterm.LambdaTerm;
import nl2kr.lambdaterm.Root;

public class LambdaBaseListenerImpl extends LambdaBaseListener {

	Root term;
	
	/**
	 * The idea behind doing this that way is that we will have levels. 
	 * 1-Each level corresponds to a list on the stack.  
	 * 2-At no point in time should a list have more than two elements.
	 * 3-At the beginning of processing a LambdaTerm we are in the outer level.
	 * 4-Whenever we encounter a LambdaLambdaTerm we create a new level (aka, we push a new List to the stack).
	 * 5-Whenever we finish with a LambdaLambdaTerm, we remove a level (aka, we pop a level from the stack).
	 * 6-Whenever we finish processing any lambda term, we need to create a BinaryLambdaTerm, that is, we 
	 *   need to check if that element has an element to the left on the list, and then we combine them both
	 *   into a new term which would be a BinaryLambdaTerm and that would take place of those two elements
	 *   on the list.
	 * 7-STILL MISSING: Pointers that will let me know to which element on the outer lever do they correspond.
	 * 8-STILL MISSING: How to handle parentheses. i.e.: a(bc).  
	 */
	
	/**
	 * I think that I have it down now.
	 */
	Stack<List<LambdaTerm>> tree;
	
	public LambdaBaseListenerImpl(Root term) {
		super();
		tree=new Stack<List<LambdaTerm>>();
	}

	@Override public void enterCp(LambdaParser.CpContext ctx) { }
	@Override public void exitCp(LambdaParser.CpContext ctx) { }

	@Override public void enterOp(LambdaParser.OpContext ctx) { }
	@Override public void exitOp(LambdaParser.OpContext ctx) { }

	@Override public void enterR(LambdaParser.RContext ctx) { }
	@Override public void exitR(LambdaParser.RContext ctx) { }

	@Override public void enterLambdaTerm(LambdaParser.LambdaTermContext ctx) { }
	@Override public void exitLambdaTerm(LambdaParser.LambdaTermContext ctx) { 
		
		String text=ctx.getText();
		//Whenever we open the parenthesis, the term needs to be pushed to the stack, even if incomplete.
		if (text.charAt(0)=='('){
			
			//a. If it is LambdaLambdaTerm
			if (ctx.getChildCount()==6){
				
			}
			//b. If it is BinaryLambdaTerm
			else if (ctx.getChildCount()==4){
				
			}
			else{
			
				//c. If it is Constant
				
				//d. If it is Variable
			}
		}
		else {
			
			//a. If it is LambdaLambdaTerm
			if (ctx.getChildCount()==4){
				
			}
			//b. If it is BinaryLambdaTerm
			else if (ctx.getChildCount()==2){
				
			}
			else{
			
				//c. If it is Constant
				
				//d. If it is Variable
			}
			
		}
		
	}

	@Override public void enterEveryRule(ParserRuleContext ctx) { }
	@Override public void exitEveryRule(ParserRuleContext ctx) { }
	@Override public void visitTerminal(TerminalNode node) { }
	@Override public void visitErrorNode(ErrorNode node) { }
	
}
