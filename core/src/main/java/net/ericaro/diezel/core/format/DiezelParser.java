package net.ericaro.diezel.core.format;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import net.ericaro.diezel.core.builder.DiezelGenerator;
import net.ericaro.diezel.core.builder.Generic;
import net.ericaro.diezel.core.builder.Transition;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DiezelParser {

	public static final String XMLNS = "http://diezel.ericaro.net/2.0.0/";
	public static final String DIEZEL  = "diezel";
	public static final String PACKAGE = "package";
	public static final String HEADER = "header";
	public static final String EXPRESSION = "expression";
	public static final String PUSH= "push";
	public static final String PULL= "pull";
	public static final String JAVADOC = "javadoc";
	public static final String RETURN = "return";
	public static final String SIGNATURE= "signature";
	public static final String TRANSITION  = "transition";
	public static final String ATT_NAME = "name";
	public static final String ATT_EXTENDS = "extends";
	public static final String ATT_SUPER = "super";
	
	enum Elements{
		    PACKAGE,
			HEADER,
			EXPRESSION,
			JAVADOC,
			RETURN,
			SIGNATURE;
	}
	private DiezelGenerator gen;
	public DiezelGenerator parse(File source) throws SAXException, IOException, ParserConfigurationException{
		
		Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(getClass().getResource("/Diezel.xsd"));
		
		SAXParserFactory parserF = javax.xml.parsers.SAXParserFactory.newInstance();
		parserF.setSchema(schema);
		parserF.setNamespaceAware(true);
		SAXParser parser = parserF.newSAXParser();
		parser.parse(source, new DefaultHandler(){

			Transition transition;
			private Elements currentElement;

			@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
				assert uri == XMLNS;
				System.out.println(localName);
				if (DIEZEL.equals(localName) ){
					gen = new DiezelGenerator();
				}
				else if (PUSH.equals(localName)){
					Generic generic = readGeneric(attributes) ;
					if (transition == null)
						gen.addRootType(generic);
					else
						transition.addPush(generic);
				}
				else if (PULL.equals(localName)){
					Generic generic = readGeneric(attributes) ;
					transition.addPush(generic);
				}
				else if (TRANSITION.equals(localName)){
					transition = new Transition(attributes.getValue(ATT_NAME));
					gen.addTransition(transition);
				}
				else if (PACKAGE.equals(localName))
					currentElement = Elements.PACKAGE;
				else if (HEADER.equals(localName))
					currentElement = Elements.HEADER;
				else if (EXPRESSION.equals(localName))
					currentElement = Elements.EXPRESSION;
				else if (JAVADOC.equals(localName))
					currentElement = Elements.JAVADOC;
				else if (RETURN.equals(localName))
					currentElement = Elements.RETURN;
				else if (SIGNATURE.equals(localName))
					currentElement = Elements.SIGNATURE;
				else
					currentElement = null;
			}

			
			private Generic readGeneric(Attributes attributes) {
				Generic gen = new Generic(attributes.getValue(ATT_NAME));
				gen.setExtendsType(attributes.getValue(ATT_EXTENDS));
				gen.setSuperType(attributes.getValue(ATT_SUPER));
				return gen;
			}


			@Override
			public void endElement(String uri, String localName, String qName) throws SAXException {
				if (TRANSITION.equals(localName))
					transition = null;
			}


			public String[] parseList(String value) {
				if (value == null)
					return new String[0];
				return value.split(" |,");
			}

			@Override
			public void characters(char[] ch, int start, int length) throws SAXException {
				if (currentElement==null) return;
				String str = new String(ch,start,length);
				//System.out.println(str);
				switch(currentElement){
				case PACKAGE:
					gen.setPackageName(str);
					break;
				case HEADER:
					gen.setHeader(str);
					break;
				case EXPRESSION:
					gen.setWorkflow(str);
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

			
			
			
		});
		
		return gen;
		
	}
}
