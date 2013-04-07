package nl2kr.lambdaterm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl2kr.datastructures.FreeVariableSet;

/**
 * Represents part B of the definition of a lambda term, aka: if M and N are lambda terms
 * then (MN) is also a lambda term.
 * @author jadiel
 *
 */
public class BinaryLambdaTerm extends LambdaTerm {

	protected LambdaTerm leftChild;
	protected LambdaTerm rightChild;
	
	//1. CONSTRUCTORS
	public BinaryLambdaTerm(LambdaTerm leftChild, LambdaTerm rightChild){
		super();
		this.leftChild=leftChild;
		this.rightChild=rightChild;
	}
	
	public BinaryLambdaTerm(LambdaTerm parent, LambdaTerm leftChild, LambdaTerm rightChild){
		super(parent);
		this.leftChild=leftChild;
		this.rightChild=rightChild;
	}
	
	//2. SETTERS AND GETTERS
	public void setLeftChild(LambdaTerm leftChild){ this.leftChild=leftChild; }
	public void setRightChild(LambdaTerm rightChild){ this.rightChild=rightChild; }
	public LambdaTerm getLeftChild(){ return leftChild; }
	public LambdaTerm getRightChild(){ return rightChild; }
	
	//3. OPERATIONS
	public boolean equals(Object o){
		if (o instanceof BinaryLambdaTerm){
			if (this.leftChild.equals(((BinaryLambdaTerm)o).leftChild) && this.rightChild.equals(((BinaryLambdaTerm)o).rightChild)){
				return true;
			}
		}
		return false;
	}
	
	public boolean contains(LambdaTerm P){
		if (equals(P)) return true;
		if (leftChild.contains(P) || rightChild.contains(P)) return true;
		return false;
	}
	
	/**
	 * Returns the first appearance of LambdaTerm P in Q (this).
	 */
	public LambdaTerm containedLambdaTerm(LambdaTerm P){
		
		if (this.equals(P)) return this;
		
		else {
			LambdaTerm temp1=leftChild.containedLambdaTerm(P);
			LambdaTerm temp2=rightChild.containedLambdaTerm(P);
		
			if (temp1!=null) return temp1;
			else return temp2;
		}
	}
	
	public List<LambdaTerm> containedLambdaTerms(LambdaTerm P){
		List<LambdaTerm> lambdaTerms=new ArrayList<LambdaTerm>();
		if (this.equals(P)) lambdaTerms.add(this);
		
		List<LambdaTerm> list1=leftChild.containedLambdaTerms(P);
		List<LambdaTerm> list2=rightChild.containedLambdaTerms(P);
		
		lambdaTerms.addAll(list1);
		lambdaTerms.addAll(list2);
		
		return lambdaTerms;
	}

	@Override
	public FreeVariableSet freeVariables(Set<Integer> bindingVariables) {
		
		Set<Integer> bindingVariablesCopy=new HashSet<Integer>(bindingVariables);
		FreeVariableSet set1=leftChild.freeVariables(bindingVariables);
		FreeVariableSet set2=rightChild.freeVariables(bindingVariablesCopy);
		set1.addAll(set2);
		return set1;
	}

	@Override
	public int length() {
		return leftChild.length()+rightChild.length();
	}

	@Override
	/**
	 * Case c) of the definition plus case a)
	 * It is important to note that we need to do a deep copy of N in this method, because
	 * N is spread in two different branches of the tree, so they need to be different objects.
	 */
	public void substitute(LambdaTerm N, Variable x) {
		
		LambdaTerm leftChildN=N.deepCopy();
		LambdaTerm rightChildN=N.deepCopy();
		
		if (leftChild instanceof Variable){
			if (leftChild.equals(x)){
				
				leftChildN.setParent(leftChild.getParent());
				leftChild=leftChildN;
			}
		}
		
		else {
			leftChild.substitute(leftChildN, x);	
		}
		
		if (rightChild instanceof Variable){
			if (rightChild.equals(x)){
				rightChildN.setParent(rightChild.getParent());
				rightChild=rightChildN;
			}
		}
		else {
			rightChild.substitute(rightChildN, x);
		}
	} //end of substitute(N,x)

	@Override
	public LambdaTerm deepCopy() {
		
		LambdaTerm copyLeftChild=leftChild.deepCopy();
		LambdaTerm copyRightChild=rightChild.deepCopy();
		LambdaTerm copyThis=new BinaryLambdaTerm(copyLeftChild, copyRightChild);
		copyLeftChild.setParent(copyThis);
		copyRightChild.setParent(copyThis);
		
		return copyThis;
	}

	@Override
	public void alphaConversion(Variable x, Variable y) {
		leftChild.alphaConversion(x, y);
		rightChild.alphaConversion(x, y);
		
	}

	@Override
	public boolean isCongruent(LambdaTerm P) {
		
		if (P instanceof BinaryLambdaTerm){
			boolean value1=leftChild.isCongruent(((BinaryLambdaTerm)P).leftChild);
			boolean value2=rightChild.isCongruent(((BinaryLambdaTerm)P).rightChild);
			return (value1 && value2);
		}
		else return false;
	}

	@Override
	public void betaReduce() {
		
		rightChild.betaReduce();
		leftChild.betaReduce();
		
		if (leftChild instanceof LambdaLambdaTerm){
			LambdaLambdaTerm newLeftChild=(LambdaLambdaTerm) leftChild;
			
			//TODO: If lambdaTerm is of the type Variable, this means trouble, because it will do nothing
			if (newLeftChild.lambdaTerm instanceof Variable){
				newLeftChild.lambdaTerm=rightChild;
			} else newLeftChild.lambdaTerm.substitute(rightChild, newLeftChild.variable);
			
			LambdaTerm child=newLeftChild.lambdaTerm;
			child.betaReduce();
			LambdaTerm parent=this.getParent();
			
			if (parent instanceof LambdaLambdaTerm){
				child.setParent(parent);
				((LambdaLambdaTerm) parent).setLambdaTerm(child);
			}
			
			//the challenge in this one is to know if I was my parents left or right child.
			//there is an error here that I still don't understand.  How could this not 
			//be the left or the right child?
			else if (parent instanceof BinaryLambdaTerm){
				boolean isLeftChild=true;
				try { 
					isLeftChild=((BinaryLambdaTerm)parent).isLeftChild(this);
				}
				catch (Exception e){
					e.printStackTrace();
					return;
				}
				
				child.setParent(parent);
				if (isLeftChild){
					((BinaryLambdaTerm) parent).setLeftChild(child);
				}
				else ((BinaryLambdaTerm) parent).setRightChild(child);
				
			}
				
			
		}
	}
	
	/**
	 * Returns true if the parameter passed references the same lambdaterm as this.leftChild.
	 * Otherwise if it is the right child, it will return false.  If it is not either, it will
	 * throw an exception.  This function is supposedly to be used exclusively by the beta
	 * reduction operation. 
	 * @param term
	 * @return
	 * @throws Exception 
	 */
	private boolean isLeftChild(LambdaTerm term) throws Exception{
		if (leftChild==term) return true;
		else if (rightChild==term) return false;
		else {
			throw new Exception("Not right or left child");
		}
	}
	
	public String toString(){
		
		StringBuffer sb=new StringBuffer();

		if (leftChild instanceof LambdaLambdaTerm){
			return sb.append('(').append(leftChild.toString()).append(')').append(rightChild.toString()).toString();
		}
		else return sb.append(leftChild.toString()).append(rightChild.toString()).toString();
	}

	

}//end of BinaryLambdaTerm