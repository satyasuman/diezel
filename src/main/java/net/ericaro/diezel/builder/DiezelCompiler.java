package net.ericaro.diezel.builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import net.ericaro.diezel.builder.impl.DiezelImplementation;
import net.ericaro.diezel.builder.lang.DiezelLanguage;
import net.ericaro.diezel.builder.lang.InconsistentTypePathException;
import net.ericaro.diezel.builder.lang.State;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

/** Code generate for either a {@link DiezelLanguage} or a {@link DiezelImplementation}.
 * 
 * It is based on StringTemplate. Hence the signature of both objects should not change (without changing the stringtemplate
 * 
 * @author eric
 *
 */
public class DiezelCompiler {

	private static STGroup	templates	= new STGroupFile("net/ericaro/diezel/core/builder/Diezel.stg");
	
	/** Generates a {@link DiezelImplementation} into the target directory. It takes care of everything
	 * 
	 * @param lang
	 * @param targetDirectory
	 * @throws IOException
	 * @throws InconsistentTypePathException
	 */
	public static void generate(DiezelImplementation lang, File targetDirectory) throws IOException, InconsistentTypePathException {
		toDir(targetDirectory, lang.getPackageName(), lang.getStartState().getName() + ".java", compile(lang));
	}

	public static void generate(DiezelLanguage lang, File targetDirectory) throws IOException, InconsistentTypePathException {
		// parse the graph from states
		for (State state : lang.getGraph().getVertices())
			toDir(targetDirectory, lang.getPackageName(), state.getName() + ".java", compile(lang, state));
		//toDir(targetDirectory, lang.getPackageName(), "guide-graph.dot", lang.toString());
	}
	
	/** Compile a {@link DiezelLanguage} into it string source.
	 * 
	 * @param lang
	 * @param state
	 * @return
	 */
	static String compile(DiezelLanguage lang, State state) {

		ST compileUnit = templates.getInstanceOf("languageStateUnit");
		compileUnit.add("state", state);
		compileUnit.add("lang", lang);
		String result = compileUnit.render();
		// System.out.println("-------------------------------------------");
		// System.out.println(result);
		return result;
	}

	/** Compile a {@link DiezelImplementation} into it string source.
	 * 
	 * @param lang
	 * @return
	 */
	static String compile(DiezelImplementation lang) {

		ST compileUnit = templates.getInstanceOf("implementationUnit");
		compileUnit.add("lang", lang);
		String result = compileUnit.render();
		return result;
	}
	
	/** Utility to generate a java file into its target directory, in particular it handles the packages.
	 * 
	 * @param dir
	 * @param packageName java package name
	 * @param filename
	 * @param content
	 * @throws IOException
	 */
	public static void toDir(File dir, String packageName, String filename, String content) throws IOException {
		File d = packageName == null ? dir : new File(dir, packageName.replace('.', '/'));
		printFile(new File(d, filename), content);
	}
	
	/** Write a string content into a real file.
	 * 
	 * @param file
	 * @param content
	 * @throws IOException
	 */
	public static void printFile(File file, String content)
			throws IOException {
		file.getParentFile().mkdirs();
		OutputStreamWriter w = new OutputStreamWriter(
				new FileOutputStream(file));
		w.write(content);
		w.close();
	}
}
