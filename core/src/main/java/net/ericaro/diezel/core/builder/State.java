package net.ericaro.diezel.core.builder;

import net.ericaro.diezel.core.gen.CompilationUnit;
import net.ericaro.diezel.core.gen.Constructor;
import net.ericaro.diezel.core.gen.Field;
import net.ericaro.diezel.core.gen.Modifier;
import net.ericaro.diezel.core.gen.Type;

/**
 * A representation of a state in the EDSL.
 * 
 * @author eric
 * 
 */
public class State {

	transient Type declarationType;// the type used to define this state
	transient Type runtimeType;// the type used to call this type (for
								// constructors etc.)

	public State(String header, String packageName, Type returnType,
			String returnName, String builderName, Class builder, String name) {
		super();
		this.header = header;
		this.packageName = packageName;
		this.returnType = returnType;
		this.returnName = returnName;
		this.builderName = builderName;
		this.builder = builder;
		this.name = name;
		this.declarationType = DiezelGenerator.typeOfDefinition(builder);
		this.runtimeType = DiezelGenerator.typeOf(builder);
		declarationType.type(name);
		runtimeType.type(name);
		if (returnType != null && !returnType.isVoid()) {
			declarationType.getGenerics().add(0, returnType); // alittle trick
																// to prepend
																// the return
																// type
			runtimeType.getGenerics().add(0, returnType);
		}

	}

	Type returnType;
	String packageName;
	String header;
	String returnName;
	private String builderName;
	Class builder;
	String name;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getReturnName() {
		return returnName;
	}

	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}

	public String getBuilderName() {
		return builderName;
	}

	public void setBuilderName(String builderName) {
		this.builderName = builderName;
	}

	public Type getReturnType() {
		return returnType;
	}

	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}

	public Class getBuilder() {
		return builder;
	}

	public void setBuilder(Class builder) {
		this.builder = builder;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getDeclarationType() {
		return declarationType;
	}

	public void setDeclarationType(Type declarationType) {
		this.declarationType = declarationType;
	}

	public Type getRuntimeType() {
		return runtimeType;
	}

	public void setRuntimeType(Type runtimeType) {
		this.runtimeType = runtimeType;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return declarationType.toString();
	}

	public CompilationUnit getCompilationUnit() {
		CompilationUnit c = new CompilationUnit().header(header).packageName(
				packageName);
		c.mod(Modifier.Public).asClass().type(getDeclarationType());

		Type targetType = DiezelGenerator.typeOf(builder);
		if (returnType != null && !returnType.isVoid()) {
			// field and constructor with return builderType
			c.fields(new Field().mod(Modifier.Private).type(returnType).name(
					returnName));
			Constructor cons = new Constructor().container(getRuntimeType()
					.getName());
			cons.mod(Modifier.Public).param(targetType, returnType).body(
					"this." + builderName + " = " + cons.arg(0) + ";" + "this."
							+ returnName + " = " + cons.arg(1) + ";");
			c.constructors(cons);
		}

		Constructor cons = new Constructor().container(getRuntimeType()
				.getName());
		cons.mod(Modifier.Public).param(targetType).body(
				"this." + builderName + " = " + cons.arg(0) + ";");
		c.constructors(cons);

		c.fields(new Field().mod(Modifier.Private).type(targetType).name(
				builderName));
		return c;
	}

}
