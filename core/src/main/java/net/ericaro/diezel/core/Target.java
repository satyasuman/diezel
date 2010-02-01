package net.ericaro.diezel.core;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.ericaro.diezel.core.gen.CompilationUnitGen;


public class Target {

	
	private String header="/*\n"+DiezelHost.HEADER+"\n*/";
	private String packageName;
	private String name;
	private List<String> superInterfaces = new LinkedList<String>();
	private Set<Transition> transgen = new HashSet<Transition>();
	
	public Target header(String header) {this.header = header;return this;}
	public Target packageName(String packageName) {this.packageName = packageName;return this;}
	public Target name(String name) {this.name = name;return this;}
	public Target superInterfaces(String... superInterface) {
		superInterfaces.addAll(Arrays.asList(superInterface));
		return this;
	}
	public Target transgen(Transition... transition) {
		transgen.addAll(Arrays.asList(transition));
		return this;
	}
	

	
	public CompilationUnitGen getInterface(){
		
		CompilationUnitGen c = new CompilationUnitGen() ;
		c.header(header).packageName(packageName).mod("public interface").name(name).supers(superInterfaces);
		for(Transition t: transgen)
			c.methods(t.getHostMethod());
		return c;
	}

	
	public String getName() {
		return name;
	}
	
}
