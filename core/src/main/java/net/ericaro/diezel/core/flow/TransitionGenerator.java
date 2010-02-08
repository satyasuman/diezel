package net.ericaro.diezel.core.flow;

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
public class TransitionGenerator {

	private StateGenerator returnState;
	
	private String doc = "";
	private String name;

	private String returnType;
	private List<String> varTypes = new LinkedList<String>();
	private List<String> exceptions = new ArrayList<String>();
	private String targetName;

	private String capture;

	public TransitionGenerator targetName(String targetName) {
		this.targetName = targetName;
		return this;
	}
	
	public TransitionGenerator doc(String doc) {
		this.doc = doc;
		return this;
	}

	public TransitionGenerator name(String name) {
		this.name = name;
		return this;
	}

	public TransitionGenerator returnType(String returnType) {
		this.returnType = returnType;
		return this;
	}

	public TransitionGenerator varTypes(String... varType) {
		
		if (varType!=null) 
			for (String var : varType)
				if (! "".equals(var)) varTypes.add(var);
		return this;
	}

	public TransitionGenerator exceptions(String... exception) {
		exceptions.addAll(Arrays.asList(exception));
		return this;
	}

	public MethodGen getMethodImplementation() {

		MethodGen m = new MethodGen().mod("public").returns(returnType)
				.name(name).param(varTypes).except(exceptions);
		if (!returnState.isExit()) {
			String body = targetName+"."+name+"("+varcall()+");\n";
			body+="return this;\n";
			m.body(body	); //to be improved (do some dsl at least for statements
		}
		else if("void".equals(returnType) ){
			String body = targetName+"."+name+"("+varcall()+");\n";
			m.body(body	); //to be improved (do some dsl at least for statements
		}
		else {
			String body = "return "+targetName+"."+name+"("+varcall()+");\n";
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

	public MethodGen getAdapterMethod() {

		boolean hasReturn = returnState.isExit() && ! "void".equals(returnType) ; 
		MethodGen m = new MethodGen();
		m.mod("public").returns(hasReturn?returnType:"void");
		m.name(name).param(varTypes).except(exceptions).
			body((hasReturn?"return null;":""));
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

	public TransitionGenerator returnState(StateGenerator s) {
		this.returnState = s;
		returnType = s.getName();
		
		return this;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public TransitionGenerator capture(String capture) {
		this.capture = capture;
		return this;
	}

	

}
