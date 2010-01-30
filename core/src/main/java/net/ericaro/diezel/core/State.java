package net.ericaro.diezel.core;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.ericaro.diezel.core.gen.CompilationUnitGen;

public class State {

	
	private String header="";
	private String packageName;
	private String name;
	private boolean exit;
	private List<String> superInterfaces = new LinkedList<String>();
	private List<Transition> transgen = new LinkedList<Transition>();
	
	
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
	

	public void toFile(File dir) throws IOException{
		File d = packageName==null?dir:new File(dir, packageName.replace('.', '/'));
		StringBuilder sb = new StringBuilder();
		getInterface().gen(sb);
		FileUtil.copy(new File(d, name+".java")  , sb.toString(), true) ;
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
