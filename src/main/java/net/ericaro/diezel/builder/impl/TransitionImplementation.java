package net.ericaro.diezel.builder.impl;

import net.ericaro.diezel.builder.lang.Transition;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupString;

/** Represent the extra amount of information needed for an implementation of a given transition.
 * 
 * @author eric
 *
 */
public class TransitionImplementation {

	
	Transition prototype;
	String alias;
	private ST body;

	public TransitionImplementation(String alias) {
		super();
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}

	public void setBody(String str) {
		this.body = new ST(str, '$', '$');
	}

	public ST getBody() {
		return body;
	}
	
	
	
}
