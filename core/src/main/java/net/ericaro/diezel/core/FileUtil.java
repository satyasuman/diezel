package net.ericaro.diezel.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileUtil {

	public static void copy(File file, String content, boolean close)
			throws IOException {
		file.getParentFile().mkdirs();
		System.out.println(file);
		System.out.println(content);
		OutputStreamWriter w = new OutputStreamWriter(
				new FileOutputStream(file));
		w.write(content);
		if (close)
			w.close();
	}

}
