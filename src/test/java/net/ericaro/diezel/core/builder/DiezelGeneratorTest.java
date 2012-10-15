package net.ericaro.diezel.core.builder;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import net.ericaro.diezel.Diezel;
import net.ericaro.diezel.DiezelException;

import org.stringtemplate.v4.ST;

public class DiezelGeneratorTest extends TestCase {

	
	public void atestSimple() throws DiezelException, IOException{
		//Diezel.generate(, new File("./target/"));
		File target = new File("./target/generated-sources/test/");
		File src = new File("./src/test/resources/");
		Diezel.generate(target, new File(src, "demo.xml"),new File(src, "demoImpl.xml"));
		//Graph.dot(new File(target, "net/ericaro/diezel/xml/guide-graph").getPath());
	}
	
	public void atestIssue12p() throws DiezelException, IOException{
		//Diezel.generate(, new File("./target/"));
		File target = new File("./target/issue12p/");
		File src = new File("./src/test/resources/");
		//Graph.DEBUG = true;
		Diezel.generate(target, new File(src, "issue12p.xml"));
	}
	public void atestIssue10() throws DiezelException, IOException{
		//Diezel.generate(, new File("./target/"));
		File target = new File("./target/issue10/");
		File src = new File("./src/test/resources/");
		Diezel.generate(target, new File(src, "issue10.xml"));
		// the file is ok, there should be not exeception
	}
	
	public void atestGeneric() throws DiezelException, IOException{
		//Diezel.generate(, new File("./target/"));
		File target = new File("./target/issueGeneric/");
		File src = new File("./src/test/resources/");
		Diezel.generate(target, new File(src, "demoGeneric.xml"));
		// the file is ok, there should be not exeception
	}
	
	
	public void testST(){
		String body = "System.out.println(\"And ... .\");\n" + 
		"				System.out.println(\"Done.\");" ;
		String result = new ST(body, '$', '$').add("transition", "tr").render();
		System.out.println(result);
		assert body.equals(result);
	}
}
