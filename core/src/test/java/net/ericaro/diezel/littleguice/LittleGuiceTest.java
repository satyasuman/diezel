package net.ericaro.diezel.littleguice;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;
import net.ericaro.diezel.core.Diezel;
import net.ericaro.diezel.core.DiezelException;

public class LittleGuiceTest extends TestCase {

	
	public void testGenerate() throws Exception {
		Diezel.generate(new File("./src/test/java"), Module.class);
	}
	
	
	public void testGuide() throws Exception {
		ModuleGuide<String,?> m = new ModuleGuide<String,Object>(new Module() );
		m.bind(Number.class).to(Double.class);
		
		
	}
	
	public static void main(String[] args) throws DiezelException {
		Diezel.generate(new File("./src/test/java"), Module.class);
		
	} 
}
