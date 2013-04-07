package nl2kr.lambdaterm.test;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import nl2kr.datastructures.FreeVariableSet;
import nl2kr.lambdaterm.*;
import nl2kr.lambdaterm.test.testParser.BetaReduceInputContext;
import nl2kr.lambdaterm.test.testParser.BetaReduceResultContext;
import nl2kr.lambdaterm.test.testParser.ClosedContext;
import nl2kr.lambdaterm.test.testParser.FreeVariablesFalseContext;
import nl2kr.lambdaterm.test.testParser.FreeVariablesTrueContext;
import nl2kr.lambdaterm.test.testParser.IsClosedContext;
import nl2kr.lambdaterm.test.testParser.IsCongruentFalseContext;
import nl2kr.lambdaterm.test.testParser.IsCongruentTrueContext;
import nl2kr.lambdaterm.test.testParser.LengthContext;
import nl2kr.lambdaterm.test.testParser.LengthNumberContext;
import nl2kr.lambdaterm.test.testParser.SubstituteInputContext;
import nl2kr.lambdaterm.test.testParser.SubstituteInputTermContext;
import nl2kr.lambdaterm.test.testParser.SubstituteResultContext;
import nl2kr.lambdaterm.test.testParser.VariableItem1Context;
import nl2kr.lambdaterm.test.testParser.VariableItemContext;
import nl2kr.lambdaterm.test.testParser.VariableListContext;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.ErrorNode;

public class testBaseListener implements testListener {
	
	private Stack<LambdaTerm> lStack=new Stack<LambdaTerm>();
	private List<LambdaTerm> temporaryList=new ArrayList<LambdaTerm>();
	private List<FreeVariableSet> temporaryList1=new ArrayList<FreeVariableSet>();  //for freeVariables
	private Set<Variable> temporarySet=new HashSet<Variable>();
	private List<Variable> temporaryList2=new ArrayList<Variable>();
	
	private ArrayList<LambdaTerm> ol=new ArrayList<LambdaTerm>();
	private ArrayList<LambdaTerm> el=new ArrayList<LambdaTerm>();
	private ArrayList<LambdaTerm> dl=new ArrayList<LambdaTerm>();
	private ArrayList<LambdaTerm> containsTrue=new ArrayList<LambdaTerm>();
	private ArrayList<LambdaTerm> containsFalse=new ArrayList<LambdaTerm>();
	private ArrayList<LambdaTerm> containedLambdaTermTrue=new ArrayList<LambdaTerm>();
	private ArrayList<LambdaTerm> containedLambdaTermFalse=new ArrayList<LambdaTerm>();
	private ArrayList<FreeVariableSet> freeVariablesTrue=new ArrayList<FreeVariableSet>();
	private ArrayList<FreeVariableSet> freeVariablesFalse=new ArrayList<FreeVariableSet>();
	private ArrayList<Boolean> isClosed=new ArrayList<Boolean>();
	private ArrayList<Integer> length=new ArrayList<Integer>();
	private ArrayList<LambdaTerm> isCongruentTrue=new ArrayList<LambdaTerm>();
	private ArrayList<LambdaTerm> isCongruentFalse=new ArrayList<LambdaTerm>();
	private ArrayList<LambdaTerm> substituteInputTerm=new ArrayList<LambdaTerm>();
	private ArrayList<Variable> substituteInputVariable=new ArrayList<Variable>();
	private ArrayList<LambdaTerm> substituteResult=new ArrayList<LambdaTerm>();
	private ArrayList<LambdaTerm> betaReduceInput=new ArrayList<LambdaTerm>();
	private ArrayList<LambdaTerm> betaReduceResult=new ArrayList<LambdaTerm>();
	
	public ArrayList<LambdaTerm> getOl(){ return ol; }
	public ArrayList<LambdaTerm> getEl(){ return el; }
	public ArrayList<LambdaTerm> getDl(){ return dl; }
	public ArrayList<LambdaTerm> getContainsTrue(){ return containsTrue; }
	public ArrayList<LambdaTerm> getContainsFalse(){ return containsFalse; }
	public ArrayList<LambdaTerm> getContainedLambdaTermTrue(){ return containedLambdaTermTrue; }
	public ArrayList<LambdaTerm> getContainedLambdaTermFalse(){ return containedLambdaTermFalse; }
	public ArrayList<FreeVariableSet> getFreeVariablesTrue(){return freeVariablesTrue; }
	public ArrayList<FreeVariableSet> getFreeVariablesFalse(){return freeVariablesFalse; }
	public ArrayList<Boolean> getIsClosed(){ return isClosed; }
	public ArrayList<Integer> getLength(){ return length; }
	public ArrayList<LambdaTerm> getIsCongruentTrue(){ return isCongruentTrue; }
	public ArrayList<LambdaTerm> getIsCongruentFalse(){ return isCongruentFalse; }
	public ArrayList<LambdaTerm> getSubstituteInputTerm(){ return substituteInputTerm; }
	public ArrayList<Variable> getSubstituteInputVariable(){ return substituteInputVariable; }
	public ArrayList<LambdaTerm> getSubstituteResult(){ return substituteResult; }
	public ArrayList<LambdaTerm> getBetaReduceInput(){ return betaReduceInput; }
	public ArrayList<LambdaTerm> getBetaReduceResult(){ return betaReduceResult; }
		
	@Override public void enterConstant(testParser.ConstantContext ctx) { }
	@Override public void exitConstant(testParser.ConstantContext ctx) { 
		
	}

	@Override public void enterContainedLambdaTermTrue(testParser.ContainedLambdaTermTrueContext ctx) { }
	
	@Override 
	public void exitContainedLambdaTermTrue(testParser.ContainedLambdaTermTrueContext ctx) { 
		containedLambdaTermTrue.addAll(temporaryList);
		temporaryList.clear();
	}

	@Override public void enterLambdaTerm(testParser.LambdaTermContext ctx) { }
	@Override public void exitLambdaTerm(testParser.LambdaTermContext ctx) { }

	@Override public void enterContainsTrue(testParser.ContainsTrueContext ctx) { }
	
	@Override 
	public void exitContainsTrue(testParser.ContainsTrueContext ctx) { 
		containsTrue.addAll(temporaryList);
		temporaryList.clear();
	}

	@Override public void enterContainsFalse(testParser.ContainsFalseContext ctx) { }
	
	@Override public void exitContainsFalse(testParser.ContainsFalseContext ctx) { 
		containsFalse.addAll(temporaryList);
		temporaryList.clear();
	}

	@Override public void enterBinaryLambdaTerm(testParser.BinaryLambdaTermContext ctx) { }
	
	@Override 
	public void exitBinaryLambdaTerm(testParser.BinaryLambdaTermContext ctx) { 
		//This distinction is needed, because there is a rule of the form
		// binaryLambdaTerm : '(' binaryLambdaTerm ')' that does not need to be taken
		//into account.
		if (ctx.getChildCount()!=3){
			LambdaTerm rightChild=lStack.pop();
			LambdaTerm leftChild=lStack.pop();
			BinaryLambdaTerm term=new BinaryLambdaTerm(leftChild, rightChild);
			leftChild.setParent(term);
			rightChild.setParent(term);
			lStack.push(term);
		}
	}

	@Override public void enterLambdaLambdaTerm(testParser.LambdaLambdaTermContext ctx) { }
	@Override public void exitLambdaLambdaTerm(testParser.LambdaLambdaTermContext ctx) { 
		//This distinction is needed, because there is a rule of the form
		// lambdaLambdaTerm : '(' lambdaLambdaTerm ')' that does not need to be taken
		//into account.
		if (ctx.getChildCount()!=3){
			LambdaTerm childTerm=lStack.pop();
			Variable var=(Variable)lStack.pop();
			LambdaLambdaTerm term=new LambdaLambdaTerm(childTerm, var);	
			lStack.push(term);
		}
		
	}

	@Override public void enterEl(testParser.ElContext ctx) { }
	@Override public void exitEl(testParser.ElContext ctx) { 
		el.addAll(temporaryList);
		temporaryList.clear();
	}

	@Override public void enterTerm(testParser.TermContext ctx) { }
	@Override public void exitTerm(testParser.TermContext ctx) {
		
	}

	@Override public void enterR(testParser.RContext ctx) { }
	@Override public void exitR(testParser.RContext ctx) { 
		LambdaTerm child=lStack.pop();
		Root term=new Root();
		term.setChild(child);
		temporaryList.add(term);
	}

	@Override public void enterFile(testParser.FileContext ctx) { }
	@Override public void exitFile(testParser.FileContext ctx) { 
		
	}

	@Override public void enterDl(testParser.DlContext ctx) { }
	@Override public void exitDl(testParser.DlContext ctx) { 
		dl.addAll(temporaryList);
		temporaryList.clear();
	}

	@Override public void enterOl(testParser.OlContext ctx) { }
	@Override public void exitOl(testParser.OlContext ctx) { 
		ol.addAll(temporaryList);
		temporaryList.clear();
	}

	@Override public void enterLambdaVariable(testParser.LambdaVariableContext ctx) { }
	@Override public void exitLambdaVariable(testParser.LambdaVariableContext ctx) { 
		int id;
		String variableText=ctx.getText();
		if (variableText.equals("u")) id=0;
		else if (variableText.equals("v")) id=1;
		else if (variableText.equals("w")) id=2;
		else if (variableText.equals("x")) id=3;
		else if (variableText.equals("y")) id=4;
		else if (variableText.equals("z")) id=5;
		else {
			id=Integer.parseInt(variableText.substring(1));
		}
		
		LambdaTerm var=new Variable(id);
		lStack.push(var);
	}

	@Override public void enterVariable(testParser.VariableContext ctx) { }
	@Override public void exitVariable(testParser.VariableContext ctx) {
		int id;
		String variableText;
		if (ctx.getChildCount()!=3) variableText=ctx.getText();
		else variableText=ctx.getChild(1).getText(); 
		
		if (variableText.equals("u")) id=0;
		else if (variableText.equals("v")) id=1;
		else if (variableText.equals("w")) id=2;
		else if (variableText.equals("x")) id=3;
		else if (variableText.equals("y")) id=4;
		else if (variableText.equals("z")) id=5;
		else {
			id=Integer.parseInt(variableText.substring(1));
		}
				
		LambdaTerm var=new Variable(id);
		lStack.push(var);
	}

	@Override public void enterContainedLambdaTermFalse(testParser.ContainedLambdaTermFalseContext ctx) { }
	@Override public void exitContainedLambdaTermFalse(testParser.ContainedLambdaTermFalseContext ctx) { 
		containedLambdaTermFalse.addAll(temporaryList);
		temporaryList.clear();
	}

	@Override public void enterEveryRule(ParserRuleContext ctx) { }
	@Override public void exitEveryRule(ParserRuleContext ctx) { }
	@Override public void visitTerminal(TerminalNode node) { }
	@Override public void visitErrorNode(ErrorNode node) { }
	@Override
	
	public void enterFreeVariablesTrue(FreeVariablesTrueContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exitFreeVariablesTrue(FreeVariablesTrueContext ctx) {
		
		freeVariablesTrue.addAll(temporaryList1);
		temporaryList1.clear();
		
	}
	
	@Override
	public void enterVariableList(VariableListContext ctx) {
		
		
	}
	@Override
	public void exitVariableList(VariableListContext ctx) {
		
		temporaryList1.add(new FreeVariableSet(temporarySet));
		temporarySet.clear();
		
	}
	@Override
	public void enterFreeVariablesFalse(FreeVariablesFalseContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exitFreeVariablesFalse(FreeVariablesFalseContext ctx) {
		
		freeVariablesFalse.addAll(temporaryList1);
		temporaryList1.clear();
		
	}

	@Override
	public void enterVariableItem(VariableItemContext ctx) {
				
	}
	@Override
	public void exitVariableItem(VariableItemContext ctx) {
		String variableText=ctx.getText();
		Variable var=createVariable(variableText);
		temporarySet.add(var);
	}
	
	private Variable createVariable(String variableText){
		int id;
		
		if (variableText.equals("u")) id=0;
		else if (variableText.equals("v")) id=1;
		else if (variableText.equals("w")) id=2;
		else if (variableText.equals("x")) id=3;
		else if (variableText.equals("y")) id=4;
		else if (variableText.equals("z")) id=5;
		else {
			id=Integer.parseInt(variableText.substring(1));
		}
				
		return new Variable(id);
	}
	@Override
	public void enterClosed(ClosedContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exitClosed(ClosedContext ctx) {
		String text=ctx.getChild(0).getText();
		if (text.equals("true")) isClosed.add(true);
		else isClosed.add(false);
		
	}
	@Override
	public void enterIsClosed(IsClosedContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exitIsClosed(IsClosedContext ctx) {
		// 
		
	}
	@Override
	public void enterLengthNumber(LengthNumberContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exitLengthNumber(LengthNumberContext ctx) {
		String number=ctx.getChild(0).getText();
		length.add(Integer.parseInt(number));
		
	}
	@Override
	public void enterLength(LengthContext ctx) {
		
		
	}
	@Override
	public void exitLength(LengthContext ctx) {
				
	}
	@Override
	public void enterSubstituteInputTerm(SubstituteInputTermContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exitSubstituteInputTerm(SubstituteInputTermContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void enterSubstituteInput(SubstituteInputContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exitSubstituteInput(SubstituteInputContext ctx) {
		substituteInputTerm.addAll(temporaryList);
		temporaryList.clear();
		
	}
	@Override
	public void enterSubstituteResult(SubstituteResultContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exitSubstituteResult(SubstituteResultContext ctx) {
		substituteResult.addAll(temporaryList);
		temporaryList.clear();
	}
	
	@Override
	public void enterIsCongruentTrue(IsCongruentTrueContext ctx) {
		
	}
	
	@Override
	public void exitIsCongruentTrue(IsCongruentTrueContext ctx) {
		isCongruentTrue.addAll(temporaryList);
		temporaryList.clear();
		
	}
	@Override
	public void enterIsCongruentFalse(IsCongruentFalseContext ctx) {
		
		
	}
	@Override
	public void exitIsCongruentFalse(IsCongruentFalseContext ctx) {
		isCongruentFalse.addAll(temporaryList);
		temporaryList.clear();
		
	}
	@Override
	public void enterVariableItem1(VariableItem1Context ctx) {
		
		
	}
	@Override
	public void exitVariableItem1(VariableItem1Context ctx) {
		String variableText=ctx.getText();
		Variable var=createVariable(variableText);
		substituteInputVariable.add(var);
		
	}
	@Override
	public void enterBetaReduceResult(BetaReduceResultContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exitBetaReduceResult(BetaReduceResultContext ctx) {
		betaReduceResult.addAll(temporaryList);
		temporaryList.clear();
		
	}
	@Override
	public void enterBetaReduceInput(BetaReduceInputContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exitBetaReduceInput(BetaReduceInputContext ctx) {
		betaReduceInput.addAll(temporaryList);
		temporaryList.clear();
		
	}

}