package net.ericaro.diezel.core.gen;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.ericaro.diezel.core.Parser;
import net.ericaro.diezel.core.Transition;

public class MethodGen extends Gen {

	protected String doc;
	protected String modifiers;
	protected String name;
	protected String methodBody;
	protected String returnType;
	protected List<String> exceptions=new LinkedList<String>();
	protected List<String> varTypes= new LinkedList<String>();
	
	public MethodGen javadoc(String doc){this.doc = doc;return this;}
	public MethodGen mod(String modifiers){this.modifiers= modifiers;return this;}
	public MethodGen name(String name) {
		this.name = name;
		return this;
	}
	public MethodGen body(String methodBody) {
		this.methodBody = methodBody;
		return this;
	}

	public MethodGen returns(String returnType) {
		this.returnType = returnType;
		return this;
	}
	public MethodGen except(String... exceptions) {
		this.exceptions.addAll(Arrays.asList(exceptions));
		return this;
	}
	public MethodGen except(List<String> exceptions) {
		this.exceptions.addAll(exceptions);
		return this;
	}
	public MethodGen param(String... varTypes) {
		this.varTypes.addAll(Arrays.asList(varTypes));
		return this;
	}
	public MethodGen param(List<String> varTypes) {
		this.varTypes.addAll(varTypes);
		return this;
	}
	
	@Override
	protected void genImpl() {
		_("\n/**")._(doc)._("*/\n")._(modifiers)._();
		returnType();
		methodName();
		
		_("(");
		vardef();
		_(")")._("throws", exceptions);
		
		methodBody();
	}

	protected void methodName() {
		_(name);
	}

	protected void returnType() {
		_(returnType==null?"void": returnType)._();
	}

	protected void methodBody() {
		_("{\n")
		._(methodBody)
		._("} \n");
	}

	private void vardef() {
			int i=0;
			if (varTypes!=null) 
				for(String v: varTypes) 
					_( i==0?"":", ")._(v)._()._(arg(i++) );
	}

	public String arg(int i) {
		return "arg"+i;
	}
}
