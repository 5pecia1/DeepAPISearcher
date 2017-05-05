package sol_5pecia1;

import java.io.FileNotFoundException;

import com.github.javaparser.ast.body.MethodDeclaration;

import sol_5pecia1.parser.ThesisParser;

public class UseFileLIstSearcher extends StartFunction{

	@Override
	MethodDeclaration start() {
		ThesisParser parser;
		try {
			parser = new ThesisParser("C:\\Users\\sol\\DeepAPISearcher\\Parser");
			parser.parse();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
