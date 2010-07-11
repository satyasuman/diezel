package net.ericaro.diezel.wildcards;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.List;

import junit.framework.TestCase;
import net.ericaro.diezel.annotations.Transition;
import net.ericaro.diezel.annotations.Workflow;
import net.ericaro.diezel.core.Diezel;
import net.ericaro.diezel.core.DiezelException;

@Workflow("a")
public class WildCardTest<U extends Annotation> extends TestCase {

	
	@Transition public <U extends Annotation> void a(Class<U> annotation){
		System.out.println(annotation);
	}
	
	
	public void testGenerate() throws Exception {
		Diezel.generate(new File("./src/test/java"), WildCardTest.class);
	}
	
	
	public void testGuide() throws Exception {
		
		new WildCardTestGuide(this).a();
		
		
	}
	
	 
}
