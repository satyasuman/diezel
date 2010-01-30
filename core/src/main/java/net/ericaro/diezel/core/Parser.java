package net.ericaro.diezel.core;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.ericaro.diezel.core.gen.CompilationUnitGen;
import net.ericaro.diezel.core.gen.ConstructorGen;
import net.ericaro.diezel.core.gen.FieldGen;


public class Parser {

	
	private String header="";
	private String packageName;
	private String name;
	private String hostType;
	private String hostname = "host";
	private List<String> superInterfaces = new LinkedList<String>();
	private List<Transition> transgen = new LinkedList<Transition>();
	
	public Parser header(String header) {this.header = header;return this;}
	public Parser packageName(String packageName) {this.packageName = packageName;return this;}
	public Parser name(String name) {this.name = name;return this;}
	public Parser host(String host) {this.hostType = host;return this;}
	public Parser superInterfaces(String... superInterface) {
		superInterfaces.addAll(Arrays.asList(superInterface));
		return this;
	}
	public Parser transgen(Transition... transition) {
		transgen.addAll(Arrays.asList(transition));
		return this;
	}
	

	public void toFile(File dir) throws IOException{
		File d = packageName==null?dir:new File(dir, packageName.replace('.', '/'));
		StringBuilder sb = new StringBuilder();
		getCompilationUnit().gen(sb);
		FileUtil.copy(new File(d, name+".java")  , sb.toString(), true) ;
		}
	
	public CompilationUnitGen getCompilationUnit(){
		CompilationUnitGen c = new CompilationUnitGen();
		c.header(header).packageName(packageName).mod("public class").name(name);
		c.interfaces(superInterfaces);
		
		ConstructorGen cons = new ConstructorGen().container(name);
		cons.mod("public").param(hostType).body("this."+hostname+" = "+cons.arg(0)+";");
		c.constructors(cons);
		
		c.fields(new FieldGen().type(hostType).name(hostname) );
		
		for(Transition gen: transgen)
		{
			c.methods( gen.host(hostname).getMethodImplementation() );
		}
		return c;
	}

	public String getName() {
		return name;
	}
	
}
