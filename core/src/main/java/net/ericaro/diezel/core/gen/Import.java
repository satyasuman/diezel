package net.ericaro.diezel.core.gen;

/** Simple import statement.
 * 
 * @author eric
 *
 */
public class Import extends Gen {

	private String importedClass; // imported type
	private boolean staticImport;

	@Override
	protected void genImpl() {
		_("import ")._(staticImport?"static ":"")._(importedClass)._(";\n");
	}
	
	/** Import static class. '*' accepted
	 * 
	 * @param name
	 * @return
	 */
	public Import importStatic(String name) {
		this.staticImport = true;
		this.importedClass= name;
		return this;
	}
	/** import normal class. '*' accepted
	 * 
	 * @param name
	 * @return
	 */
	public Import importClass(String name) {
		this.staticImport = false;
		this.importedClass= name;
		return this;
	}
	

}
