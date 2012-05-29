package net.ericaro.diezel.builder.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.ericaro.diezel.builder.StringUtils;

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
			StringUtils.join(",", generics, sb);
			sb.append(">");
		}
		return sb.toString() ;
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
	
	/** to dot graphiz protocol
	 * 
	 * @return
	 */
	public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append(getName()).append("[shape=component,label=\"" + asJavaType() + "\"]");
			return sb.toString();
	}
	
	public Collection<TransitionInstance> getTransitions(){
		return grex.getGraph().getOutEdges(this);
	}

	
	
	void setName(String name) {
		this.name = name;
	}

	void init(DiezelGrex grex, String name) {
		this.grex = grex;
	}

	
	
}
