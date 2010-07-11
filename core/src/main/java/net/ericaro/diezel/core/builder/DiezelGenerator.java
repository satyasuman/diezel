package net.ericaro.diezel.core.builder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.portable.UnknownException;

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
 * Reads an actual Builder.class, extract all reflexive information in types,
 * and annotations, and get ready for generation. By convention, the generated
 * classes are called guides.
 * 
 * 
 * @author eric
 * 
 */
public class DiezelGenerator {

	/**
	 * QName of the Guiders package. By default, use the target one.
	 */
	transient private String packageName;
	/**
	 * The workflow expressed in the workflow language.
	 */
	transient private String workflow;
	/**
	 * While parsing the target recursively parse each method and convert them
	 * into transition
	 */
	transient private Set<Transition> transitions = new HashSet<Transition>();
	/**
	 * The exit state builderType. A workflow can EXIT, is that case it returns
	 * a instance located in the guide class, with returnType as builderType,
	 * and returnName as name.
	 */
	transient private Type returnType = new Type(); // void by default
	/**
	 * The exit state builderType. A workflow can EXIT, is that case it returns
	 * a instance located in the guide class, with returnType as builderType,
	 * and returnName as name.
	 */
	transient private String returnName;
	/**
	 * After parsing the workflow as string, a Graph of state and transition is
	 * generated.
	 */
	transient private Graph graph;
	/**
	 * each guide contains the builder instance as a field of builderType
	 * builderType, and name builderName
	 */
	transient private String builderName;
	
	/**
	 * Every guide class generate is uses the guidebasename as name + an index
	 * to make them unique.
	 */
	transient private String guideBaseName;

	/**
	 * A workflow is "callable" (from another workflow) if it can return back to
	 * where it was.
	 */
	transient private boolean callable;
	/**
	 * the header use for each generated file. by default, we provide an old but
	 * nice ascii art saying that it was generated with Diezel ;-)
	 */
	transient private String header;
	
	transient private Class<?> builder;

	// all field are made transient because they all are deduced from the parsed
	// class.
	// all accessor are made package to be accessed from the Transition, without
	// polluting the real public interface of
	// this object

	public static <T> DiezelGenerator parse(Class<T> builder) {
		return new DiezelGenerator(builder);
	}

	public DiezelGenerator(Class<?> builder) {
		init(builder);
	}

	public static Type typeOf(java.lang.reflect.Type c) {
		return typeOf(c, false);
	}
	
	public static Type typeOfDefinition(java.lang.reflect.Type c) {
		return typeOf(c, true);
	}

	public static Type typeOf(java.lang.reflect.Type c, boolean definition) {
		if (c instanceof GenericArrayType) {
			GenericArrayType array = (GenericArrayType) c;
			Type type = typeOf(array.getGenericComponentType()).array();
			return type;
		}
		if (c instanceof ParameterizedType) {
			ParameterizedType p = (ParameterizedType) c;
			Type type = typeOf(p.getRawType());
			type.getGenerics().clear();
			for (java.lang.reflect.Type pt : p.getActualTypeArguments()) {
				type.generics(typeOf(pt));
			}
			return type;
		}
		if (c instanceof WildcardType) {
			WildcardType w = (WildcardType) c;
			Type type = new Type();
			type.type("?");
			for (java.lang.reflect.Type t : w.getUpperBounds())
				type.upperBound(typeOf(t));
			for (java.lang.reflect.Type t : w.getLowerBounds())
				type.lowerBound(typeOf(t));
			return type;

		}
		if (c instanceof Class) {
			Class cl = (Class) c;
			Type type = new Type();
			type.type(cl.getName());
			for (java.lang.reflect.Type t : cl.getTypeParameters())
				type.generics(typeOf(t, definition) );
			return type;
		}
		if (c instanceof TypeVariable) {
			TypeVariable t = (TypeVariable) c;
			Type type = new Type();
			type.type(t.getName());
			if (definition)
				for (java.lang.reflect.Type ty : t.getBounds())
					if (ty != Object.class)
						type.upperBound(typeOf(ty));
			return type;

		}
		throw new IllegalArgumentException("unknown type " + c);

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
		this.builder = builder;

		if (guideBaseName == null || "".equals(guideBaseName)) {
			guideBaseName = builder.getSimpleName() + "Guide";
		}

		packageName = builder.getPackage().getName();

		// test ing returntype, and callable options
		if (callable && returnType.isVoid()) {
			returnType.type("ReturnType"); // force to something
		}
		if (!callable)
			returnType = new Type(); // force to void

		for (Method m : builder.getMethods()) {
			Transition t = new Transition(builder, returnType, m);
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
		Map<Integer, State> stateTypes = new HashMap<Integer, State>();
		int i = 0;
		for (S s : graph.states) {
			String stateName;
			if (s.equals(graph.in))
				stateName = guideBaseName;
			else
				stateName = guideBaseName + ++i;
			State state = new State(header,packageName, returnType, returnName, builderName, builder, stateName);
			stateTypes.put(s.id, state);
		}

		List<FileUnit> fileunits = new LinkedList<FileUnit>();

		// pars the graph from states
		for (S s : graph.states) {
			State fromState = stateTypes.get(s.id);
			System.out.println("generating " + fromState);
			CompilationUnit cu = fromState.getCompilationUnit();
			// append transitions
			for (T t : s.outs) {
				State toState = stateTypes.get(t.out.id);
				System.out.println("\tto " + toState);
				Transition trans = index.get(t.name);
				cu.methods(trans.getTransitionMethod(toState, this));
			}
			fileunits.add(cu);
		}

		fileunits.add(new ResourceUnit().packageName(packageName).name(
				"guide-graph.dot").content(graph.toString()));
		try {
			graph.graph("target/guide-graph");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

	

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("workflow: ").append(workflow).append("\n");
		sb.append("package: ").append(packageName).append("\n");
		sb.append("builderType: ").append(builder.toString() );
		
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
