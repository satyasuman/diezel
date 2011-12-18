package net.ericaro.diezel.plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.DirectoryScanner;

/**
 * Scans source directories for Diezel's files.
 */
class DiezelDirectoryScanner {

	/**
	 * The directory scanner used to scan the source directory for files.
	 */
	private DirectoryScanner scanner;

	/**
	 * The absolute path to the output directory used to detect stale target files by timestamp checking, may be <code>null</code> if no stale detection should
	 * be performed.
	 */
	private File outputDirectory;

	/**
	 * The granularity in milliseconds of the last modification date for testing whether a grammar file needs
	 * recompilation because its corresponding target file is stale.
	 */
	private int staleMillis;

	/**
	 * A set of Files containing the included grammar files, must never be <code>null</code>.
	 */
	private List includedGrammars;

	/**
	 * Creates a new grammar directory scanner.
	 */
	public DiezelDirectoryScanner() {
		this.scanner = new DirectoryScanner();
		this.scanner.setFollowSymlinks(true);
		this.includedGrammars = new ArrayList();
	}

	/**
	 * Sets the absolute path to the source directory to scan for grammar files. This directory must exist or the
	 * scanner will report an error.
	 * 
	 * @param directory
	 *            The absolute path to the source directory to scan, must not be <code>null</code>.
	 */
	public void setSourceDirectory(File directory) {
		if (!directory.isAbsolute()) {
			throw new IllegalArgumentException("source directory is not absolute: " + directory);
		}
		this.scanner.setBasedir(directory);
	}

	/**
	 * Sets the Ant-like inclusion patterns.
	 * 
	 * @param includes
	 *            The set of Ant-like inclusion patterns, may be <code>null</code> to include all files.
	 */
	public void setIncludes(String[] includes) {
		this.scanner.setIncludes(includes);
	}

	/**
	 * Sets the Ant-like exclusion patterns.
	 * 
	 * @param excludes
	 *            The set of Ant-like exclusion patterns, may be <code>null</code> to exclude no files.
	 */
	public void setExcludes(String[] excludes) {
		this.scanner.setExcludes(excludes);
		this.scanner.addDefaultExcludes();
	}

	/**
	 * Sets the absolute path to the output directory used to detect stale target files.
	 * 
	 * @param directory
	 *            The absolute path to the output directory used to detect stale target files by timestamp
	 *            checking, may be <code>null</code> if no stale detection should be performed.
	 */
	public void setOutputDirectory(File directory) {
		if (directory != null && !directory.isAbsolute()) {
			throw new IllegalArgumentException("output directory is not absolute: " + directory);
		}
		this.outputDirectory = directory;
	}

	/**
	 * Sets the granularity in milliseconds of the last modification date for stale file detection.
	 * 
	 * @param milliseconds
	 *            The granularity in milliseconds of the last modification date for testing whether a grammar
	 *            file needs recompilation because its corresponding target file is stale.
	 */
	public void setStaleMillis(int milliseconds) {
		this.staleMillis = milliseconds;
	}

	/**
	 * Scans the source directory for grammar files that match at least one inclusion pattern but no exclusion pattern,
	 * optionally performing timestamp checking to exclude grammars whose corresponding parser files are up to date.
	 * 
	 * @throws IOException
	 *             If a grammar file could not be analyzed for metadata.
	 */
	public void scan() throws IOException {
		this.includedGrammars.clear();
		this.scanner.scan();

		String[] files = this.scanner.getIncludedFiles();
		for (int i = 0; i < files.length; i++) {
			String path = files[i];
			File sourceFile = new File(scanner.getBasedir(), path); 
			//deactivated to force the generation, I'll do it later
//			if (this.outputDirectory != null)
//				for (File targetFile : getTargetFiles(this.outputDirectory, sourceFile)) {
//					if (!targetFile.exists() || targetFile.lastModified() + this.staleMillis < sourceFile.lastModified()) {
//						this.includedGrammars.add(sourceFile);
//						break;
//					}
//				}
//			else
				this.includedGrammars.add(sourceFile);

		}
	}

	/**
	 * Determines the output files corresponding to the specified grammar file.
	 * 
	 * @param targetDirectory
	 *            The absolute path to the output directory for the target files, must not be <code>null</code>.
	 * @param diezelFile
	 *            The path to the grammar file, relative to the scanned source directory, must not be <code>null</code>.
	 * @param grammarInfo
	 *            The grammar info describing the grammar file, must not be <code>null</code>
	 * @return A file array with target files, never <code>null</code>.
	 */
//	protected File[] getTargetFiles(File targetDirectory, File diezelFile) {
//		File parserFile = new File(targetDirectory); // TODO parse the source anyway to find at least the target file
//		return new File[] { parserFile };
//	}

	/**
	 * Gets the grammar files that were included by the scanner during the last invocation of {@link #scan()}.
	 * 
	 * @return An array of grammar infos describing the included grammar files, will be empty if no files were included
	 *         but is never <code>null</code>.
	 */
	public File[] getIncludedDiezels() {
		return (File[]) this.includedGrammars.toArray(new File[this.includedGrammars.size()]);
	}

}
