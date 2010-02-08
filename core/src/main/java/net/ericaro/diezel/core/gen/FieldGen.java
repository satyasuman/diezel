package net.ericaro.diezel.core.gen;

/** Simple statement field with initializer.
 * 
 * @author eric
 *
 */
public class FieldGen extends Gen {

	private String modifiers; //modifiers for this field
	private String type; // FQN of this field.
	private String name; // field name (array accepted)
	private String initialiser; // initializer statement for this field.

	/**sets this field type
	 *  
	 * @param type
	 * @return
	 */
	public FieldGen type(String type) {
		this.type = type;
		return this;
	}
	
	/** sets this field name
	 * 
	 * @param name
	 * @return
	 */
	public FieldGen name(String name) {
		this.name = name;
		return this;
	}
	
	/** sets this field initializer statement.
	 * 
	 * @param initialiser
	 * @return
	 */
	public FieldGen initialiser(String initialiser) {
		this.initialiser = initialiser;
		return this;
	}
	
	/** set this field modifiers.
	 * 
	 * @param modifiers
	 * @return
	 */
	public FieldGen mod(String modifiers) {
		this.modifiers = modifiers;
		return this;
	}
	
	@Override
	protected void genImpl() {
		_(modifiers)._()._(type)._()._(name)._("=", initialiser)._(";\n");
	}

}
