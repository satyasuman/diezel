package net.ericaro.diezel.annotations;

public enum CallType {

	/** The transition will call the builder method, and continue in the flow
	 * 
	 */
	CONTINUE,
	/** The transition will return the result of the builder method
	 * 
	 */
	RETURN,
	/** The Transition will call the builder method, and return the returnType (this mark the end of the workflow)
	 * 
	 */
	EXIT,
	/** The Transition will call the builder method passing it the next state in the workflow, and will return
	 * the result of the builder method. Used to start, an embedded flow, and provide support for
	 * context free language.
	 * 
	 */
	CALL
	
}
