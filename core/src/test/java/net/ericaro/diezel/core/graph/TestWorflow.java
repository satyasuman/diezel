package net.ericaro.diezel.core.graph;

import g2.Host;

public class TestWorflow implements Host{

	
	double buf = 0.0;
	
	public double opt1() {
		System.out.println("opt1");
		buf+=1;
		return buf;
		
	}

	public double opt2() {
		System.out.println("opt2");
		return buf+12;
	}

	
	
	public void start() {
		System.out.println("Starting");
	}

	public static void main(String[] args) {
		TestWorflow w = new TestWorflow();
		System.out.println(new g2.Parser(w).start().opt2());
		
	}
}
