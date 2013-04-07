package nl2kr.datastructures;

import java.util.HashSet;
import java.util.Set;

import nl2kr.lambdaterm.Variable;

/**
 * The class FreeVariableSet is suppose to store the free variable of a lambda expression
 * in a HashSet, so that we can determine efficiently if a variable belongs to the set or not.
 * 
 * It also keeps the maximum id of any of the variables in the set.  The purpose of keeping
 * the maximum id is so that when for some lambda operation we need to make a substitution with 
 * a variable that is not on the set, we can quickly give an id that is not in the set.  The
 * easiest way to do this is to return maxId+1.
 * 
 * For example, a situation where we need this is part f) of Definition 1.12:
 * f) [N/x](#y.P)=#z.[N/x][z/y]P (if x is in FV(P) and y is in FV(N)).
 * 
 * Here, variable z cannot be one of the free variables of the lambda expression
 * @author jadiel
 *
 */
public class FreeVariableSet {

	Set<Variable> freeVariables;
	int maxId;
	
	public FreeVariableSet(){
		freeVariables=new HashSet<Variable>();
		maxId=0;
	}
	
	public FreeVariableSet(Set<Variable> freeVariables){
		this.freeVariables=new HashSet<Variable>(freeVariables);
		maxId=0;
		for (Variable var : freeVariables){
			int temp=var.getId();
			if (temp > maxId) maxId=temp;
		}
	}
	
	public boolean contains(Variable variable){
		return freeVariables.contains(variable);
	}
	
	public boolean add(Variable variable){
		int temp=variable.getId();
		if (maxId<temp) maxId=temp;
		return freeVariables.add(variable);
	}
	
	public int size(){
		return freeVariables.size();
	}
	
	public boolean addAll(FreeVariableSet anotherSet){
		if (this.freeVariables.addAll(anotherSet.freeVariables)){
			if (this.maxId<anotherSet.maxId) this.maxId=anotherSet.maxId;
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public int newId(){
		return maxId+1;
	}
	
	public boolean equals(Object o){
		if (o instanceof FreeVariableSet){
			FreeVariableSet newO=(FreeVariableSet)o;
			return newO.freeVariables.equals(this.freeVariables);
		}
		return false;
	}
	
	public void clear(){
		freeVariables.clear();
	}
	
	public String toString(){
		return freeVariables.toString();
	}
}
