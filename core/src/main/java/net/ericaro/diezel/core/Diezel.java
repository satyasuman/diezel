package net.ericaro.diezel.core;

import java.io.File;
import java.io.IOException;

import net.ericaro.diezel.core.builder.Builder;
import net.ericaro.diezel.core.parser.ParseException;





/** Diezel is a simple tool to generate Intermediary classes to guide programmers using a Complex class
 * based on a workflow.
 * 
 * @author eric
 *
 */
public class Diezel {

	
	public static void generate(File dir, Class... classes) throws DiezelException{
		for (Class c: classes)
			try {
				Builder.generate(c, dir);
			} catch (ParseException e) {
				throw new DiezelException("Error while parsing "+c+" workfow: \n"+ e.getMessage(), e);
			} catch (IOException e) {
				throw new DiezelException("Error while generating workflow for "+c+" into "+dir+" due to "+e.getMessage(), e);
			}
	}
}
