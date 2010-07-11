package net.ericaro.diezel.core.builder;

import java.util.LinkedList;
import java.util.List;

import net.ericaro.diezel.annotations.CallType;
import net.ericaro.diezel.core.gen.Cast;
import net.ericaro.diezel.core.gen.Method;
import net.ericaro.diezel.core.gen.Modifier;
import net.ericaro.diezel.core.gen.Type;



/** Represent a transition in the user defined workflow.
 * 
 * A Transition is constructed from every method of the target builder.
 * 
 * @author eric
 *
 */
public class Transition {

	/** The actual real real method name
	 * 
	 */
	String name;
	/** the transition short name, it is used in the workflow definition, and as the method name for the guides.
	 *  if not explicitly set by the annotation we use name.
	 */
	String alias;
	/** List of exception declared by the method, and converted into Type (for easier code manipulation and generation)
	 */
	private List<Type>exceptions = new LinkedList<Type>();
	/** the list of the method parameters. converted into Type for easier manipulation and generation
	 */
	private List<Type> parameters = new LinkedList<Type>();

	private List<Type> typeParameters = new LinkedList<Type>();
	/** the method return type
	 */
	private Type methodReturnType;
	/** the call type selected by the user.
	 */
	private CallType callType;

	private Type builderType;
	private Type returnType;
	private Class builder;
	
	

	
	public Transition(Class builder, Type returnType, java.lang.reflect.Method m) {
		super();
		this.builder = builder;
		this.builderType = DiezelGenerator.typeOf(builder);
		this.returnType = returnType;
		parse(m);
	}

	/** Package method used by the BuilderParser to delegate every method parsing
	 * @param m the actual method to be analyzed
	 */
	void parse(java.lang.reflect.Method m){
		// fully parse it and get as much as information as needed
		// read parameters always available
		name= m.getName() ; 
		
		methodReturnType = DiezelGenerator.typeOf(m.getGenericReturnType());
		
		callType = CallType.CONTINUE ; // set the default value
		
		if (m.isAnnotationPresent(net.ericaro.diezel.annotations.Transition.class) ){
			net.ericaro.diezel.annotations.Transition t = m.getAnnotation(net.ericaro.diezel.annotations.Transition.class);
			alias = t.value(); //read the annotation values
			callType = t.callType();
		}
		//if the alias was not user define use name instead
		if (alias==null || "".equals(alias))
			alias = name;
		
		// parse exceptions, and parameter types
		for (java.lang.reflect.Type c: m.getGenericExceptionTypes())
			exceptions.add(DiezelGenerator.typeOf(c));
		
		for (java.lang.reflect.Type c: m.getTypeParameters() )
			typeParameters.add(DiezelGenerator.typeOfDefinition(c));
		
		for (java.lang.reflect.Type c: m.getGenericParameterTypes())
			parameters.add(DiezelGenerator.typeOf(c));
		
		// missing catching parameters like in : public <T> toString(T t)
		
	}
	
	/** Return the reflexive/generative Method object that represent this transition for a given guide, and a given "nextState"
	 * 
	 * @param nextGuideType the next guide type
	 * @param diezelGenerator the parent DiezelGenerator, used for global information on the workflow
	 * @return
	 */
	public Method getTransitionMethod(State nextGuideState, DiezelGenerator diezelGenerator){
		switch(callType){
		case CONTINUE: return getContinueTransition(nextGuideState, diezelGenerator);
		case RETURN: return getReturnTransition(nextGuideState, diezelGenerator);
		case EXIT: return getExitTransition(nextGuideState, diezelGenerator);
		case CALL: return getCallTransition(nextGuideState, diezelGenerator);
		default: throw new RuntimeException("Unknown callType type"+ callType);
		}
	}
	
	/** Generates a continue transition:
	 * 
	 * call the builder
	 * then return the nextstate
	 * 
	 * @param nextGuideType
	 * @param diezelGenerator
	 * @return
	 */
	protected Method getContinueTransition(State nextGuideState, DiezelGenerator diezelGenerator){
		String builderName = diezelGenerator.getBuilderName();
		String returnName = diezelGenerator.getReturnName();
		Method m = new Method()
		.mod(Modifier.Public)
		.captures(typeParameters)
		.returns(nextGuideState.getRuntimeType())
		.name(alias).param(parameters).except(exceptions);
		
		String body = builderName+"."+name+"("+Method.varcall(parameters)+");\n";
		body+="return new "+nextGuideState.getRuntimeType().toString()+"("
				+new Cast(builderType)+ builderName+","
				+new Cast(returnType)+ returnName +");\n";
		m.body(body	);
		return m;
	}
	/**Generates a return transition.
	 * 
	 * returns the builder method's result. 
	 * 
	 * @param nextGuideType
	 * @param diezelGenerator
	 * @return
	 */
	protected Method getExitTransition(State nextGuideState, DiezelGenerator diezelGenerator){
		String builderName = diezelGenerator.getBuilderName();
		Method m = new Method()
		.mod(Modifier.Public)
		.captures(typeParameters)
		.returns(methodReturnType )
		.name(alias).param(parameters).except(exceptions);
		
		String body = (methodReturnType.isVoid()?"":"return ")+builderName+"."+name+"("+Method.varcall(parameters)+");\n";
		m.body(body	);
		return m;
	}
	/** Generates an exit transition.
	 * 
	 * calls the transition
	 * return the returnType instead of the next transition
	 * 
	 * @param returnType
	 * @param diezelGenerator
	 * @return
	 */
	protected Method getReturnTransition(State returnState, DiezelGenerator diezelGenerator){
		Type returnType = returnState.getRuntimeType();
		assert diezelGenerator.isCallable(): "cannot use CallType.EXIT on a workflow that is not callable. set @Workflow(callable=true) ";
		String returnName = diezelGenerator.getReturnName();
		Method m = new Method()
		.mod(Modifier.Public)
		.captures(typeParameters)
		.returns(diezelGenerator.getReturnType())
		.name(alias).param(parameters).except(exceptions);
		
		String body = "return "+returnName+";\n";
		m.body(body	);
		return m;
	}
	/** Generates a Call transition.
	 * 
	 * special case:
	 * 
	 * the declared workflow method does not have the same signature as the actual method: 
	 * the first parameter is removed
	 * 
	 * call the method passing the next state as first parameter.
	 * return the result of the method call.
	 * 
	 * @param nextGuideType
	 * @param diezelGenerator
	 * @return
	 */
	protected Method getCallTransition(State nextGuideState, DiezelGenerator diezelGenerator){
		Type nextGuideType = nextGuideState.getRuntimeType();
		String builderName = diezelGenerator.getBuilderName();
		
		LinkedList<Type> p = new LinkedList<Type>(parameters);
		p.remove(0);// remove the first param: it's always the one that is used to pass the return type
		//later I'll remove the first catch too
		Method m = new Method()
		.mod(Modifier.Public)
		.captures(typeParameters)
		.returns(methodReturnType )
		.name(alias).param(p).except(exceptions);
		
		// build the next state "as usual"
		String returnName = diezelGenerator.getReturnName();
		String returnStateName = "new "+nextGuideType.toString()+"("+builderName+","+returnName +")";
		// now change the first arg to the actual return type
		p.add(0, nextGuideType); // this MUST be the actual signature of the builder method
		String body = "return "+builderName+"."+name+"("+offset_varcall(p, returnStateName )+");\n";
		
		
		m.body(body	);
		return m;
	}
	
	/** The first parameter was added to the list, so index must remain as previously named
	 * 
	 * @param p
	 * @return
	 */
	private String offset_varcall(List<Type> p, String name){
		StringBuilder sb = new StringBuilder();
		int i = 0;
		sb.append(name);
		if (p != null)
			for (Type v : p)
				{
				if (i==0) {
					i++;
					continue;// skip the first param
				}
				sb.append(", ").append("arg" + (i++ -1));
				}
		return sb.toString();
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
