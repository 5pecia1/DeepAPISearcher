package sol_5pecia1;

import java.io.File;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import sol_5pecia1.parser.ThesisMethodParser;

public class GetThesisSequence extends UseCopyLargeInIOUtils{
	@Override
	MethodDeclaration start() {
		MethodDeclaration declaration = super.start();
		
		JavaParserTypeSolver javaSolver = new JavaParserTypeSolver(new File(SOURCE_PATH));
    
    	CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
		combinedTypeSolver.add(new ReflectionTypeSolver());
		combinedTypeSolver.add(javaSolver);
		
		ThesisMethodParser methodParser = new ThesisMethodParser(declaration, combinedTypeSolver);
		System.out.println("result : ");
		System.out.println(methodParser.getAPISequence());
		return null;
	}
}
