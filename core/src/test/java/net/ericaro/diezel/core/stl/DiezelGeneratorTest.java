package net.ericaro.diezel.core.stl;

import java.io.File;

import net.ericaro.diezel.core.Diezel;
import net.ericaro.diezel.core.DiezelException;
import junit.framework.TestCase;

public class DiezelGeneratorTest extends TestCase {

	
	public void testSimple() throws DiezelException{
		Diezel.generate(new File("./src/test/resources/simple.xml"), new File("./target/"));
	}
}
