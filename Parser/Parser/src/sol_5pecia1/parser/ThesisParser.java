package sol_5pecia1.parser;

import java.io.FileNotFoundException;

public class ThesisParser {
	private FileSearcher fileSearcher;
	
	public ThesisParser(String path) throws FileNotFoundException {
		fileSearcher = new FileSearcher(path);
	}
	public void parse() {
		System.out.println(fileSearcher.nextCompilablePackage());
		System.out.println(fileSearcher.nextCompilablePackage());
		System.out.println(fileSearcher.nextCompilablePackage());
	}
}
