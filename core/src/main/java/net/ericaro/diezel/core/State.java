package net.ericaro.diezel.core;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.ericaro.diezel.core.gen.CompilationUnitGen;

public class State {

	
	String header="/*\n"+DiezelHost.HEADER+"\n*/";
	String packageName;
	String name;
	boolean exit;
	List<String> superInterfaces = new LinkedList<String>();
	Set<Transition> transgen = new HashSet<Transition>();
	
	
	public State exiting(boolean exit) {
		this.exit = exit;
		return this;
	}
	
	public State header(String header) {this.header = header;return this;}
	public State packageName(String packageName) {this.packageName = packageName;return this;}
	public State name(String name) {this.name = name;return this;}
	public State superInterfaces(String... superInterface) {
		superInterfaces.addAll(Arrays.asList(superInterface));
		return this;
	}
	public State transgen(Transition... transition) {
		transgen.addAll(Arrays.asList(transition));
		return this;
	}
	

	
	public CompilationUnitGen getInterface(){
		CompilationUnitGen cg = new CompilationUnitGen().header(header).packageName(packageName);
		cg.mod("public interface").name(name).supers(superInterfaces);
		
		//append transitions
		for(Transition gen: transgen)
			cg.methods( gen.getMethodInterface() );
		return cg;
	}

	private String superInterfaces() {
		StringBuilder sb = new StringBuilder() ;
		int i=0;
		if (superInterfaces!=null) for(String e: superInterfaces) sb.append((i++)==0?"":", ").append(e);
		return sb.toString();
	}
	public String getName() {
		return name;
	}
	public boolean isExit() {
		return exit;
	}
	
	
}
