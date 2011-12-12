package net.ericaro.diezel.core.builder;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import net.ericaro.diezel.core.Diezel;
import net.ericaro.diezel.core.DiezelException;
import net.ericaro.diezel.core.parser.Graph;
import net.ericaro.diezel.core.parser.GraphBuilder;

public class DiezelGeneratorTest extends TestCase {

	
	public void testSimple() throws DiezelException, IOException{
		//Diezel.generate(, new File("./target/"));
		File target = new File("./target/generated-sources/test/");
		File src = new File("./src/test/resources/");
		Diezel.generate(target, new File(src, "demo.xml"));
		Graph.dot(new File(target, "net/ericaro/diezel/xml/guide-graph").getPath());
		
	}
	
	public void atestGraphs() throws Exception{
		GraphBuilder.toFile("a&b&c", "target/bang");
		
	}
}
