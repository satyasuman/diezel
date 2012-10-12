package net.ericaro.diezel.builder.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** represent a state in the EDSL, i.e. an interface with methods
 * 
 * @author eric
 *
 */
public class State {

	
	String name;
	List<Generic> generics = new ArrayList<Generic>();
	private DiezelGrex	grex;
	
	State(){}
	
	public String getName() {
		return name;
	}
	
	String asJavaType(){
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		if (generics!=null && generics.size() > 0 ){
			sb.append("<");
			join(",", generics, sb);
			sb.append(">");
		}
		return sb.toString() ;
	}
	
	/**
	 * mimics join in python.
	 * 
	 * @param delimiter
	 * @param iterable
	 * @param builder
	 * @return
	 */
	public static StringBuilder join(String delimiter, Iterable<?> iterable, StringBuilder builder) {

		boolean first = true;
		for (Object argument : iterable) {
			if (first)
				first = false;
			else
				builder.append(delimiter);
			builder.append(argument);
		}
		return builder;

	}

	public List<Generic> getGenerics() {
		return generics;
	}

	

	public boolean isInput() {
		return grex.getStartState() == this;
	}

	public boolean isOutput() {
		return grex.isOutput(this);
	}
	
	public String toString(){
		return getName();
	}
	
	public Collection<TransitionInstance> getTransitions(){
		return grex.getGraph().getOutEdges(this);
	}

	
	
	void setName(String name) {
		this.name = name;
	}

	void init(DiezelGrex grex, String name) {
		this.grex = grex;
		this.name = name ;
	}

	
	
}
