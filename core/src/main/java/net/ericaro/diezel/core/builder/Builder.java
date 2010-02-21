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
 * Reflexive information about a builder
 * 
 * 
 * A builder is a dsl, an type, and a collection of transitions.
 * 
 * @author eric
 * 
 */
public class Builder<E> {

	transient private String packageName;
	transient private Type type;
	transient private String dsl;
	transient private Set<Transition> transitions = new HashSet<Transition>();
	transient private Type returnType = new Type(); // void by default
	transient private Graph graph;
	transient private String builderName;
	transient private String returnName;
	transient private String guideBaseName;
	transient private boolean callable;
	transient private String header;

	public String getBuilderName() {
		return builderName;
	}

	public void setBuilderName(String builderName) {
		this.builderName = builderName;
	}

	public String getReturnName() {
		return returnName;
	}

	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}

	public static <T> Builder<T> parse(Class<T> builder) {
		return new Builder<T>(builder);
	}

	public Builder(Class<E> builder) {
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

	protected void init(Class<E> builder) {
		// gets all information.
		// abuilder is a dsl, a collection of transitions.
		Workflow d = builder.getAnnotation(Workflow.class);
		dsl = d.value();
		builderName = d.builderInstance();
		callable = d.callable();
		guideBaseName = d.guideBaseName();
		header = d.header();
		returnName = d.returnGuideInstance();

		if (guideBaseName == null || "".equals(guideBaseName)) {
			guideBaseName = builder.getSimpleName() + "Guide";
		}

		type = typeOf(builder);
		List<Type> l = type.getGenerics();
		if (!l.isEmpty())
			returnType = l.get(0);
		packageName = builder.getPackage().getName();
		for (Method m : builder.getMethods()) {
			if (m
					.isAnnotationPresent(net.ericaro.diezel.annotations.Transition.class)) {
				Transition t = new Transition();
				t.parse(m);
				transitions.add(t);
			}
		}
	}

	@net.ericaro.diezel.annotations.Transition
	public void buildGraph() throws ParseException {
		GraphBuilder db = new GraphBuilder();
		RegExp.parse(dsl, db);
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
			stateType.generics(type.getGenerics());
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
				cu.methods(trans.getSimpleTransition(toType, this));
			}
			fileunits.add(cu);
		}

		fileunits.add(new ResourceUnit().packageName(packageName).name(
				"guide-graph.dot").content(graph.toString()));

		return fileunits;
	}

	
	public static void generate(Class builder, File dir) throws ParseException, IOException{
		Builder b = Builder.parse(builder);
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

		Type targetType = this.type;
		if (callable) {
			// field and constructor with return type
			c.fields(new Field().type(returnType).name(returnName));
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

		c.fields(new Field().type(targetType).name(builderName));
		return c;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("dsl: ").append(dsl).append("\n");
		sb.append("package: ").append(packageName).append("\n");
		sb.append("type: ");
		type.gen(sb);
		sb.append("\n");
		for (Transition t : transitions) {
			sb.append("transition:\n").append(t).append("\n");
		}
		return sb.toString();
	}
}
