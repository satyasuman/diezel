package net.ericaro.diezel.core.gen;


/** Statement to generate a constructor.
 * 
 * @author eric
 *
 */
public class ConstructorGen extends MethodGen {

	String enclosingClass; // the class this constructor belongs to.
	
	/** tells this constructor which class it is.
	 * 
	 * @param containingClass
	 * @return
	 */
	public ConstructorGen container(String containingClass) {
		this.enclosingClass = containingClass;
		return this;
	}
	
	/** Override returnType in method gen to ignore return type.
	 * 
	 */
	@Override
	protected void returnType() {
		return; //ignore return type for construct
	}
	/** Overrides method name to for it to fit the enclosing class.
	 * 
	 */
	@Override
	protected void methodName() {
		_(enclosingClass);
	}
	
	

	
	
	
}
