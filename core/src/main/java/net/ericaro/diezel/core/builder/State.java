package net.ericaro.diezel.core.builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class State {

	
	List<Transition> transitions = new ArrayList<Transition>();
	Set<Generic> generics = new HashSet<Generic>();
	
	String name;
	boolean isOutput = false;
	
	
	public State() {
		super();
	}

	

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	String asJavaType(){
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		if (generics!=null && generics.size() > 0 ){
			sb.append("<");
			StringUtils.join(",", generics, sb);
			sb.append(">");
		}
		return sb.toString() ;
	}





	public Set<Generic> getGenerics() {
		return generics;
	}



	public List<Transition> getTransitions() {
		return transitions;
	}



	void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}



	public boolean isOutput() {
		return isOutput;
	}
	
	
	
	
	
}
