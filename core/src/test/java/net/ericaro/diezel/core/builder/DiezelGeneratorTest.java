package net.ericaro.diezel.core.builder;

import java.io.File;
import java.io.IOException;

import net.ericaro.diezel.core.Diezel;
import net.ericaro.diezel.core.DiezelException;
import net.ericaro.diezel.core.parser.Graph;
import junit.framework.TestCase;

public class DiezelGeneratorTest extends TestCase {

	
	public void testSimple() throws DiezelException, IOException{
		Diezel.generate(new File("./src/test/resources/simple.xml"), new File("./target/"));
		Graph.dot("./target/net/ericaro/diezel/xml/guide-graph");
		
	}
}
