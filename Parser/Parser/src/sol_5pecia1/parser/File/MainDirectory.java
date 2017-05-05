package sol_5pecia1.parser.File;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

public class MainDirectory extends File implements Iterator<ProjectDirectory>{
	private final Iterator<ProjectDirectory> projectDirectories;
	
	public MainDirectory(String pathname) {
		super(pathname);
		projectDirectories = Arrays
				.asList(this.listFiles())
				.stream()
				.map(f -> new ProjectDirectory(f))
				.iterator();
	}

	@Override
	public boolean hasNext() {
		return hasNextProjectDirctory();
	}

	@Override
	public ProjectDirectory next() {
		return nextProjectDirecotry();
	}
	
	public boolean hasNextProjectDirctory() {
		return projectDirectories.hasNext();
	}

	public ProjectDirectory nextProjectDirecotry() {
		return projectDirectories.next();
	}
}