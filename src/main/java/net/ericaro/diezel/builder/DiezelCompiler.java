package net.ericaro.diezel.builder;

import java.io.File;
import java.io.IOException;

import net.ericaro.diezel.builder.impl.DiezelImplementation;
import net.ericaro.diezel.builder.lang.DiezelLanguage;
import net.ericaro.diezel.builder.lang.InconsistentTypePathException;
import net.ericaro.diezel.builder.lang.State;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class DiezelCompiler {

	private static STGroup	templates	= new STGroupFile("net/ericaro/diezel/core/builder/Diezel.stg");

	public static void generate(DiezelImplementation lang, File targetDirectory) throws IOException, InconsistentTypePathException {
		toDir(targetDirectory, lang.getPackageName(), lang.getStartState().getName() + ".java", compile(lang));
	}

	public static void generate(DiezelLanguage lang, File targetDirectory) throws IOException, InconsistentTypePathException {
		// parse the graph from states
		for (State state : lang.getGraph().getVertices())
			toDir(targetDirectory, lang.getPackageName(), state.getName() + ".java", compile(lang, state));
		toDir(targetDirectory, lang.getPackageName(), "guide-graph.dot", lang.toString());
	}

	static String compile(DiezelLanguage lang, State state) {

		ST compileUnit = templates.getInstanceOf("languageStateUnit");
		compileUnit.add("state", state);
		compileUnit.add("lang", lang);
		String result = compileUnit.render();
		// System.out.println("-------------------------------------------");
		// System.out.println(result);
		return result;
	}

	static String compile(DiezelImplementation lang) {

		ST compileUnit = templates.getInstanceOf("implementationUnit");
		compileUnit.add("lang", lang);
		String result = compileUnit.render();
		// System.out.println("-------------------------------------------");
		// System.out.println(result);
		return result;
	}

	public static void toDir(File dir, String packageName, String filename, String content) throws IOException {
		File d = packageName == null ? dir : new File(dir, packageName.replace('.', '/'));
		FileUtils.printFile(new File(d, filename), content, true);
	}

}
