package nl2kr.lambdaterm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl2kr.datastructures.FreeVariableSet;

/**
 * This class is used to represent a lambda expression.  
 * We follow Definition 1.1 (lambda term) from Hindley and Seldin (2008).
 * In place of the Greek character lambda, we use # in this documentation.
 * Also, we will refer to this LambdaTerm by Q, and to other LambdaTerm by P.
 * 
 * The DEFINITION (1.1) is as follows:
 * 
 * Assume an infinite sequence of variables and a sequence (empty, finite or infinite)
 * of constants.  The set of expressions called lambda terms (l-terms) is defined inductively 
 * as follows:
 * a) All variables and atomic constants are lambda terms.
 * b) If M and N are l-terms, then (MN) is a l-term (called application).
 * c) If M is a l-term and x is any variable, then #x.M is a l-term (called abstraction).
 * 
 * @author jadiel
 *
 */
public abstract class LambdaTerm {

	private LambdaTerm parent;
	
		
	//1. CONSTRUCTORS
	public LambdaTerm(){  parent=null; }
	public LambdaTerm(LambdaTerm parent){ this.parent=parent; }
	
	//2. GETTERS AND SETTERS
	protected LambdaTerm getParent() { return parent;  }
	public void setParent(LambdaTerm parent){ this.parent=parent;  }
		
	//3. OPERATIONS
	/**
	 * This method returns a deep copy of itself.
	 * @param toCopy
	 * @return
	 */
	public abstract LambdaTerm deepCopy();
	
	/**
	 * This method implements Definition 1.7 (occurrence) from Hindley and Seldin (2008). 
	 * The DEFINITION is as follows:  For l-terms P and Q, the relation P occurs is defined as:
	 * a) P occurs in P
	 * b) If P occurs in M or in N, then P occurs in MN
	 * c) If P occurs in M or P=x, then P occurs in #x.M
	 * 
	 * IMPLEMENTATION
	 * a) Easy way: Check if it is equal to this term.  If not, recursively check
	 * if it is contained in one of the children.
	 * b) Hard way: No need to go there.
	 * @param P is a lambda expression.  We check if this is equal to P or contains P
	 * @return It returns true if P is contained in this lambda expression, false otherwise.
	 */
	public abstract boolean contains(LambdaTerm P);
	
	/**
	 * Checks if term P is contained in Q(this), and returns a reference to the FIRST term
	 * equal to P in Q.  
	 * 
	 * It explores the tree in a left-then right manner.
	 * TODO: A more intuitive way to do it is to return the term closest to the root that is 
	 * equal to P.  In order to do this we would need to implement a sort of BFS algorithm.
	 * Well, it is actually not impossible.  I will try to do it when I get to Binary LambdaTerm
	 * 
	 * @param P is the term to be checked if it is contained
	 * @return Returns null if P is not contained in Q, otherwise returns a reference.
	 */
	public abstract LambdaTerm containedLambdaTerm(LambdaTerm P);
	
	
	/**
	 * Equal to method containedLambdaTerm(LambdaTerm P), but instead of returning a reference
	 * to one term
	 * @param P
	 * @return
	 */
	public abstract List<LambdaTerm> containedLambdaTerms(LambdaTerm P);
	
	/**
	 * DEFINITION 1.11 from Hindley and Seldin (2008):
	 * An occurrence of a variable x in a term P is called:
	 * a) Bound: if it is in the scope of a #x in P.
	 * b) Bound and binding: iff it is the x in #x.
	 * c) Free otherwise.
	 * 
	 * FV(P)=the set of free variables of P.
	 * 
	 * EXAMPLE: Let P=xvu(#x.x(#v.uv)).  FV(P)={x, v, u} (interesting).  Note that
	 * variable x belongs to the free variables because its first appearance is as a free
	 * variable.
	 * 
	 * IMPLEMENTATION:
	 * As I explore the tree, add binding variables to an initially empty set of binding variables.  If I find a variable
	 * not in the set, then I add it to the set of free variables.  
	 * 
	 * @return The Set returned contains an (integer)? list of the free variable of
	 * the current LambdaTerm.
	 * @param bindingVariables. It contains the id's of the variables that are binding on this lambda term,
	 * because this lambda term is in their scope.  THE INITIAL CALL SHOULD PASS AS PARAMETER AN **EMPTY** SET.
	 * TODO: I am not sure yet if the return value should be Set<Variable> or Set<Integer>
	 */
	public abstract FreeVariableSet freeVariables(Set<Integer> bindingVariables);
	
	/**
	 * DEFINITION: A term P is closed if FV(P)=empty.
	 * 
	 * IMPLEMENTATION: Call the function freeVariables().  If the size of the returned 
	 * set is zero, return true; otherwise return false.
	 * 
	 * @return True if it is closed, False otherwise.
	 */
	public boolean isClosed(){
		FreeVariableSet temp=freeVariables(new HashSet<Integer>());
		if (temp.size()==0) return true;
		return false;
	}
	
	/**
	 * DEFINITION 1.6: The length of a term M is the total number of occurrences of atoms in M.  In more detail:
	 * a) length(a)=1 for a being a constant or a variable.
	 * b) length(MN)=length(M)+length(N).
	 * c) length(#x.M)=1+length(M).
	 * @return
	 */
	public abstract int length();
	
	/**
	 * DEFINITION 1.12: For any M, N, x, define [N/x]M to be the result of substituting every free occurrence of variable x in M by N, 
	 * and changing bound variables to avoid clashes.  The precise definition is as follows:
	 * a) [N/x]x=N.
	 * b) [N/x]a=a.
	 * c) [N/x](PQ)=([N/x]P [N/x]Q).
	 * d) [N/x](#x.P)=#x.P.			
	 * e) [N/x](#y.P)=#y.P (if x is not in FV(P)).  
	 * f) [N/x](#y.P)=#z.[N/x][z/y]P (if x is in FV(P) and y is in FV(N)).
	 * g) [N/x](#y.P)=#y.[N/x]P (if x is in FV(P)).
	 * IMPORTANT NOTE REGARDING THE DEFINITION: Parts (e) and (g) can be combined into one.  There is no need to check
	 * if x is in FV(P) or not, since (d), (a) and (b) would take care of that case down the road.
	 * 
	 * Note that in this definition f) is g) in the textbook; and g) is f) in the textbook.  This needed
	 * to be so just in case we are implementing this using an if-else statement.
	 * 
	 * IMPLEMENTATION: The implementation of this operation is not trivial.  It has some challenges that I still
	 * need to think about.  Essentially, differently to other methods, the substitute method will have 
	 * to work from the parent of the current LambdaTerm in order to make the substitutions, because
	 * an object cannot substitute itself from inside itself by using the "this" pointer.
	 * 
	 * @param N is a lambda term that will take the place of variable x
	 * @param x is the variable that will be substituted.
	 * 
	 */
	public abstract void substitute(LambdaTerm N, Variable x);
	
	/**
	 * DEFINITION 1.17: Let a term P contain an occurrence of #x.M, and let y not be in FV(M).  The act of replacing this
	 * #x.M by #y.[y/x]M is called change of bound variable or alpha conversion in P.
	 * @param x is the old variable
	 * @param y is the new variable
	 */
	public abstract void alphaConversion(Variable x, Variable y);
	
	/**
	 * DEFINITION 1.17, second part: If P can be changed to Q by a finite, (perhaps empty) series of bound variables,
	 * we shall say that P is congruent to Q, or that P alpha-converts to Q.
	 * 
	 * IMPLEMENTATION: An idea on how to implement it:
	 * 1.1 If this.freeVariables().equals(P.freeVariables)
	 * 		1.1.1 If (P instanceof this)
	 *			1.1.1.1 If it is a variable, check that they are equal.
	 *			1.1.1.2 If it is a constant, check that they are equal.
	 *			1.1.1.3 If it is a BinaryLambdaTerm, call isCongruent on each of the branches.  If one of the two
	 *					is false, return false
	 *			1.1.1.4 If it is a LambdaLambdaTerm, 
	 *			1.1.1.4.1   If the variables are equal, call isCongruent on the child.
	 *			1.1.1.4.2   Else, change the variable in P to the variable in Q by using alphaConversion.  And then
	 *						call isCongruent on the children of Q with P as parameter. 
	 * 		1.1.2 Else return false
	 * 1.2 Else, return false.
	 * TODO: Review that this logic works.
	 * @param P
	 */
	public abstract boolean isCongruent(LambdaTerm P);
	
	/**
	 * DEFINITION 1.24: Any term of the form (#x.M)N is called a beta-redex and the corresponding term [N/x]M is
	 * called its contractum.  Iff a term P contains an occurrence of (#x.M)N and we replace that occurrence by [N/x]M,
	 * and the result is P', we say that we have contracted the redex-occurrence in P, and P beta-contracts to P'
	 * 
	 * WARNING: Be aware that the call to this method may not end if the lambda expression is not a safe expression!!!!
	 */
	public abstract void betaReduce();
}
