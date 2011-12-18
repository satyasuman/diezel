package net.ericaro.diezel.core.parser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;


import net.ericaro.diezel.core.builder.Compilable;
import net.ericaro.diezel.core.builder.DiezelBuilder;
import net.ericaro.diezel.core.builder.DiezelImplementationBuilder;
import net.ericaro.diezel.core.builder.DiezelLanguageBuilder;
import net.ericaro.diezel.core.builder.Generic;
import net.ericaro.diezel.core.builder.Transition;
import net.ericaro.diezel.core.builder.TransitionImplementation;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses an XML diezel file, and build a DiezelLanguageBuilder class.
 * 
 * @author eric
 */
public class DiezelParser extends DefaultHandler {

	public static final String XMLNS = "http://diezel.ericaro.net/2.0.0/";

	public static final String ATT_NAME = "name";
	public static final String ATT_PATH = "path";
	public static final String ATT_EXTENDS = "extends";
	public static final String ATT_SUPER = "super";

	// simple text element are read using this enum
	static Map<String, Elements> names = new HashMap<String, Elements>();

	enum Elements {
		DIEZEL("diezel"), 
		TRANSITION("transition"), 
		TRANSITIONS("transitions"), 
		CAPTURE("capture"), 
		DROP("drop"), 
		PACKAGE("package"), 
		HEADER("header"), 
		EXPRESSION("expression"), 
		JAVADOC("javadoc"), 
		RETURN("return"), 
		SIGNATURE("signature"), 
		NAME("name"), 
		EXTENDS("extends"), 
		STATES("states"), 
		STATE("state"),
		DIEZEL_IMPLEMENTATION("diezelImplementation"),
		IMPLEMENTS("implements"),
		TRANSITION_IMPLEMENTATION("transitionImplementation"),
		BODY("body"),
		;
		String name;

		Elements(String name) {
			this.name = name;
			names.put(name, this);
		}

		static Elements byName(String name) {
			return names.get(name);
		}

	}

	private DiezelLanguageBuilder gen;
	private DiezelImplementationBuilder genImpl;
	DiezelBuilder builder;
	private String currentPath ;
	private Transition transition;
	private TransitionImplementation transitionImpl;
	
	private Elements currentElement;


	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		assert uri == XMLNS : "illegal elementn outside the xmlns";

		currentElement = Elements.byName(localName);
		assert currentElement !=null : "found and unknown element "+localName;
		switch (currentElement) {
		case DIEZEL:
			builder = gen = new DiezelLanguageBuilder();
			break;
		case DIEZEL_IMPLEMENTATION:
			builder = genImpl = new DiezelImplementationBuilder();
			break;
			
		case CAPTURE:
			Generic generic = readGeneric(attributes);
			if (transition == null)
				gen.addRootType(generic);
			else
				transition.addPush(generic);
			break;
		case DROP:
			generic = readGeneric(attributes);
			transition.addPull(generic);
			break;
		case TRANSITION:
			transition = new Transition(attributes.getValue(ATT_NAME));
			gen.addTransition(transition);
			break;
		case STATE:
			currentPath = attributes.getValue(ATT_PATH);
			break;
			
		case TRANSITION_IMPLEMENTATION:
			transitionImpl = new TransitionImplementation(attributes.getValue(ATT_NAME));
			genImpl.addTransitionImplementation(transitionImpl);
			break;
		}
	}

	/**
	 * read generic attributes
	 * 
	 * @param attributes
	 * @return
	 */
	private Generic readGeneric(Attributes attributes) {
		String name = attributes.getValue(ATT_NAME);
		String ext = attributes.getValue(ATT_EXTENDS);
		String sup = attributes.getValue(ATT_SUPER);
		
		return new Generic(name, ext, sup);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (Elements.TRANSITION.name.equals(localName))
			transition = null;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (currentElement == null)
			return;
		String str = new String(ch, start, length).trim();
		switch (currentElement) {
		case PACKAGE:
			builder.setPackageName(str);
			break;
		case NAME:
			builder.setGuideName(str);
			break;
		case HEADER:
			gen.setHeader(str);
			break;
		case EXPRESSION:
			gen.setExpression(str);
			break;
		case STATE:
			gen.addStatePath(currentPath, str);
			break;
		case IMPLEMENTS:
			genImpl.setImplements(str);
			break;
		case EXTENDS:
			genImpl.setExtends(str);
			break;
		case BODY:
			transitionImpl.setBody(str);
			break;
		case JAVADOC:
			transition.setJavadoc(str);
			break;
		case RETURN:
			transition.setReturnType(str);
			break;
		case SIGNATURE:
			transition.setSignature(str);
			break;
		}
		currentElement = null; //read
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		throw e;
	}

	@Override
	public void warning(SAXParseException e) throws SAXException {
		System.out.println("warning" + e);
	}

	public static DiezelBuilder<? extends Compilable> parse(File source) throws SAXException, IOException, ParserConfigurationException {
		SAXParser parser = newDiezelSaxParser();
		DiezelParser p = new DiezelParser();
		parser.parse(source, p);
		return p.builder;
	}

	public static SAXParser newDiezelSaxParser() throws SAXException, ParserConfigurationException {
		Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(DiezelBuilder.class.getResource("Diezel.xsd"));
		SAXParserFactory parserF = javax.xml.parsers.SAXParserFactory.newInstance();
		parserF.setSchema(schema);
		parserF.setNamespaceAware(true);
		SAXParser parser = parserF.newSAXParser();
		return parser;
	}
}
