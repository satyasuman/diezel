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

	
	private String doc;
	private String modifiers;
	protected String name;
	private List<String> supers = new LinkedList<String>();
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
	
	public ClassGen supers(List<String> supers) {
		this.supers.addAll(supers);
		return this;
	}

	public ClassGen supers(String... supers) {
		this.supers.addAll(Arrays.asList(supers));
		return this;
	}
	
	public ClassGen interfaces(Collection<String> interfaces) {
		this.interfaces.addAll(interfaces);
		return this;
	}

	public ClassGen interfaces(String... interfaces) {
		this.interfaces.addAll(Arrays.asList(interfaces));
		return this;
	}
	
	public ClassGen statics(List<StaticGen> statics) {
		this.statics.addAll(statics);
		return this;
	}

	public ClassGen statics(StaticGen... statics) {
		this.statics.addAll(Arrays.asList(statics));
		return this;
	}
	
	public ClassGen fields(List<FieldGen> fields) {
		this.fields.addAll(fields);
		return this;
	}

	public ClassGen fields(FieldGen... fields) {
		this.fields.addAll(Arrays.asList(fields));
		return this;
	}
	
	public ClassGen inner(List<InnerClassGen> innerClasses) {
		this.innerClasses.addAll(innerClasses);
		return this;
	}

	public ClassGen inner(InnerClassGen... innerClasses) {
		this.innerClasses.addAll(Arrays.asList(innerClasses));
		return this;
	}
	
	public ClassGen constructors(List<ConstructorGen> constructors) {
		this.constructors.addAll(constructors);
		return this;
	}

	public ClassGen constructors(ConstructorGen... constructors) {
		this.constructors.addAll(Arrays.asList(constructors));
		return this;
	}
	
	public ClassGen methods(List<MethodGen> methods) {
		this.methods.addAll(methods);
		return this;
	}
	
	public ClassGen methods(MethodGen... methods) {
		this.methods.addAll(Arrays.asList(methods));
		return this;
	}
	
	public ClassGen javadoc(String doc) {
		this.doc = doc;
		return this;
	}
	
	
	public ClassGen mod(String modifiers) {
		this.modifiers = modifiers;
		return this;
	}
	
	public ClassGen name(String name) {
		this.name = name;
		return this;
	}
	
	
	
	

	
	
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


	public void doc() {
		_(doc)._("\n");
	}

	public void declaration() {
		_(modifiers)._()._(name)._("extends", supers)._("implements", interfaces)._("{\n");
	}
	

	
	
	

}
