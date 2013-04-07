package nl2kr.main;

import java.io.IOException;


import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import nl2kr.lambdaparser.*;
import nl2kr.lambdaterm.LambdaTerm;
import nl2kr.lambdaterm.Root;

public class LambdaParserMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ANTLRInputStream input = null;
	
		try {
			input=new ANTLRInputStream(System.in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LambdaLexer lexer=new LambdaLexer(input);
		CommonTokenStream tokens=new CommonTokenStream(lexer);
		LambdaParser parser=new LambdaParser(tokens);
		ParseTree tree=parser.r();
		
		ParseTreeWalker treeWalker=new ParseTreeWalker();
		Root term=new Root();
		treeWalker.walk(new LambdaBaseListenerImpl(term), tree);
		System.out.println();
		System.out.println(term.toString());
	}

}
