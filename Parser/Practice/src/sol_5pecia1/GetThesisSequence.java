package sol_5pecia1;

import java.io.File;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;

import sol_5pecia1.parser.ThesisMethodParser;

public class GetThesisSequence extends UseCopyLargeInIOUtils{
	@Override
	MethodDeclaration start() {
		MethodDeclaration declaration = super.start();
		
		ThesisMethodParser methodParser = new ThesisMethodParser();
		methodParser.visit(declaration, new JavaParserTypeSolver(new File(SOURCE_PATH)));
		
		System.out.println("result : ");
		System.out.println(methodParser.getAPISequence());
		return null;
	}
}
