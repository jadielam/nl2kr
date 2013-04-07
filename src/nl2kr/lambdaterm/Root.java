package nl2kr.lambdaterm;

import java.util.List;
import java.util.Set;

import nl2kr.datastructures.FreeVariableSet;
/**
 * The purpose of this class is to serve as the root of a lambda expression.  
 * The parent of this LambdaTerm will always be null.  
 * I had to implement this class in order to more easily implement methods such as substitute.
 * 
 * TODO: this means that a better design of the system will require me to create a constructor for the
 * different LambdaTerm classes, since the hierarchy is getting more complex.
 * @author jadiel
 *
 */
public class Root extends LambdaTerm {

	private LambdaTerm child;
	
	public Root(){
		super(null);
		this.child=null;
	}
	
	public Root(LambdaTerm child) {
		super(null);
		this.child=child;
	}

	public void setChild(LambdaTerm child){
		this.child=child;
	}
	
	@Override
	public boolean contains(LambdaTerm P) {
		
		if (P instanceof Root) return child.contains(((Root)P).child);
		else return child.contains(P);
	}

	@Override
	public LambdaTerm containedLambdaTerm(LambdaTerm P) {
		if (P instanceof Root) return child.containedLambdaTerm(((Root)P).child);
		else return child.containedLambdaTerm(P);
	}

	@Override
	public List<LambdaTerm> containedLambdaTerms(LambdaTerm P) {
		if (P instanceof Root) return child.containedLambdaTerms(((Root)P).child);
		else return child.containedLambdaTerms(P);
	}

	@Override
	public FreeVariableSet freeVariables(Set<Integer> bindingVariables) {
		return child.freeVariables(bindingVariables);
	}

	@Override
	public int length() {

		return child.length();
	}

	@Override
	/**
	 * Note that this term does all the work here only when the lambda term is of the type x, where x is a single
	 * variable.  Otherwise, it will do all its work on the child.
	 */
	public void substitute(LambdaTerm N, Variable x) {
		
		if (N instanceof Root){
			N=((Root) N).child;
		}
		if (child instanceof Variable){
			
			if (child.equals(x)){
				N.setParent(child.getParent());
				child=N;
			}
		}
		else child.substitute(N,  x);

	}

	/**
	 * This is going to be interesting here.  How do I make all the callings.
	 */
	@Override
	public LambdaTerm deepCopy() {
		
		LambdaTerm copyChild=child.deepCopy();
		LambdaTerm copyRoot=new Root(copyChild);
		copyChild.setParent(copyRoot);
		return copyRoot;
		
	}

	@Override
	public void alphaConversion(Variable x, Variable y) {
		child.alphaConversion(x, y);
	}

	@Override
	public boolean isCongruent(LambdaTerm P) {
		
		if (P instanceof Root){
			return child.isCongruent(((Root)P).child);
		}
		else return false;
		
	}

	
	public void betaReduce() {
		
		child.betaReduce();
	}
	
	
	public String toString(){
		
		return child.toString();
		
	}
	
	public final boolean equals(Object o){
		if (o instanceof Root){
			return this.child.equals(((Root)o).child);
		}
		return false;
	}
}
