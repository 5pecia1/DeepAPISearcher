package sol_5pecia1.parser;

import java.io.File;
import java.util.regex.Pattern;

import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;

import sol_5pecia1.parser.file.DataSaver;
import sol_5pecia1.parser.functions.Parser;

/**
 * 파일을 찾고 결과값을 비동기적으로 넘겨준다
 * 
 * @author sol
 *
 */
public class FileSearcher {
	private final File rootFile;
	private final File saveRoot;
	private final sol_5pecia1.parser.functions.Compiler compiler;
	private final Parser parser;

	public FileSearcher(File rootFile, File saveRoot, sol_5pecia1.parser.functions.Compiler compiler, Parser parser) {
		this.rootFile = rootFile;
		this.compiler = compiler;
		this.parser = parser;
		this.saveRoot = saveRoot;
	}

	public void search() {
		DataSaver.setRootFile(saveRoot);
		searchRoot(rootFile);
		DataSaver.flushFile();
	}
	private void search(File file) {
		if (file.isFile() && Pattern.matches(".+[.]java", file.getName())) {
			System.gc();
			compiler.compile(file);
			System.gc();
			parser.run(file);
			System.gc();
		} else if (file.isDirectory()){
			File[] files = file.listFiles();
		
			if (files != null) {
				for(File f : files) {
					search(f);
				}
			}
		}
	}
	
	/**
	 * 무조건 directory를 넣어야 한다.
	 * @param file
	 */
	private void searchRoot(File file) {
		File[] files = file.listFiles();
		
		if (files != null) {
			for(File f : files) {
				search(f);
			}
		}
	}
}
