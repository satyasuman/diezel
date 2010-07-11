package net.ericaro.diezel.core.gen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class Method extends Gen {

	protected String javadoc;
	protected List<Modifier> modifiers = new ArrayList<Modifier>();// one of public static etc... later creates a "modifier gen for more general purpose
	protected String name; // the method name
	protected String methodBody; // the method body
	protected Type returnType; // the fully qualified return type
	protected List<Type> exceptions=new LinkedList<Type>(); // list of fully qualified exceptions (generics accepted)
	protected List<Type> varTypes= new LinkedList<Type>();// list of arguments types
	private List<Type> captureTypes = new LinkedList<Type>(); // list of generic capture like in public <T> toto( Class<T> arg)
	
	/** Defines the javadoc for this method. Do not put leading and trailing comment marker (/ * and * /
	 * 
	 * @param doc
	 * @return
	 */
	public Method javadoc(String doc){this.javadoc = doc;return this;}
	
	/** defines the modifiers for this class.
	 * 
	 * @param modifiers
	 * @return
	 */
	public Method mod(Modifier... modifiers) {
		return mod(Arrays.asList(modifiers));
	}
	public Method mod(List<Modifier> modifiers) {
		this.modifiers.addAll(modifiers);
		return this;
	}
	
	/** Set this method name
	 * 
	 * @param name
	 * @return
	 */
	public Method name(String name) {
		this.name = name;
		return this;
	}
	
	/** Sets this method body (do not add leading and trailing '{' '}' )
	 * 
	 * @param methodBody
	 * @return
	 */
	public Method body(String methodBody) {
		this.methodBody = methodBody;
		return this;
	}
	
	/** Sets the method return type
	 * 
	 * @param returnType
	 * @return
	 */
	public Method returns(Type returnType) {
		this.returnType = returnType;
		return this;
	}
	
	/** Sets the list of generic captured types. Must match captured type in argument types)
	 * 
	 * @param captureTypes
	 * @return
	 */
	public Method captures(Type... captureTypes) {
		this.captures( Arrays.asList(captureTypes) );
		return this;
	}
	
	public Method captures(List<Type> captureTypes) {
		this.captureTypes.addAll( captureTypes );
		return this;
	}
	
	/** var args of fully qualified name of exceptions
	 * 
	 * @param exceptions
	 * @return
	 */
	public Method except(Type... exceptions) {
		this.exceptions.addAll(Arrays.asList(exceptions));
		return this;
	}
	/** List of fully qualified name of exceptions
	 * 
	 * @param exceptions
	 * @return
	 */
	public Method except(List<Type> exceptions) {
		this.exceptions.addAll(exceptions);
		return this;
	}
	
	/** var args of fully qualified name parameters types (generic accepted)
	 * 
	 * @param varTypes
	 * @return
	 */
	public Method param(Type... varTypes) {
		return param(Arrays.asList(varTypes));
	}
	/** List of fully qualified names of parameters types (generic accepted)
	 * 
	 * @param varTypes
	 * @return
	 */
	public Method param(List<Type> varTypes) {
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
		_opt("<", "> ", captureTypes);
	}
	
	/** Generate the return type. null return type is converted into void
	 * 
	 */
	protected void returnType() {
		_(returnType==null?Type.Void: returnType)._();
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
				for(Type v: varTypes) 
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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Method) {
			Method m = (Method) obj;
			return m.signature().equals(signature());
		}
		return false;
	}

	@Override
	public int hashCode() {
			return signature().hashCode() ;
	}
	
	public String signature(){
		StringBuilder sb = new StringBuilder();
		sb.append(name).append("[");
		for (Type t: varTypes){
			sb.append(t.getName()).append(',');
		}
		return sb.toString() ;

	}
	

	public static String varcall(List<Type> parameters) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		if (parameters != null)
			for (Type v : parameters)
				sb.append(i == 0 ? "" : ", ").append("arg" + (i++));
		return sb.toString();
	}
	
	public static String vardef(List<Type> parameters) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		if (parameters != null)
			for (Type v : parameters)
				sb.append((i++) == 0 ? "" : ", ").append(v);
		return sb.toString();
	}

	
}
