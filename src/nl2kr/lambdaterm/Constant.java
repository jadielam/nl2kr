package nl2kr.lambdaterm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nl2kr.datastructures.FreeVariableSet;

/**
 * Represents the lambda terms that are single constants
 * A constant is differentiated from other constant by the id.
 * Represents part A of the definition of Lambda Term.
 * @author jadiel
 *
 */
public class Constant extends LambdaTerm {

	private int id;
	
	public Constant(){
		super();
		id=0;
	}
	
	public Constant(int id){
		super();
		this.id=id;
	}
	
	public Constant(int id, LambdaTerm parent){
		super(parent);
		this.id=id;
	}
	
	public int getId(){ return id;}
	
	public boolean equals(Object o){
		if (o instanceof Constant)
			if (((Constant)o).id==this.id) return true;
		return false;
	}
	
	public boolean contains(LambdaTerm P){
		return equals(P);
	}
	
	public LambdaTerm containedLambdaTerm(LambdaTerm P){
		if (this.equals(P)) return this;
		return null;
	}
	
	public List<LambdaTerm> containedLambdaTerms(LambdaTerm P){
		List<LambdaTerm> lambdaTerms=new ArrayList<LambdaTerm>();
		if (this.equals(P)) lambdaTerms.add(this);
		return lambdaTerms;
	}

	@Override
	public FreeVariableSet freeVariables(Set<Integer> bindingVariables) {
		
		return new FreeVariableSet();
	}

	@Override
	public int length() {
		return 1;
	}

	@Override
	/**
	 * This method is supposed to do nothing.  Hence, it is good as it is.
	 * Case b) of the definition.
	 */
	public void substitute(LambdaTerm N, Variable x) {
		return;
	}

	@Override
	public LambdaTerm deepCopy() {
		
		return new Constant(this.getId());
	}

	@Override
	public void alphaConversion(Variable x, Variable y) {
		// This is not a method stub.  This is how it is supposed to be.
		
	}

	@Override
	public boolean isCongruent(LambdaTerm P) {
		
		if (P instanceof Constant){
			if (this.equals(P)) return true;
			else return false;
		}
		else return false;
				
	}

	@Override
	public void betaReduce() {
		//It is supposed to be like this.
		
	}

	public String toString(){
		
		switch (id){
			case 0: return "a";
			case 1: return "b";
			case 2: return "c";
			case 3: return "d";
			case 4: return "e";
			case 5: return "f";
		}
		
		return 'c'+Integer.toString(id);
	}
	
}