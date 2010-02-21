package net.ericaro.diezel.core.builder;

import java.util.LinkedList;
import java.util.List;

import net.ericaro.diezel.core.gen.Method;
import net.ericaro.diezel.core.gen.Modifier;
import net.ericaro.diezel.core.gen.Type;



/** Reflexive side of a transition
 * 
 * A transition is a method, and a state configurable state return
 * 
 * @author eric
 *
 */
public class Transition {

	String name;//the method name
	String alias;//the transition short name, the name by default
	private List<Type>exceptions = new LinkedList<Type>();
	private List<Type> parameters = new LinkedList<Type>();

	Transition(){
	}
	
	/** Package method used by the BuilderParser to delegate some m=parsing to this transition.
	 * 
	 * @return true if the method is a valid transition.
	 * 
	 * @param m
	 * @return
	 */
	void parse(java.lang.reflect.Method m){
		// fully parse it and get as much as information as needed
		name= m.getName() ;
		
		if (m.isAnnotationPresent(net.ericaro.diezel.annotations.Transition.class) ){
			net.ericaro.diezel.annotations.Transition t = m.getAnnotation(net.ericaro.diezel.annotations.Transition.class);
			alias = t.value();
		}
		if (alias==null || "".equals(alias)){
			alias = name;
		}
		
		for (Class c: m.getExceptionTypes())
			exceptions.add(Builder.typeOf(c));
		
		for (Class c: m.getParameterTypes())
			parameters.add(Builder.typeOf(c));
	}
	
	
	protected Method getSimpleTransition(Type returnType, Builder builder){
		String builderName = builder.getBuilderName();
		String returnName = builder.getReturnName();
		Method m = new Method().mod(Modifier.Public).returns(returnType)
				.name(alias).param(parameters).except(exceptions);
		
		String body = builderName+"."+name+"("+Method.varcall(parameters)+");\n";
		body+="return new "+returnType.toString()+"("+builderName+","+returnName +");\n";
		m.body(body	);
		return m;
	}
	
	
	public String toString(){
		
		StringBuilder sb = new StringBuilder();
		sb.append("\t").append("name: ").append(name).append("\n");
		sb.append("\t").append("alias: ").append(alias);
		for(Type e: exceptions) 
			e.gen(sb.append("\n\tthrows: "));
		for(Type e: parameters) 
			e.gen(sb.append("\n\targs: "));
		
		sb.append("\n");
		return sb.toString();
		
	}
	
}
