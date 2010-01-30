package net.ericaro.diezel.core;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.ericaro.diezel.core.gen.CompilationUnitGen;


public class Host {

	
	private String header="";
	private String packageName;
	private String name;
	private List<String> superInterfaces = new LinkedList<String>();
	private List<Transition> transgen = new LinkedList<Transition>();
	
	public Host header(String header) {this.header = header;return this;}
	public Host packageName(String packageName) {this.packageName = packageName;return this;}
	public Host name(String name) {this.name = name;return this;}
	public Host superInterfaces(String... superInterface) {
		superInterfaces.addAll(Arrays.asList(superInterface));
		return this;
	}
	public Host transgen(Transition... transition) {
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
