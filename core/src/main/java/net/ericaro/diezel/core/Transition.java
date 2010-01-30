package net.ericaro.diezel.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.ericaro.diezel.core.gen.AbstractMethodGen;
import net.ericaro.diezel.core.gen.MethodGen;
import net.ericaro.diezel.core.graph.Graph.S;

/**
 * Simple generator for methods
 * 
 * @author eric
 * 
 */
public class Transition {

	private State returnState;
	
	private String doc = "";
	private String name;

	private String returnType;
	private List<String> varTypes = new LinkedList<String>();
	private List<String> exceptions = new ArrayList<String>();
	private String host;

	public Transition host(String host) {
		this.host = host;
		return this;
	}
	
	public Transition doc(String doc) {
		this.doc = doc;
		return this;
	}

	public Transition name(String name) {
		this.name = name;
		return this;
	}

	public Transition returnType(String returnType) {
		this.returnType = returnType;
		return this;
	}

	public Transition varTypes(String... varType) {
		varTypes.addAll(Arrays.asList(varType));
		return this;
	}

	public Transition exceptions(String... exception) {
		exceptions.addAll(Arrays.asList(exception));
		return this;
	}

	public MethodGen getMethodImplementation() {

		MethodGen m = new MethodGen().mod("public").returns(returnType)
				.name(name).param(varTypes).except(exceptions);
		if (!returnState.isExit()) {
			String body = host+"."+name+"("+varcall()+");\n";
			body+="return this;\n";
			m.body(body	); //to be improved (do some dsl at least for statements
		}
		else if("void".equals(returnType) ){
			String body = host+"."+name+"("+varcall()+");\n";
			m.body(body	); //to be improved (do some dsl at least for statements
		}
		else {
			String body = "return "+host+"."+name+"("+varcall()+");\n";
			m.body(body	); //to be improved (do some dsl at least for statements
		}
		return m;
	}

	public AbstractMethodGen getMethodInterface() {

		AbstractMethodGen m = new AbstractMethodGen();
		m.mod("public").returns(returnType);
		m.name(name).param(varTypes).except(exceptions);
		return m;
	}

	public AbstractMethodGen getHostMethod() {

		AbstractMethodGen m = new AbstractMethodGen();
		m.mod("public");
		if (returnState.isExit() && ! "void".equals(returnType))  
			m.returns(returnType);
		else m.returns("void");
		
		m.name(name).param(varTypes).except(exceptions);
		return m;
	}

	private String varcall() {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		if (varTypes != null)
			for (String v : varTypes)
				sb.append(i == 0 ? "" : ", ").append("arg" + (i++));
		return sb.toString();
	}

	private String exceptions() {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		if (exceptions != null)
			for (String e : exceptions)
				sb.append((i++) == 0 ? "" : ", ").append(e);
		return sb.toString();
	}

	private String vardef() {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		if (varTypes != null)
			for (String v : varTypes)
				sb.append((i++) == 0 ? "" : ", ").append(v);
		return sb.toString();
	}

	public Transition returnState(State s) {
		this.returnState = s;
		returnType = s.getName();
		
		return this;
		
	}

}
