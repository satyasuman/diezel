package net.ericaro.diezel.core.builder;

import java.io.File;

import junit.framework.TestCase;
import net.ericaro.diezel.annotations.Transition;
import net.ericaro.diezel.annotations.Workflow;

public class BuilderParserTest extends TestCase {

	@Workflow("t | b")
	public static class Simple<T,V>{
		
		@Transition("t")
		public void t1(String s) throws Exception{
			System.out.println("t = "+s);
		}
	}
	
	@Workflow(value="t | b")
	public static class BuilderGen<T,V>{
		
		@Transition("t")
		public void t1(String s) throws Exception{
			System.out.println("t = "+s);
		}
		@Transition("b")
		public void t2(String s) throws Exception{
			System.out.println("t2 = "+s);
		}
	}
	
	public void testGen() throws Exception {
		// generation fail cause exeception
		Builder.generate(BuilderGen.class,new File("./target/test-generated-sources/javacc/"));
	}
}
