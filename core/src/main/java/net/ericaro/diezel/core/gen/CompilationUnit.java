package net.ericaro.diezel.core.gen;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;



/** A single top level compilation unit.
 * 
 * @author eric
 *
 */
public class CompilationUnit extends Class implements FileUnit{

	private String header; // header for this file
	private String packageName; // package declaration
	private List<Import> imports; // list of imports.
	

	
	/**defines the header for this compilation unit (csv tags, or ascii art, or license)
	 * 
	 * @param header
	 * @return
	 */
	public CompilationUnit header(String header) {
		this.header = header;
		return this;
	}
	
	
	

	/** defines the fully qualified package name (null does not define any package)
	 * 
	 * @param packageName
	 * @return
	 */
	public CompilationUnit packageName(String packageName) {
		this.packageName = packageName;
		return this;
	}

	/** varargs of imports
	 * 
	 * @param imports
	 * @return
	 */
	public CompilationUnit imports(Import... imports) {
		this.imports.addAll(Arrays.asList(imports));
		return this;
	}

	/** list of imports.
	 * 
	 * @param imports
	 * @return
	 */
	public CompilationUnit imports(List<Import> imports) {
		this.imports.addAll(imports);
		return this;
	}

	/** does not delegate anything.
	 * 
	 */
	@Override
	protected void genImpl() {
		// a class structure is
		// compilation Unit
		// header
		// package definition
		// import
		_("/*\n")._(header)._("\n*/")._("package ", packageName)._(packageName == null ? "" : ";")
				._("\n");
		_(imports);
		super.genImpl();
	}

	/** generates this compilation unit in the appropriate directory/file using "dir" as the default package directory.
	 * 
	 * @param dir
	 * @throws IOException
	 */
	public void toDir(File dir) throws IOException {
		File d = packageName == null ? dir : new File(dir, packageName.replace(
				'.', '/'));
		StringBuilder sb = new StringBuilder();
		gen(sb);
		FileUtil.printFile(new File(d, type.getName() + ".java"), sb.toString(), true);
	}

}
