package net.ericaro.diezel.core.builder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ericaro.diezel.annotations.Workflow;
import net.ericaro.diezel.core.gen.CompilationUnit;
import net.ericaro.diezel.core.gen.Constructor;
import net.ericaro.diezel.core.gen.Field;
import net.ericaro.diezel.core.gen.FileUnit;
import net.ericaro.diezel.core.gen.Modifier;
import net.ericaro.diezel.core.gen.ResourceUnit;
import net.ericaro.diezel.core.gen.Type;
import net.ericaro.diezel.core.parser.Graph;
import net.ericaro.diezel.core.parser.GraphBuilder;
import net.ericaro.diezel.core.parser.ParseException;
import net.ericaro.diezel.core.parser.RegExp;
import net.ericaro.diezel.core.parser.Graph.S;
import net.ericaro.diezel.core.parser.Graph.T;

/**
 *  Reads an actual Builder.class, extract all reflexive information in types, and annotations, and get ready for generation.
 *  By convention, the generated classes are called guides.
 * 
 * 
 * @author eric
 * 
 */
public class DiezelGenerator {

	/** QName of the Guiders package. By default, use the target one.
	 */
	transient private String packageName;
	/** The workflow expressed in the workflow language.
	 */
	transient private String workflow;
	/** While parsing the target recursively parse each method and convert them into transition
	 */
	transient private Set<Transition> transitions = new HashSet<Transition>();
	/** The exit state builderType. A workflow can EXIT, is that case it returns a instance located in the guide class, with returnType as builderType, and returnName as name.
	 */
	transient private Type returnType = new Type(); // void by default
	/** The exit state builderType. A workflow can EXIT, is that case it returns a instance located in the guide class, with returnType as builderType, and returnName as name.
	 */
	transient private String returnName;
	/** After parsing the workflow as string, a Graph of state and transition is generated.
	 */
	transient private Graph graph;
	/** each guide contains the builder instance as a field of builderType builderType, and name builderName
	 */
	transient private String builderName;
	/** each guide contains the builder instance as a field of builderType builderType, and name builderName
	 */
	transient private Type builderType;
	/** Every guide class generate is uses the guidebasename as name + an index to make them unique.
	 */
	transient private String guideBaseName;
	
	/** A workflow is "callable" (from another workflow) if it can return back to where it was.
	 */
	transient private boolean callable;
	/** the header use for each generated file. by default, we provide an old but nice ascii art saying that it was generated with Diezel ;-)
	 */
	transient private String header;

	// all field are made transient because they all are deduced from the parsed class.
	// all accessor are made package to be accessed from the Transition, without polluting the real public interface of
	// this object


	public static <T> DiezelGenerator parse(Class<T> builder) {
		return new DiezelGenerator(builder);
	}

	public DiezelGenerator(Class<?> builder) {
		init(builder);
	}

	public static <T> Type typeOf(Class<T> c) {
		Type type = new Type();
		type.type(c.getCanonicalName());
		for (TypeVariable<Class<T>> p : c.getTypeParameters()) {
			type.generics(new Type().type(p.getName()));
		}
		return type;
	}

	protected void init(Class<?> builder) {
		// gets all information.
		// abuilder is a workflow, a collection of transitions.
		Workflow d = builder.getAnnotation(Workflow.class);
		workflow = d.value();
		builderName = d.builderInstance();
		callable = d.callable();
		guideBaseName = d.guideBaseName();
		header = d.header();
		returnName = d.returnGuideInstance();

		if (guideBaseName == null || "".equals(guideBaseName)) {
			guideBaseName = builder.getSimpleName() + "Guide";
		}

		builderType = typeOf(builder);
		List<Type> l = builderType.getGenerics();
		if (!l.isEmpty())
			returnType = l.get(0);
		packageName = builder.getPackage().getName();

		// test ing returntype, and callable options
		if (callable && returnType.isVoid()) {
			returnType.type("T"); // force to something
		}
		if (!callable)
			returnType = new Type(); // force to void

		for (Method m : builder.getMethods()) {
			Transition t = new Transition();
			t.parse(m);
			transitions.add(t);

		}
	}

	public void buildGraph() throws ParseException {
		GraphBuilder db = new GraphBuilder();
		RegExp.parse(workflow, db);
		this.graph = db.getGraph();
	}

	public List<FileUnit> generate() {

		// loop ov er the grah
		// for each state creates a compilation unit
		// for each trans creates a transition unit

		// build a faster index for transition by name
		Map<String, Transition> index = new HashMap<String, Transition>();
		for (Transition t : transitions)
			index.put(t.alias, t);

		// build an index S.id -> Type for transition generation
		Map<Integer, Type> stateTypes = new HashMap<Integer, Type>();
		int i = 0;
		for (S s : graph.states) {
			Type stateType = new Type();
			if (s.equals(graph.in))
				stateType.type(guideBaseName);
			else
				stateType.type(guideBaseName + ++i);
			if (builderType.getGenerics().isEmpty() && callable) {
				stateType.generics(returnType);
			} else {
				stateType.generics(builderType.getGenerics());
			}
			stateTypes.put(s.id, stateType);
			System.out.println("preparing " + stateType);
		}

		List<FileUnit> fileunits = new LinkedList<FileUnit>();

		for (S s : graph.states) {
			Type fromType = stateTypes.get(s.id);
			System.out.println("generating " + fromType);
			CompilationUnit cu = createCompilationUnit(fromType);
			// append transitions
			for (T t : s.outs) {
				Type toType = stateTypes.get(t.out.id);
				System.out.println("\tto " + toType);
				Transition trans = index.get(t.name);
				cu.methods(trans.getTransitionMethod(toType, this));
			}
			fileunits.add(cu);
		}

		fileunits.add(new ResourceUnit().packageName(packageName).name(
				"guide-graph.dot").content(graph.toString()));

		return fileunits;
	}

	public static void generate(Class builder, File dir) throws ParseException,
			IOException {
		DiezelGenerator b = DiezelGenerator.parse(builder);
		b.buildGraph();
		b.toFile(dir);

	}

	public void toFile(File dir) throws IOException {
		for (FileUnit u : generate())
			u.toDir(dir);
	}

	CompilationUnit createCompilationUnit(Type type) {
		CompilationUnit c = new CompilationUnit().header(header).packageName(
				packageName);
		c.mod(Modifier.Public).asClass().type(type);

		Type targetType = this.builderType;
		if (callable && !returnType.isVoid()) {
			// field and constructor with return builderType
			c.fields(new Field().mod(Modifier.Private).type(returnType).name(returnName));
			Constructor cons = new Constructor().container(type.getName());
			cons.mod(Modifier.Public).param(targetType, returnType).body(
					"this." + builderName + " = " + cons.arg(0) + ";" + "this."
							+ returnName + " = " + cons.arg(1) + ";");
			c.constructors(cons);
		}

		Constructor cons = new Constructor().container(type.getName());
		cons.mod(Modifier.Public).param(targetType).body(
				"this." + builderName + " = " + cons.arg(0) + ";");
		c.constructors(cons);

		c.fields(new Field().mod(Modifier.Private).type(targetType).name(builderName));
		return c;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("workflow: ").append(workflow).append("\n");
		sb.append("package: ").append(packageName).append("\n");
		sb.append("builderType: ");
		builderType.gen(sb);
		sb.append("\n");
		for (Transition t : transitions) {
			sb.append("transition:\n").append(t).append("\n");
		}
		return sb.toString();
	}

	
	// accessors 
	
	
	String getPackageName() {
		return packageName;
	}

	String getWorkflow() {
		return workflow;
	}

	Set<Transition> getTransitions() {
		return transitions;
	}

	Type getReturnType() {
		return returnType;
	}

	String getReturnName() {
		return returnName;
	}

	Graph getGraph() {
		return graph;
	}

	String getBuilderName() {
		return builderName;
	}

	Type getBuilderType() {
		return builderType;
	}

	String getGuideBaseName() {
		return guideBaseName;
	}

	boolean isCallable() {
		return callable;
	}

	String getHeader() {
		return header;
	}
	
	
	
	
}
