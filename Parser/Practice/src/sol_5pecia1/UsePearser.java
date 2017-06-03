package sol_5pecia1;

import java.io.File;

import com.github.javaparser.ast.body.MethodDeclaration;

import sol_5pecia1.parser.FileSearcher;
import sol_5pecia1.parser.functions.Compiler;
import sol_5pecia1.parser.functions.Parser;

public class UsePearser extends StartFunction{

	@Override
	MethodDeclaration start() {
		File file  = new File("C:\\Users\\sol\\DeepAPISearcher\\Parser");
		Compiler compiler = new Compiler();
		Parser parser = new Parser();
		FileSearcher fileSearcher = new FileSearcher(file, file, compiler, parser);
		fileSearcher.search();
		return null;
	}

}
