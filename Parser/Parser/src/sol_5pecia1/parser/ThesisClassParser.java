package sol_5pecia1.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class ThesisClassParser {
	private final File javaPath;
	private final File javaPackagePath;
	private final File classPackagePath;
	private StringBuilder builder = new StringBuilder();

	public ThesisClassParser(String javaClassPath) throws FileNotFoundException {
		this(javaClassPath, javaClassPath); 
	}
	
	public ThesisClassParser(String javaPath, String classPackagePath) throws FileNotFoundException {
		this.javaPath = new File(javaPath);
		this.classPackagePath = new File(classPackagePath);
	
		FileInputStream in = new FileInputStream(this.javaPath);
		System.out.println("class : " + this.javaPath);
		CompilationUnit cu = JavaParser.parse(in);
	
		String packageName = cu.getPackageDeclaration().get().getNameAsString();
		
		File javaPackagePathForLoop = new File(javaPath);
		for (int i = packageName.split("[.]").length; i >= 0 ; i--) {
			javaPackagePathForLoop = javaPackagePathForLoop.getParentFile();
		}
		this.javaPackagePath = javaPackagePathForLoop;
	
		MethodVisitor methodVisitor = new MethodVisitor();
		methodVisitor.visit(cu, null);
		
		methodVisitor
		.parsedDataList
		.stream()
		.forEach(d -> {
			builder.append(d);

		});
	}
	
	public String getParsedDatas() {
		return builder.toString();
	}
	
	private class MethodVisitor extends VoidVisitorAdapter<Void> {
		LinkedList<ParsedData> parsedDataList = new LinkedList<>();
		
        @Override
        public void visit(MethodDeclaration n, Void arg) {
        	JavaParserTypeSolver javaSolver = new JavaParserTypeSolver(javaPackagePath);
        	JavaParserTypeSolver clasSolver = new JavaParserTypeSolver(classPackagePath);
        
//        	ThesisMethodParser methodParser = (javaPackagePath.equals(classPackagePath))?
//        			new ThesisMethodParser(n, javaSolver)
//        			: new ThesisMethodParser(n, javaSolver, clasSolver);
        	
        	CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
    		combinedTypeSolver.add(new ReflectionTypeSolver());
    		combinedTypeSolver.add(javaSolver);
    
    		if (javaPackagePath.equals(classPackagePath)) {
    			combinedTypeSolver.add(clasSolver);
    		}
    		
    		ThesisMethodParser methodParser = new ThesisMethodParser(n, combinedTypeSolver);
       
        	ParsedData data = new ParsedData(methodParser.getAPISequence(), methodParser.getAnnotaion());
        	
        	if (!"".equals(data.annotation) && !"".equals(data.apiSequence)) {
        		parsedDataList.add(data);
        	}
       
            super.visit(n, arg);
        }
    }
	
	private class ParsedData {
		String apiSequence = "";
		String annotation = "";
		
		public ParsedData(String apiSequence, String annotation) {
			this.apiSequence = apiSequence;
			this.annotation = annotation;
		}
	
		@Override
		public String toString() {
			return annotation + "\n" + apiSequence + "\n";
		}
	}
}
