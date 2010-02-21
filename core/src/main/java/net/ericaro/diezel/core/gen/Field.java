package net.ericaro.diezel.core.gen;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/** Simple statement field with initializer.
 * 
 * @author eric
 *
 */
public class Field extends Gen {

	private List<Modifier> modifiers = new LinkedList<Modifier>(); //modifiers for this field
	private Type type; // FQN of this field.
	private String name; // field name (array accepted)
	private String initialiser; // initializer statement for this field.

	/**sets this field type
	 *  
	 * @param type
	 * @return
	 */
	public Field type(Type type) {
		this.type = type;
		return this;
	}
	
	/** sets this field name
	 * 
	 * @param name
	 * @return
	 */
	public Field name(String name) {
		this.name = name;
		return this;
	}
	
	/** sets this field initializer statement.
	 * 
	 * @param initialiser
	 * @return
	 */
	public Field initialiser(String initialiser) {
		this.initialiser = initialiser;
		return this;
	}
	
	/** defines the modifiers for this class. (including the type :( )
	 * 
	 * @param modifiers
	 * @return
	 */
	public Field mod(Modifier... modifiers) {
		return mod(Arrays.asList(modifiers));
	}
	public Field mod(List<Modifier> modifiers) {
		this.modifiers.addAll(modifiers);
		return this;
	}
	
	@Override
	protected void genImpl() {
		_(modifiers)._()._(type)._()._(name)._("=", initialiser)._(";\n");
	}

}
