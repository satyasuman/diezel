package net.ericaro.diezel.core.flow;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.ericaro.diezel.core.DiezelHost;
import net.ericaro.diezel.core.gen.CompilationUnitGen;


public class HostGenerator {

	
	private String header="/*\n"+DiezelHost.HEADER+"\n*/";
	private String packageName;
	private String name;
	private List<String> superInterfaces = new LinkedList<String>();
	private Set<TransitionGenerator> transgen = new HashSet<TransitionGenerator>();
	
	public HostGenerator header(String header) {this.header = header;return this;}
	public HostGenerator packageName(String packageName) {this.packageName = packageName;return this;}
	public HostGenerator name(String name) {this.name = name;return this;}
	public HostGenerator superInterfaces(String... superInterface) {
		superInterfaces.addAll(Arrays.asList(superInterface));
		return this;
	}
	public HostGenerator transgen(TransitionGenerator... transition) {
		transgen.addAll(Arrays.asList(transition));
		return this;
	}
	

	
	public CompilationUnitGen getInterface(){
		
		CompilationUnitGen c = new CompilationUnitGen() ;
		c.header(header).packageName(packageName).mod("public interface").name(name).supers(superInterfaces);
		for(TransitionGenerator t: transgen)
			c.methods(t.getHostMethod());
		return c;
	}
	
public CompilationUnitGen getAdapter(){
		
		CompilationUnitGen c = new CompilationUnitGen() ;
		c.header(header).packageName(packageName).mod("public class").name(name+"Adapter").interfaces(name);
		for(TransitionGenerator t: transgen)
			c.methods(t.getAdapterMethod());
		return c;
	}

	
	public String getName() {
		return name;
	}
	
}
