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
public class ResourceUnit extends Gen implements FileUnit{

	private String packageName; // package declaration
	private String resourceName; // package declaration
	private String name;
	private String content;
	
	
	

	/** defines the fully qualified package name (null does not define any package)
	 * 
	 * @param packageName
	 * @return
	 */
	public ResourceUnit packageName(String packageName) {
		this.packageName = packageName;
		return this;
	}

	/** define this resource name.
	 * 
	 * @param name
	 * @return
	 */
	public ResourceUnit name(String name) {
		this.name = name;
		return this;
	}
	/** define this resource content.
	 * 
	 * @param name
	 * @return
	 */
	public ResourceUnit content(String content) {
		this.content= content;
		return this;
	}
		
	

	@Override
	protected void genImpl() {
		_(content);
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
		FileUtil.printFile(new File(d, name ), sb.toString(), true);
	}

}
