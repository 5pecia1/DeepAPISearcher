package sol_5pecia1.parser.File;

import java.io.File;
import java.util.Iterator;
import java.util.regex.Pattern;

public class CompilableDirectory extends DFSFile {
	private JavaFile nextFile;

	public CompilableDirectory(File file) {
		super(file);
	}

	@Override
	public boolean hasNext() {
		nextFile = nextJavaFile();
		return nextFile != null;
	}

	@Override
	public JavaFile next() {
		return nextFile;
	}
}
