package net.ericaro.diezel.annotations;

public enum CallType {

	/** The transition will call the builder method, and continue in the flow
	 * 
	 */
	CONTINUE,
	/** The transition will return the result of the builder method
	 * 
	 */
	EXIT,
	/** The Transition will call the builder method, and return the returnType (as previously pass in a "call" transition)
	 * 
	 */
	RETURN,
	/** The Transition will call the builder method and return another builder passing it the next state as the "return type". You can see
	 * it either as a "subroutine call", or as a start, for a context free grammar.
	 * 
	 */
	CALL
	
}
