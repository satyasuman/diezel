package net.ericaro.diezel.core.gen;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * A Generic Class Generator.
 * 
 * @author eric
 * 
 */
public class Class extends Gen{

	
	private String javadoc;
	private List<Modifier> modifiers = new LinkedList<Modifier>();
	private List<Type> supers = new LinkedList<Type>(); // list of super classes (for interfaces only
	private List<Type> interfaces = new LinkedList<Type>();
	protected String classType;
	protected Type type;
	
	// * static
	private List<StaticBlock> statics = new LinkedList<StaticBlock>();
	// * fields
	private List<Field> fields = new LinkedList<Field>();
	// * inner classes (no header no import
	private List<InnerClass> innerClasses = new LinkedList<InnerClass>();
	// * constructor
	private List<Constructor> constructors = new LinkedList<Constructor>();
	// * methods
	private List<Method> methods = new LinkedList<Method>();
	
	/** set the list of superclasse. Use only for interfaces
	 * 
	 * @param supers
	 * @return
	 */
	public Class supers(List<Type> supers) {
		this.supers.addAll(supers);
		return this;
	}
	
	

	/** var arg of super classes (use only for interfaces)
	 * 
	 * @param supers
	 * @return
	 */
	public Class supers(Type... supers) {
		this.supers.addAll(Arrays.asList(supers));
		return this;
	}
	
	/** set the list of generics
	 * 
	 * @param generics
	 * @return
	 */
	public Class generics(List<Type> generics) {
		this.type.generics(generics);
		return this;
	}
	
	/** var arg of generics
	 * 
	 * @param supers
	 * @return
	 */
	public Class generics(Type... generics) {
		this.generics(Arrays.asList(generics));
		return this;
	}
	
	
	/** List of implemented interfaces
	 * 
	 * @param interfaces
	 * @return
	 */
	public Class interfaces(Collection<Type> interfaces) {
		this.interfaces.addAll(interfaces);
		return this;
	}

	/** Varargs of implemented interfaces
	 * 
	 * @param interfaces
	 * @return
	 */
	public Class interfaces(Type... interfaces) {
		this.interfaces.addAll(Arrays.asList(interfaces));
		return this;
	}
	
	/** list of static statements for this class
	 * 
	 * @param statics
	 * @return
	 */
	public Class statics(List<StaticBlock> statics) {
		this.statics.addAll(statics);
		return this;
	}

	/** Var args of static statement.
	 * 
	 * @param statics
	 * @return
	 */
	public Class statics(StaticBlock... statics) {
		this.statics.addAll(Arrays.asList(statics));
		return this;
	}
	
	/** LIst of fields defined by this class
	 * 
	 * @param fields
	 * @return
	 */
	public Class fields(List<Field> fields) {
		this.fields.addAll(fields);
		return this;
	}

	/** varargs of fields defined by this class
	 * 
	 * @param fields
	 * @return
	 */
	public Class fields(Field... fields) {
		this.fields.addAll(Arrays.asList(fields));
		return this;
	}
	
	/** List of inner classes defined by this class
	 * 
	 * @param innerClasses
	 * @return
	 */
	public Class inner(List<InnerClass> innerClasses) {
		this.innerClasses.addAll(innerClasses);
		return this;
	}

	/** varargs of inner classes
	 * 
	 * @param innerClasses
	 * @return
	 */
	public Class inner(InnerClass... innerClasses) {
		this.innerClasses.addAll(Arrays.asList(innerClasses));
		return this;
	}
	
	
	/** List of contructors
	 * 
	 * @param constructors
	 * @return
	 */
	public Class constructors(List<Constructor> constructors) {
		this.constructors.addAll(constructors);
		return this;
	}

	/** varargs of constructor
	 * 
	 * @param constructors
	 * @return
	 */
	public Class constructors(Constructor... constructors) {
		this.constructors.addAll(Arrays.asList(constructors));
		return this;
	}
	
	/** list of methods
	 * 
	 * @param methods
	 * @return
	 */
	public Class methods(List<Method> methods) {
		this.methods.addAll(methods);
		return this;
	}
	
	/** varargs of methods
	 * 
	 * @param methods
	 * @return
	 */
	public Class methods(Method... methods) {
		this.methods.addAll(Arrays.asList(methods));
		return this;
	}
	
	/** defines the javadoc for this class. do not use '/''*' 
	 * 
	 * @param doc
	 * @return
	 */
	public Class javadoc(String doc) {
		this.javadoc = doc;
		return this;
	}
	
	
	/** defines the modifiers for this class. (including the type :( )
	 * 
	 * @param modifiers
	 * @return
	 */
	public Class mod(Modifier... modifiers) {
		return mod(Arrays.asList(modifiers));
	}
	public Class mod(List<Modifier> modifiers) {
		this.modifiers.addAll(modifiers);
		return this;
	}
	
	
	/** define this class name.
	 * 
	 * @param name
	 * @return
	 */
	public Class type(Type type) {
		this.type= type;
		return this;
	}
		
	
	/** generates the class. delegate stuff to doc(), and declaration part.
	 * 
	 */
	@Override
	protected void genImpl() {
		doc();		// doc
		declaration(); // declaration
		// * static
		_(statics);
		// * fields
		_(fields);
		// * inner classes (no header no import
		_(innerClasses);
		// * constructor
		_(constructors);
		// * methods
		_(methods);
		
		_("\n}\n");
	}


	/** generates the javadoc part.
	 * 
	 */
	protected void doc() {
		_("/**")._(javadoc)._("\n")._("*/\n");
	}
	
	public Class asClass(){
		this.classType = "class";
		return this;
	}
	public Class asInterface() {
		this.classType="interface";
		return this;
	}
	
	/**
	 * generates the declaration of this class.
	 */
	protected void declaration() {
		_(modifiers)._()._(classType)._()._(type)._("extends", supers)._("implements", interfaces)._("{\n");
	}



	

	
	
	

}
