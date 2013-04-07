package nl2kr.lambdaterm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl2kr.datastructures.FreeVariableSet;

/**
 * Represents the Lambda Terms that are single variables.
 * A variable is differentiated from other variable by the id.
 * Represents part A of the definition of Lambda Term.
 * @author jadiel
 *
 */
public class Variable extends LambdaTerm {
	
	private final int id;
	
	public Variable(){
		super();
		id=0;
	}
	
	public Variable(int id){
		super();
		this.id=id;
	}
	
	public Variable(int id, LambdaTerm parent){
		super(parent);
		this.id=id;
	}
	
	public int getId(){ return id;}
	
	
	public final boolean equals(Object o){
		if (o instanceof Variable)
			if ((((Variable)o).id)==this.id) return true;
		return false;
	}
	
	public boolean contains(LambdaTerm P){
		return equals(P);
	}
	
	public final LambdaTerm containedLambdaTerm(LambdaTerm P){
		if (this.equals(P)) return this;
		return null;
	}
	
	public final List<LambdaTerm> containedLambdaTerms(LambdaTerm P){
		List<LambdaTerm> lambdaTerms=new ArrayList<LambdaTerm>();
		if (this.equals(P)) lambdaTerms.add(this);
		return lambdaTerms;
	}

	@Override
	public FreeVariableSet freeVariables(Set<Integer> bindingVariables) {
		
		FreeVariableSet toReturn=new FreeVariableSet();
		if (!bindingVariables.contains(id)) toReturn.add(this);
		return toReturn;
	}

	@Override
	public int length() {
		return 1;
	}

	@Override
	/**
	 * This method is supposed to do nothing, hence, it is good as it is.
	 * Case a) of the definition, but it is handled by the Root class object of the lambda term.
	 */
	public void substitute(LambdaTerm N, Variable x) {
		return;		
	}

	@Override
	public LambdaTerm deepCopy() {
		
		return new Variable(this.getId());
	}

	@Override
	public void alphaConversion(Variable x, Variable y) {
		//This is not a stub.  This is how it is supposed to be.
		
	}

	@Override
	public boolean isCongruent(LambdaTerm P) {
		
		
		if (P instanceof Variable){
			return this.equals(P);
		}
		else return false;
	}

	@Override
	public void betaReduce() {
		//It is supposed to be like this.	
	}

	public String toString(){
				
		switch (id){
			case 0:	return "u";
			case 1: return "v";
			case 2: return "w";
			case 3: return "x";
			case 4: return "y";
			case 5: return "z";
		}
		
		return 'x'+Integer.toString(id);
	}
	
	@Override
	public int hashCode(){
		return id;
	}

}