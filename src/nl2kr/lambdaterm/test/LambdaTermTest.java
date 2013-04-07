package nl2kr.lambdaterm.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;



import nl2kr.datastructures.FreeVariableSet;
import nl2kr.lambdaparser.LambdaBaseListener;
import nl2kr.lambdaparser.LambdaLexer;
import nl2kr.lambdaparser.LambdaParser;
import nl2kr.lambdaterm.LambdaTerm;
import nl2kr.lambdaterm.Root;
import nl2kr.lambdaterm.Variable;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LambdaTermTest {

	
	protected static ArrayList<LambdaTerm> ol;		//original list of terms
	protected static ArrayList<LambdaTerm> el;		//term i of el is supposed to be equal to term i of ol.
	protected static ArrayList<LambdaTerm> dl;		//term i of dl is supposed to be different to term i of ol.
	
	protected static ArrayList<LambdaTerm> containsTrue;		//term i of containsTrue is supposed to be contained in term i of ol.
	protected static ArrayList<LambdaTerm> containsFalse;		//term i of containsTrue is not supposed to be contained in term i of ol.
	
	protected static ArrayList<LambdaTerm> containedLambdaTermTrue;	//it could be the same list as containsTrue
	protected static ArrayList<LambdaTerm> containedLambdaTermFalse;	//it could be the same list as containsFalse
	
	protected static ArrayList<FreeVariableSet> freeVariablesTrue;
	protected static ArrayList<FreeVariableSet> freeVariablesFalse;
	
	protected static ArrayList<Boolean> isClosed;
	protected static ArrayList<Integer> length;
	protected static ArrayList<LambdaTerm> isCongruentTrue;
	protected static ArrayList<LambdaTerm> isCongruentFalse;
	protected static ArrayList<LambdaTerm> substituteTerm;
	protected static ArrayList<Variable> substituteVariable;
	protected static ArrayList<LambdaTerm> substituteTermResult;
	protected static ArrayList<LambdaTerm> betaReduceInput;
	protected static ArrayList<LambdaTerm> betaReduceResult;
	
	private Iterator<LambdaTerm>olIt;
	private Iterator<LambdaTerm>elIt;
	private Iterator<LambdaTerm> dlIt;
	private Iterator<LambdaTerm> containsTrueIt;
	private Iterator<LambdaTerm> containsFalseIt;
	private Iterator<LambdaTerm> containedLambdaTermTrueIt;
	private Iterator<LambdaTerm> containedLambdaTermFalseIt;
	private Iterator<FreeVariableSet> freeVariablesTrueIt;
	private Iterator<FreeVariableSet> freeVariablesFalseIt;
	private Iterator<Boolean> isClosedIt;
	private Iterator<Integer> lengthIt;
	private Iterator<LambdaTerm> isCongruentTrueIt;
	private Iterator<LambdaTerm> isCongruentFalseIt;
	private Iterator<LambdaTerm> substituteTermIt;
	private Iterator<Variable> substituteVariableIt;
	private Iterator<LambdaTerm> substituteTermResultIt;
	private Iterator<LambdaTerm> betaReduceInputIt;
	private Iterator<LambdaTerm> betaReduceResultIt;
	
	@BeforeClass
	public static void initialization(){
		
		File file=new File("testData");
		FileInputStream is=null;
		try {
			is=new FileInputStream(file);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
		ANTLRInputStream input = null;
		
		try {
			input=new ANTLRInputStream(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		testLexer lexer=new testLexer(input);
		CommonTokenStream tokens=new CommonTokenStream(lexer);
		testParser parser=new testParser(tokens);
		ParseTree tree=parser.file();
		
		ParseTreeWalker treeWalker=new ParseTreeWalker();
		testBaseListener listener=new testBaseListener();
		treeWalker.walk(listener, tree);
		
		ol=listener.getOl();
		el=listener.getEl();
		dl=listener.getDl();
		containsTrue=listener.getContainsTrue();
		containsFalse=listener.getContainsFalse();
		containedLambdaTermTrue=listener.getContainedLambdaTermTrue();
		containedLambdaTermFalse=listener.getContainedLambdaTermFalse();
		freeVariablesTrue=listener.getFreeVariablesTrue();
		freeVariablesFalse=listener.getFreeVariablesFalse();
		isClosed=listener.getIsClosed();
		length=listener.getLength();
		isCongruentTrue=listener.getIsCongruentTrue();
		isCongruentFalse=listener.getIsCongruentFalse();
		substituteTerm=listener.getSubstituteInputTerm();
		substituteVariable=listener.getSubstituteInputVariable();
		substituteTermResult=listener.getSubstituteResult();
		betaReduceInput=listener.getBetaReduceInput();
		betaReduceResult=listener.getBetaReduceResult();
	}
	
	@Before
	public void init(){
		olIt=ol.iterator();
		elIt=el.iterator();
		dlIt=dl.iterator();
		containsTrueIt=containsTrue.iterator();
		containsFalseIt=containsFalse.iterator();
		containedLambdaTermTrueIt=containedLambdaTermTrue.iterator();
		containedLambdaTermFalseIt=containedLambdaTermFalse.iterator();
		freeVariablesTrueIt=freeVariablesTrue.iterator();
		freeVariablesFalseIt=freeVariablesFalse.iterator();
		isClosedIt=isClosed.iterator();
		lengthIt=length.iterator();
		isCongruentTrueIt=isCongruentTrue.iterator();
		isCongruentFalseIt=isCongruentFalse.iterator();
		substituteTermIt=substituteTerm.iterator();
		substituteVariableIt=substituteVariable.iterator();
		substituteTermResultIt=substituteTermResult.iterator();
		betaReduceInputIt=betaReduceInput.iterator();
		betaReduceResultIt=betaReduceResult.iterator();
	}
	
	@Test
	public void testEquals() {
	
		while (olIt.hasNext()){
			LambdaTerm term=olIt.next();
			assertTrue(term.equals(elIt.next()));
		}
	}
	
	@Test
	public void testEquals1(){
		while (olIt.hasNext()){
			assertFalse(olIt.next().equals(dlIt.next()));
		}
	}
	
	@Test
	public void testDeepCopy(){
		
		List<LambdaTerm> testList=new ArrayList<LambdaTerm>();
		while (olIt.hasNext()){
			testList.add(olIt.next().deepCopy());
		}
		
		Iterator<LambdaTerm> newOlIt=ol.iterator();
		Iterator<LambdaTerm> testListIt=testList.iterator();
		
		while (newOlIt.hasNext()){
			assertTrue(newOlIt.next().equals(testListIt.next()));
		}
	}
	
	@Test
	public void testContains(){
		
		
		while (olIt.hasNext()){
			LambdaTerm term1=olIt.next();
			LambdaTerm term2=containsTrueIt.next();
			
			assertTrue(term1.contains(term2));
		}
	}
	
	@Test
	public void testCointains1(){
		while (olIt.hasNext()){
			assertFalse(olIt.next().contains(containsFalseIt.next()));
		}
	}
	
	@Test
	public void testContainedLambdaTerm(){
		while (olIt.hasNext()){
			LambdaTerm temp=containedLambdaTermTrueIt.next();
			LambdaTerm temp2=olIt.next();
			LambdaTerm temp3=temp2.containedLambdaTerm(temp);
			
			assertNotNull(temp3);
		}
	}
	
	@Test
	public void testContainedLambdaTerm1(){
		while (olIt.hasNext()){
			assertNull(olIt.next().containedLambdaTerm(containedLambdaTermFalseIt.next()));
		}
	}
	
	@Test
	public void testFreeVariables(){
		
		while (olIt.hasNext()){
			
			
			LambdaTerm next=olIt.next();
			FreeVariableSet set1=next.freeVariables(new HashSet<Integer>());
			FreeVariableSet set2=freeVariablesTrueIt.next();
						
			assertTrue(set1.equals(set2));
			
		}
	}
	
	@Test public void testFreeVariables1(){
		while (olIt.hasNext()){
			assertFalse(olIt.next().freeVariables(new HashSet<Integer>()).equals(freeVariablesFalseIt.next()));
		}
	}
	
	@Test
	public void testIsClosed(){
		while (olIt.hasNext()){
			assertTrue(olIt.next().isClosed()==isClosedIt.next());
		}
	}
	
	@Test
	public void testLength(){
		while (olIt.hasNext()){
			assertTrue(olIt.next().length()==lengthIt.next());
		}
	}
	
	@Test
	public void testIsCongruent(){
		while (olIt.hasNext()){
			LambdaTerm next=olIt.next();
			LambdaTerm next1=isCongruentTrueIt.next();
			assertTrue(next.isCongruent(next1));
		}
	}
	
	@Test
	public void testIsCongruent1(){
		while (olIt.hasNext()){
			assertFalse(olIt.next().isCongruent(isCongruentFalseIt.next()));
		}
	}
	
	@Test
	public void testSubstitute(){
		System.out.println();
		while (olIt.hasNext()){
			LambdaTerm next=olIt.next();
			LambdaTerm result=substituteTermResultIt.next();
			LambdaTerm substituteTerm=substituteTermIt.next();
			Variable variable=substituteVariableIt.next();
			next.substitute(substituteTerm, variable);
					
			assertTrue(next.equals(result));
			assertTrue(next.isCongruent(result));
			assertTrue(result.isCongruent(next));
			
		}
	}
	
	@Test
	public void testBetaReduce(){
		while (betaReduceInputIt.hasNext()){
			LambdaTerm term=betaReduceInputIt.next();
			System.out.println(term);
			term.betaReduce();
			System.out.println(term);
			
			LambdaTerm result=betaReduceResultIt.next();
			System.out.println(result);
			//assertTrue(term.equals(result));
		}
	}
}