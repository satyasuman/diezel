package net.ericaro.diezel.builder.impl;

import java.util.Collection;

import net.ericaro.diezel.builder.lang.Generic;
import net.ericaro.diezel.builder.lang.TransitionInstance;

public class TransitionImplementationInstance {

	TransitionImplementation prototype;
	TransitionInstance parent;
	private StateImplementation	nextState;
	public TransitionImplementationInstance(TransitionImplementation prototype, TransitionInstance parent) {
		super();
		this.prototype = prototype;
		this.parent = parent;
	}
	public StateImplementation getNextState() {
		return nextState;
	}
	

	void setNextState(StateImplementation nextState) {
		this.nextState = nextState;
	}
	public String toString(){
		return getAlias();
	}
	
	// proto delegation methods
	
	public String getBody() {
		return prototype.getBody().add("transition", this).render();
	}
	
	
	// parent delegation methods
	
	public TransitionInstance getParent() {
		return parent;
	}
	public Collection<? extends Generic> getPush() {
		return parent.getPush();
	}
	public Collection<? extends Generic> getPull() {
		return parent.getPull();
	}
	public String getAlias() {
		return parent.getAlias();
	}
	public String getJavadoc() {
		return parent.getJavadoc();
	}
	
	public String getReturnType() {
		return parent.getReturnType();
	}
	public String getSignature() {
		return parent.getSignature();
	}
	
	
	
	
	
	
}
