package net.ericaro.diezel.core.builder;

import net.ericaro.diezel.impl.MyGuideImpl;

public class Tester {

	
	
	public static void main(String[] args) {
		new MyGuideImpl<String>().trans_a().withC(13).build();
	}
}
