package net.ericaro.diezel.core.gen;

import java.util.Collections;

public class Cast extends Gen{

	Type type;
	
	
	
	public Cast(Type type) {
		super();
		this.type = type;
	}



	@Override
	protected void genImpl() {
		_opt("(", ") ", Collections.singletonList(type) ); 
	}

	
	
	
}
