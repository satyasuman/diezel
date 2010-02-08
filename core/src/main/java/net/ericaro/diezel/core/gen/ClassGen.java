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
public class ClassGen extends Gen{

	
	private String javadoc;
	private String modifiers;
	protected String name;
	private List<String> supers = new LinkedList<String>(); // list of super classes (for interfaces only
	private List<String> interfaces = new LinkedList<String>();
	
	
	
	// * static
	private List<StaticGen> statics = new LinkedList<StaticGen>();
	// * fields
	private List<FieldGen> fields = new LinkedList<FieldGen>();
	// * inner classes (no header no import
	private List<InnerClassGen> innerClasses = new LinkedList<InnerClassGen>();
	// * constructor
	private List<ConstructorGen> constructors = new LinkedList<ConstructorGen>();
	// * methods
	private List<MethodGen> methods = new LinkedList<MethodGen>();
	
	/** set the list of superclasse. Use only for interfaces
	 * 
	 * @param supers
	 * @return
	 */
	public ClassGen supers(List<String> supers) {
		this.supers.addAll(supers);
		return this;
	}

	/** var arg of super classes (use only for interfaces)
	 * 
	 * @param supers
	 * @return
	 */
	public ClassGen supers(String... supers) {
		this.supers.addAll(Arrays.asList(supers));
		return this;
	}
	
	/** List of implemented interfaces
	 * 
	 * @param interfaces
	 * @return
	 */
	public ClassGen interfaces(Collection<String> interfaces) {
		this.interfaces.addAll(interfaces);
		return this;
	}

	/** Varargs of implemented interfaces
	 * 
	 * @param interfaces
	 * @return
	 */
	public ClassGen interfaces(String... interfaces) {
		this.interfaces.addAll(Arrays.asList(interfaces));
		return this;
	}
	
	/** list of static statements for this class
	 * 
	 * @param statics
	 * @return
	 */
	public ClassGen statics(List<StaticGen> statics) {
		this.statics.addAll(statics);
		return this;
	}

	/** Var args of static statement.
	 * 
	 * @param statics
	 * @return
	 */
	public ClassGen statics(StaticGen... statics) {
		this.statics.addAll(Arrays.asList(statics));
		return this;
	}
	
	/** LIst of fields defined by this class
	 * 
	 * @param fields
	 * @return
	 */
	public ClassGen fields(List<FieldGen> fields) {
		this.fields.addAll(fields);
		return this;
	}

	/** varargs of fields defined by this class
	 * 
	 * @param fields
	 * @return
	 */
	public ClassGen fields(FieldGen... fields) {
		this.fields.addAll(Arrays.asList(fields));
		return this;
	}
	
	/** List of inner classes defined by this class
	 * 
	 * @param innerClasses
	 * @return
	 */
	public ClassGen inner(List<InnerClassGen> innerClasses) {
		this.innerClasses.addAll(innerClasses);
		return this;
	}

	/** varargs of inner classes
	 * 
	 * @param innerClasses
	 * @return
	 */
	public ClassGen inner(InnerClassGen... innerClasses) {
		this.innerClasses.addAll(Arrays.asList(innerClasses));
		return this;
	}
	
	
	/** List of contructors
	 * 
	 * @param constructors
	 * @return
	 */
	public ClassGen constructors(List<ConstructorGen> constructors) {
		this.constructors.addAll(constructors);
		return this;
	}

	/** varargs of constructor
	 * 
	 * @param constructors
	 * @return
	 */
	public ClassGen constructors(ConstructorGen... constructors) {
		this.constructors.addAll(Arrays.asList(constructors));
		return this;
	}
	
	/** list of methods
	 * 
	 * @param methods
	 * @return
	 */
	public ClassGen methods(List<MethodGen> methods) {
		this.methods.addAll(methods);
		return this;
	}
	
	/** varargs of methods
	 * 
	 * @param methods
	 * @return
	 */
	public ClassGen methods(MethodGen... methods) {
		this.methods.addAll(Arrays.asList(methods));
		return this;
	}
	
	/** defines the javadoc for this class. do not use '/''*' 
	 * 
	 * @param doc
	 * @return
	 */
	public ClassGen javadoc(String doc) {
		this.javadoc = doc;
		return this;
	}
	
	
	/** defines the modifiers for this class. (including the type :( )
	 * 
	 * @param modifiers
	 * @return
	 */
	public ClassGen mod(String modifiers) {
		this.modifiers = modifiers;
		return this;
	}
	
	
	/** define this class name.
	 * 
	 * @param name
	 * @return
	 */
	public ClassGen name(String name) {
		this.name = name;
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

	
	/**
	 * generates the declaration of this class.
	 */
	protected void declaration() {
		_(modifiers)._()._(name)._("extends", supers)._("implements", interfaces)._("{\n");
	}
	

	
	
	

}
