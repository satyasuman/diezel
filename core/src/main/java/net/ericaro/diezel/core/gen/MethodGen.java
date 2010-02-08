package net.ericaro.diezel.core.gen;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.ericaro.diezel.core.flow.FlowGenerator;
import net.ericaro.diezel.core.flow.TransitionGenerator;

public class MethodGen extends Gen {

	protected String javadoc;
	protected String modifiers;// one of public static etc... later creates a "modifier gen for more general purpose
	protected String name; // the method name
	protected String methodBody; // the method body
	protected String returnType; // the fully qualified return type
	protected List<String> exceptions=new LinkedList<String>(); // list of fully qualified exceptions (generics accepted)
	protected List<String> varTypes= new LinkedList<String>();// list of arguments types
	private List<String> captureTypes; // list of generic capture like in public <T> toto( Class<T> arg)
	
	/** Defines the javadoc for this method. Do not put leading and trailing comment marker (/ * and * /
	 * 
	 * @param doc
	 * @return
	 */
	public MethodGen javadoc(String doc){this.javadoc = doc;return this;}
	
	/** Defines the modifier (any string like "public static" ). Don't worry about space
	 * 
	 * @param modifiers
	 * @return
	 */
	public MethodGen mod(String modifiers){this.modifiers= modifiers;return this;}
	
	/** Set this method name
	 * 
	 * @param name
	 * @return
	 */
	public MethodGen name(String name) {
		this.name = name;
		return this;
	}
	
	/** Sets this method body (do not add leading and trailing '{' '}' )
	 * 
	 * @param methodBody
	 * @return
	 */
	public MethodGen body(String methodBody) {
		this.methodBody = methodBody;
		return this;
	}
	
	/** Sets the method return type
	 * 
	 * @param returnType
	 * @return
	 */
	public MethodGen returns(String returnType) {
		this.returnType = returnType;
		return this;
	}
	
	/** Sets the list of generic captured types. Must match captured type in argument types)
	 * 
	 * @param captureTypes
	 * @return
	 */
	public MethodGen captures(String... captureTypes) {
		this.captureTypes= Arrays.asList(captureTypes);
		return this;
	}
	
	/** var args of fully qualified name of exceptions
	 * 
	 * @param exceptions
	 * @return
	 */
	public MethodGen except(String... exceptions) {
		this.exceptions.addAll(Arrays.asList(exceptions));
		return this;
	}
	/** List of fully qualified name of exceptions
	 * 
	 * @param exceptions
	 * @return
	 */
	public MethodGen except(List<String> exceptions) {
		this.exceptions.addAll(exceptions);
		return this;
	}
	
	/** var args of fully qualified name parameters types (generic accepted)
	 * 
	 * @param varTypes
	 * @return
	 */
	public MethodGen param(String... varTypes) {
		this.varTypes.addAll(Arrays.asList(varTypes));
		return this;
	}
	/** List of fully qualified names of parameters types (generic accepted)
	 * 
	 * @param varTypes
	 * @return
	 */
	public MethodGen param(List<String> varTypes) {
		this.varTypes.addAll(varTypes);
		return this;
	}
	
	/** Actual generation, it delegates some parts of the generations to capture, methodName, returnType, vardef, methodBody
	 * 
	 */
	@Override
	protected void genImpl() {
		_("\n/**")._(javadoc)._("*/\n")._(modifiers)._();
		captures();
		returnType();
		methodName();
		
		_("(");
		vardef();
		_(")")._("throws", exceptions);
		
		methodBody();
	}

	
	/** Generates the method name. For overriding purpose
	 * 
	 */
	protected void methodName() {
		_(name);
	}

	/** Generate the capture definition
	 * 
	 */
	protected void captures() {
		if (captureTypes!=null&& !captureTypes.isEmpty())
		{
			_("<")._(captureTypes, ",")._(">");
		}
	}
	
	/** Generate the return type. null return type is converted into void
	 * 
	 */
	protected void returnType() {
		_(returnType==null?"void": returnType)._();
	}

	/** add trailing and leading '{' '}' and append the method body
	 * 
	 */
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

	
	/** generates the unique name of the ith parameter name. 
	 * 
	 * @param i
	 * @return
	 */
	public String arg(int i) {
		return "arg"+i;
	}
}
