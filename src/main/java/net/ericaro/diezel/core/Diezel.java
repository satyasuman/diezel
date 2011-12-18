package net.ericaro.diezel.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import net.ericaro.diezel.core.builder.Compilable;
import net.ericaro.diezel.core.builder.DiezelBuilder;
import net.ericaro.diezel.core.builder.DiezelCompiler;
import net.ericaro.diezel.core.builder.DiezelImplementation;
import net.ericaro.diezel.core.builder.DiezelImplementationBuilder;
import net.ericaro.diezel.core.builder.DiezelLanguage;
import net.ericaro.diezel.core.builder.DiezelLanguageBuilder;
import net.ericaro.diezel.core.parser.DiezelParser;
import net.ericaro.diezel.core.parser.ParseException;

import org.xml.sax.SAXException;

/**
 * Diezel is a simple tool to generate Intermediary classes to guide programmers using a Complex class
 * based on a workflow.
 * 
 * @author eric
 */
public class Diezel {

	public static void generate(File target, File... sources) throws DiezelException {
		try {
			Map<String, DiezelLanguage> languages = new HashMap<String, DiezelLanguage>();
			List<DiezelImplementationBuilder> others = new ArrayList<DiezelImplementationBuilder>();
			for(File src : sources){
				DiezelBuilder<? extends Compilable> gen = DiezelParser.parse(src);
				// quicky build lang first
				if (gen instanceof DiezelLanguageBuilder){
					DiezelLanguageBuilder db = (DiezelLanguageBuilder) gen;
					String name = db.getQualifiedName();
					languages.put(name, db.build());
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
				DiezelCompiler.generate(lang, target);
			// build and compile impl
			for(DiezelImplementationBuilder gen: others)
				DiezelCompiler.generate(gen.build(), target);
		} catch (SAXException e) {
			throw new DiezelException("Syntax Error: "+ e.getMessage(), e);
		} catch (IOException e) {
			throw new DiezelException("File IO Error: "+e.getMessage(), e);
		} catch (ParserConfigurationException e) {
			throw new DiezelException("Unexpected Parser Configuration Error.", e);
		} catch (ParseException e) {
			throw new DiezelException("Diezel Expression Syntax Error: "+e.getMessage(), e);
		}

	}
}
