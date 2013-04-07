package nl2kr.lambdaterm.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class LambdaTermTestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Result result=JUnitCore.runClasses(LambdaTermTest.class);
		for (Failure failure : result.getFailures()){
			System.out.println(failure.toString());
		}

	}

}
