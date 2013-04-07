package nl2kr.lambdaterm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl2kr.datastructures.FreeVariableSet;

/**
 * Represents part C of the definition of a lambda term, aka: Represents the lambda 
 * terms of the form #x.M
 * 
 * @author jadiel
 *
 */
public class LambdaLambdaTerm extends LambdaTerm {

	//Let the term be of the form #x.M
	protected Variable variable;  //then variable represents the x
	protected LambdaTerm lambdaTerm;  //then lambdaTerm represents M
	
	
	//1. CONSTRUCTORS
	public LambdaLambdaTerm(LambdaTerm lambdaTerm, Variable variable){
		super();
		this.variable=variable;
		this.lambdaTerm=lambdaTerm;
	}
	
	public LambdaLambdaTerm(LambdaTerm lambdaTerm, Variable variable, LambdaTerm parent){
		super(parent);
		this.variable=variable;
		this.lambdaTerm=lambdaTerm;
	}
	
		
	//2. SETTERS AND GETTERS
	/**
	 * The setter is needed when change of variables are needed to be implemented.
	 * @param variable
	 */
	public void setLambda(Variable variable){	this.variable=variable; }
	public Variable getLambda(){ return variable; }
	
	public void setLambdaTerm(LambdaTerm term) {this.lambdaTerm=term; }
	
		
	//3. OPERATIONS
	/**
	 * Checks if both lambda terms are equal.  Let this=#x.M and o=#y.N
	 * For this and o to be equal, this must be true: x=y and M=N	 * 
	 */
	public boolean equals(Object o){
		if (o instanceof LambdaLambdaTerm){
			Variable oVariable=((LambdaLambdaTerm)o).variable;
			LambdaTerm oLambdaTerm=((LambdaLambdaTerm)o).lambdaTerm;
			
			if (variable.equals(oVariable) && lambdaTerm.equals(oLambdaTerm)) return true;
		}
		return false;
	}
	
	@Override
	/**
	 * This method implements part c) of the definition of contains, aka:
	 * c) If P occurs in M or P=x, then P occurs in #x.M
	 */
	public boolean contains(LambdaTerm P) {
		
		if (this.equals(P)) return true;
		if (variable.equals(P)) return true;
		if (lambdaTerm.contains(P)) return true;
		return false;
	}
	
	public LambdaTerm containedLambdaTerm(LambdaTerm P){
		if (this.equals(P)) return this;
		return lambdaTerm.containedLambdaTerm(P);
	}
	
	public List<LambdaTerm> containedLambdaTerms(LambdaTerm P){
		List<LambdaTerm> lambdaTerms=new ArrayList<LambdaTerm>();
		if (this.equals(P)) lambdaTerms.add(this);
		List<LambdaTerm> list1=lambdaTerm.containedLambdaTerms(P);
		lambdaTerms.addAll(list1);
		return lambdaTerms;
	}

	@Override
	public FreeVariableSet freeVariables(Set<Integer> bindingVariables) {
		bindingVariables.add(variable.getId());
		return lambdaTerm.freeVariables(bindingVariables);
	}

	@Override
	public int length() {
		
		return 1+lambdaTerm.length();
	}

	@Override
	public void substitute(LambdaTerm N, Variable x) {
		
		//0. Case (d)
		if (variable.equals(x)){
			return;
		} 
		//1. Case (a)
		else if (lambdaTerm instanceof Variable){
			if (lambdaTerm.equals(x)){

				FreeVariableSet freeVariablesN=N.freeVariables(new HashSet<Integer>());

				if(freeVariablesN.contains(variable)){
					//implement case (f)
					//finding the smaller id not yet used:
				
					int newId = freeVariablesN.newId();
					
					Variable z=new Variable(newId);
					this.variable=z;
				}
				N.setParent(this);
				lambdaTerm=N;
			}
			
		}
		//2. Cases (f), (e), and (g) 
		else {

			Set<Integer> bindingVariables=new HashSet<Integer>();
			bindingVariables.add(variable.getId());
			FreeVariableSet freeVariablesLambdaTerm=lambdaTerm.freeVariables(bindingVariables);
	
			if (freeVariablesLambdaTerm.contains(x)){
				FreeVariableSet freeVariablesN=N.freeVariables(new HashSet<Integer>());

				if(freeVariablesN.contains(variable)){
					//implement case (f)
					//finding the smaller id not yet used:
				
					int newId = Math.max(freeVariablesLambdaTerm.newId(), freeVariablesN.newId())+1;
					
					Variable z=new Variable(newId);
					lambdaTerm.substitute(z, this.variable);
					this.variable=z;
				}
			
				//2.2 Cases (e) and (g)
				lambdaTerm.substitute(N, x);
			}
		}		
		
	}

	@Override
	public LambdaTerm deepCopy() {
	
		LambdaTerm copyVariable=variable.deepCopy();
		LambdaTerm copyLambdaTerm=lambdaTerm.deepCopy();
		LambdaTerm copyThis=new LambdaLambdaTerm(copyLambdaTerm, (Variable)copyVariable);
		((Variable)copyVariable).setParent(copyThis);
		copyLambdaTerm.setParent(copyThis);
		
		return copyThis;
	}

	@Override
	public void alphaConversion(Variable x, Variable y) {
		//Here is where the fun begins:
		if (variable.equals(x)){
			Set<Integer> bv=new HashSet<Integer>();
			bv.add(this.variable.getId());
			FreeVariableSet fv=lambdaTerm.freeVariables(bv);
			if (!fv.contains(y)){
				Variable newVariable=new Variable(y.getId());
				this.variable=newVariable;
				if (lambdaTerm instanceof Variable){
					substitute(y,x);	
				}
				else lambdaTerm.substitute(y, x);
			}
			
		}
		//We still need this line at the end, since by Definition 1.17, every single
		//occurrence of #x.M needs to be found, not just this one that we found.
		lambdaTerm.alphaConversion(x, y);
	}

	@Override
	public boolean isCongruent(LambdaTerm P) {
		
		if (P instanceof LambdaLambdaTerm){
			LambdaLambdaTerm newP=((LambdaLambdaTerm)P);
			if (this.variable.equals(newP.variable)){
				return this.lambdaTerm.isCongruent(newP.lambdaTerm);
			}
			else {
				newP=(LambdaLambdaTerm)newP.deepCopy();
				newP.alphaConversion((Variable)newP.variable.deepCopy(), (Variable)this.variable.deepCopy());
				return this.lambdaTerm.isCongruent(newP.lambdaTerm);
			}
		}
		else return false;
	}

	@Override
	public void betaReduce() {
		
		lambdaTerm.betaReduce();
	}
	
	public String toString(){
		
		StringBuffer sb=new StringBuffer();
		sb.append('#').append(variable.toString()).append(".").append(lambdaTerm.toString());
		return sb.toString();
	}
}