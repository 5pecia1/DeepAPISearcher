package sol_5pecia1.parser.File;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;

public class DFSFile extends File implements Iterator<DFSFile>{
	private Iterator<DFSFile> listFile;
	private DFSFile nextDFSFile = null;
	
	public DFSFile(File file) {
		super(file.toString());
	
		if (file.isFile()){
			listFile = Arrays
					.asList(this.listFiles())
					.stream()
					.map(f -> new DFSFile(f))
					.iterator();
		} 
	}

	@Override
	public boolean hasNext() {
		return hasNextChildDirectory();
	}

	@Override
	public DFSFile next() {
		return nextChildDirectory();
	}

	public boolean hasNextChildDirectory() {
		return listFile != null && listFile.hasNext();
	}
	
	public DFSFile nextChildDirectory() {
		return listFile.next();
	}
	
	public boolean isJavaFile() {
		return this.isFile() && Pattern.matches(".+[.]java", this.getName());
	}

	/**
	 * 
	 * @return null - 다음 자바 파일이 없음
	 */
	public JavaFile nextJavaFile() {
		JavaFile returnFile = null;
		
		if (isJavaFile()) {
			JavaFile javaFile = JavaFile.factory(this);
			returnFile = javaFile;
		} else if (nextDFSFile != null) {
			DFSFile resultDFSFile = (DFSFile) nextDFSFile.nextJavaFile();
			
			if (resultDFSFile == null) {
				if (nextDFSFile.hasNextChildDirectory()) {
					returnFile = nextDFSFile.next().nextJavaFile();
					
				} else if (hasNextChildDirectory()) {
					nextDFSFile = nextChildDirectory();
					returnFile = nextDFSFile.nextJavaFile();
				}
			} else {
				returnFile = (JavaFile) resultDFSFile;
			}
		} else if (this.exists() && this.isDirectory()){ // new child directory
			if (hasNextChildDirectory()) {
				nextDFSFile = nextChildDirectory();
				returnFile = nextDFSFile.nextJavaFile();	
			} 
		}
		
		return returnFile;
	}
}
