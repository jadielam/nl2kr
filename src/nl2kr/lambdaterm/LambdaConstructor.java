package nl2kr.lambdaterm;

/**
 * This class is to be used by the LambdaBaseListener in order to smartly build
 * lambda expressions.  It should be nice.
 * @author jadiel
 *
 */
public class LambdaConstructor {
	
	LambdaTerm lambdaTerm;
	
	public LambdaConstructor(){
		lambdaTerm=new Root();
	}
	
	public void add(LambdaTerm lambdaTerm){
		
	}
	
	public LambdaTerm getLambdaTerm(){
		return lambdaTerm;
	}
}
