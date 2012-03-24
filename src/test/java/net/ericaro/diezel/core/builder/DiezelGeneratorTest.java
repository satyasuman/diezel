package net.ericaro.diezel.core.builder;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import net.ericaro.diezel.core.Diezel;
import net.ericaro.diezel.core.DiezelException;
import net.ericaro.diezel.core.exceptions.UndefinedTransitionException;
import net.ericaro.diezel.core.parser.Graph;
import net.ericaro.diezel.core.parser.GraphBuilder;

import org.stringtemplate.v4.ST;

public class DiezelGeneratorTest extends TestCase {

	
	public void testSimple() throws DiezelException, IOException{
		//Diezel.generate(, new File("./target/"));
		File target = new File("./target/generated-sources/test/");
		File src = new File("./src/test/resources/");
		Diezel.generate(target, new File(src, "demo.xml"),new File(src, "demoImpl.xml"));
		Graph.dot(new File(target, "net/ericaro/diezel/xml/guide-graph").getPath());
		
	}
	
	
	public void testIssue8() throws DiezelException, IOException{
		//Diezel.generate(, new File("./target/"));
		File target = new File("./target/issue8/");
		File src = new File("./src/test/resources/");
		try {
			Diezel.generate(target, new File(src, "issue8.xml"));
		} catch (UndefinedTransitionException e) {
			return;
		}
		assert false: "undefined transition exception was expected !" ;
		
		
	}
	
	public void testIssue12p() throws DiezelException, IOException{
		//Diezel.generate(, new File("./target/"));
		File target = new File("./target/issue12p/");
		File src = new File("./src/test/resources/");
		//Graph.DEBUG = true;
		Diezel.generate(target, new File(src, "issue12p.xml"));
		Graph.dot(new File(target, "org/apache/s4/core/guide-graph").getPath());
		
		
	}
	public void testIssue10() throws DiezelException, IOException{
		//Diezel.generate(, new File("./target/"));
		File target = new File("./target/issue10/");
		File src = new File("./src/test/resources/");
		Diezel.generate(target, new File(src, "issue10.xml"));
		// the file is ok, there should be not exeception
	}
	
	public void testGeneric() throws DiezelException, IOException{
		//Diezel.generate(, new File("./target/"));
		File target = new File("./target/issueGeneric/");
		File src = new File("./src/test/resources/");
		Diezel.generate(target, new File(src, "demoGeneric.xml"));
		// the file is ok, there should be not exeception
	}
	
	public void atestGraphs() throws Exception{
		GraphBuilder.toFile("a&b&c", "target/bang");
		
	}
	
	
	public void testST(){
		String body = "System.out.println(\"And ... .\");\n" + 
		"				System.out.println(\"Done.\");" ;
		String result = new ST(body, '$', '$').add("transition", "tr").render();
		System.out.println(result);
		assert body.equals(result);
	}
}
