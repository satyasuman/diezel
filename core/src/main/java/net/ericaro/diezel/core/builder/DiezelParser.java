package net.ericaro.diezel.core.builder;

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


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses an XML diezel file, and build a DiezelGenerator class.
 * 
 * @author eric
 */
public class DiezelParser extends DefaultHandler {

	public static final String XMLNS = "http://diezel.ericaro.net/2.0.0/";

	public static final String ATT_NAME = "name";
	public static final String ATT_EXTENDS = "extends";
	public static final String ATT_SUPER = "super";

	// simple text element are read using this enum
	static Map<String, Elements> names = new HashMap<String, Elements>();

	enum Elements {
		DIEZEL("diezel"), TRANSITION("transition"), TRANSITIONS("transitions"), PUSH("push"), PULL("pull"), PACKAGE("package"), HEADER("header"), EXPRESSION("expression"), JAVADOC("javadoc"), RETURN("return"), SIGNATURE("signature"), GUIDE("guide"), STATES("states"), STATE("state");
		String name;

		Elements(String name) {
			this.name = name;
			names.put(name, this);
		}

		static Elements byName(String name) {
			return names.get(name);
		}

	}

	private DiezelGenerator gen;
	private Transition transition;
	private Elements currentElement;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		assert uri == XMLNS : "illegal elementn outside the xmlns";

		currentElement = Elements.byName(localName);
		assert currentElement !=null : "found and unknown element "+localName;
		switch (currentElement) {
		case DIEZEL:
			gen = new DiezelGenerator();
			break;
		case PUSH:
			Generic generic = readGeneric(attributes);
			if (transition == null)
				gen.addRootType(generic);
			else
				transition.addPush(generic);
			break;
		case PULL:
			generic = readGeneric(attributes);
			transition.addPush(generic);
			break;
		case TRANSITION:
			transition = new Transition(attributes.getValue(ATT_NAME));
			gen.addTransition(transition);
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
		Generic gen = new Generic(attributes.getValue(ATT_NAME));
		gen.setExtendsType(attributes.getValue(ATT_EXTENDS));
		gen.setSuperType(attributes.getValue(ATT_SUPER));
		return gen;
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
			gen.setPackageName(str);
			break;
		case HEADER:
			gen.setHeader(str);
			break;
		case GUIDE:
			gen.setGuideName(str);
			break;
		case EXPRESSION:
			gen.setExpression(str);
			break;
		case STATE:
			gen.addStateName(str);
			break;
		case JAVADOC:
			transition.setJavadoc(str);
			break;
		case RETURN:
			transition.setOptionalReturnType(str);
			break;
		case SIGNATURE:
			transition.setAfterTypeDeclaration(str);
			break;
		}
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		throw e;
	}

	@Override
	public void warning(SAXParseException e) throws SAXException {
		System.out.println("warning" + e);
	}

	public static DiezelGenerator parse(File source) throws SAXException, IOException, ParserConfigurationException {
		SAXParser parser = newDiezelSaxParser();
		DiezelParser p = new DiezelParser();
		parser.parse(source, p);
		return p.gen;
	}

	public static SAXParser newDiezelSaxParser() throws SAXException, ParserConfigurationException {
		Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(DiezelParser.class.getResource("Diezel.xsd"));
		SAXParserFactory parserF = javax.xml.parsers.SAXParserFactory.newInstance();
		parserF.setSchema(schema);
		parserF.setNamespaceAware(true);
		SAXParser parser = parserF.newSAXParser();
		return parser;
	}
}
