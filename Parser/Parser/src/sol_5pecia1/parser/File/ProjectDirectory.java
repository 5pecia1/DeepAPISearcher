package sol_5pecia1.parser.File;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ProjectDirectory extends DFSFile {
	private Iterator<CompilableDirectory> compilableDirectories;

	public ProjectDirectory(File file) {
		super(file);
	}
	
	

	@Override
	public boolean hasNext() {
		JavaFile file = nextJavaFile();
		if (file != null) {
			file.getPackageName()
		} else {
			return false;
		}
	}

	@Override
	public CompilableDirectory next() {
		return null;
	}

}