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
	
	public void testSimple() throws Exception {
		
		String s = Builder.parse(Simple.class).toString();
		System.out.println(s);
		String gold = "dsl: t | b\n" + 
				"package: net.ericaro.diezel.core.builder\n" + 
				"type: net.ericaro.diezel.core.builder.BuilderParserTest.Simple<T, V>\n" + 
				"transition:\n" + 
				"	name: t1\n" + 
				"	alias: t\n" + 
				"	throws: java.lang.Exception\n" + 
				"	args: java.lang.String\n" + 
				"\n" + 
				"";
		System.out.println(gold);
		assertEquals(gold, s);
		
		
	}
	
	public void testGen() throws Exception {
		Builder.generate(BuilderGen.class,new File("./target/test-generated-sources/javacc/"));
	}
}
