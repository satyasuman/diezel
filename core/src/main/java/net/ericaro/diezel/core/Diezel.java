package net.ericaro.diezel.core;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import net.ericaro.diezel.core.builder.DiezelGenerator;
import net.ericaro.diezel.core.builder.DiezelParser;
import net.ericaro.diezel.core.parser.ParseException;

import org.xml.sax.SAXException;

/**
 * Diezel is a simple tool to generate Intermediary classes to guide programmers using a Complex class
 * based on a workflow.
 * 
 * @author eric
 */
public class Diezel {

	public static void generate(File src, File target) throws DiezelException {
		try {
			DiezelParser p = new DiezelParser();
			DiezelGenerator gen = p.parse(src);
			gen.generate(target);
		} catch (SAXException e) {
			throw new DiezelException("Syntax Error in " + src+": "+ e.getMessage(), e);
		} catch (IOException e) {
			throw new DiezelException("File IO Error while compiling " + src+": "+e.getMessage(), e);
		} catch (ParserConfigurationException e) {
			throw new DiezelException("Unexpected Parser Configuration Error.", e);
		} catch (ParseException e) {
			throw new DiezelException("Syntax Error in Diezel Expression in " + src+": "+e.getMessage(), e);
		}

	}
}
