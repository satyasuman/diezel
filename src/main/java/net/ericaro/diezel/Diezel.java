package net.ericaro.diezel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import net.ericaro.diezel.builder.DiezelCompiler;
import net.ericaro.diezel.builder.FileUtil;
import net.ericaro.diezel.builder.impl.DiezelImplementationBuilder;
import net.ericaro.diezel.builder.lang.DiezelLanguage;
import net.ericaro.diezel.builder.lang.DiezelLanguageBuilder;

/**
 * Diezel is a simple tool to generate Intermediary classes to guide programmers using a Complex class
 * based on a workflow.
 * 
 * @author eric
 */
public class Diezel {

	/** Generates into a target directory a set of diezel files (including languages and their implementations)
	 * 
	 * @param targetDir
	 * @param sources
	 * @throws DiezelException
	 */
	public static void generate(File targetDir, File... sources) throws DiezelException {
		try {
			Map<String, DiezelLanguage> languages = new HashMap<String, DiezelLanguage>();
			List<DiezelImplementationBuilder> others = new ArrayList<DiezelImplementationBuilder>();
			for(File src : sources){
				
				Object gen;
				try {
					gen = parse(src);
				} catch (Exception e) {
					throw new DiezelException("Diezel-Error: Wrong XML file format in "+src+"\nCaused by\n"+e.getMessage(), e);
				}
				
				// quicky build lang first
				if (gen instanceof DiezelLanguageBuilder){
					DiezelLanguageBuilder db = (DiezelLanguageBuilder) gen;
					String name = db.getQualifiedName();
					
					try {
						languages.put(name, db.build());
					} catch (Exception e) {
						throw new DiezelException("Diezel-Error: XML Compilation Error in "+name+"\nCaused by\n"+e.getMessage(), e);
					}
					
				}
				else
					others.add((DiezelImplementationBuilder) gen);
			}
						
			// link others 
			for(DiezelImplementationBuilder gen: others){
				DiezelLanguage lang = languages.get(gen.getImplements() );
				gen.setLanguage(lang);
			}
			// compile lang
			for(DiezelLanguage lang: languages.values())
				DiezelCompiler.generate(lang, targetDir);
			// build and compile impl
			for(DiezelImplementationBuilder gen: others)
				DiezelCompiler.generate(gen.build(), targetDir);
		} catch (DiezelException e) {
			throw e;
		} catch (IOException e) {
			throw new DiezelException("File IO Error: "+e.getMessage(), e);
		} catch (Exception e) {
			throw new DiezelException("Diezel Expression Syntax Error: "+e.getMessage(), e);
		}

	}
	
	
	private static Object parse(File source) throws XMLStreamException, FactoryConfigurationError, JAXBException, IOException, DiezelException {
		Object src = FileUtil.read(source);
		if (src instanceof net.ericaro.diezel._2_0.Diezel)
			return new DiezelLanguageBuilder((net.ericaro.diezel._2_0.Diezel)src);
		else if (src instanceof net.ericaro.diezel._2_0.DiezelImplementation)
			return new DiezelImplementationBuilder((net.ericaro.diezel._2_0.DiezelImplementation)src);
		throw new DiezelException("Unknown XML content in "+ source);
	}
}
