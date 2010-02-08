package net.ericaro.diezel.core.gen;

/** static statement generator.
 * 
 * @author eric
 *
 */
public class StaticGen extends Gen {

	private String codeBlock;

	@Override
	protected void genImpl() {
		_("static {\n")._(codeBlock)._("\n}\n");
	}
	
	/** set this static statement code. Do not use trailing or leading '{' '}'
	 * 
	 * @param code
	 * @return
	 */
	public StaticGen code(String code){
		this.codeBlock = code;
		return this;
	}

}
