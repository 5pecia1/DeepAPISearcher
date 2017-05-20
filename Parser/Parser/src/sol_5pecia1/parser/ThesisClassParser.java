package sol_5pecia1.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

/**
 * 해당 클래스의 내용을 논문 형식으로 parsing한다.
 * 
 * @author sol
 *
 */
public class ThesisClassParser {
	private final File javaPath;
	private final File classPackagePath;
	private File javaPackagePath;
	private StringBuilder builder = new StringBuilder();

	public ThesisClassParser(String javaClassPath) throws FileNotFoundException {
		this(javaClassPath, javaClassPath); 
	}
	
	public ThesisClassParser(String javaPath, String classPackagePath) throws FileNotFoundException {
		this.javaPath = new File(javaPath);
		this.classPackagePath = new File(classPackagePath);
	
		FileInputStream in = new FileInputStream(this.javaPath);
		System.out.println("class : " + this.javaPath);
		
		try {
		CompilationUnit cu = JavaParser.parse(in);
	
		String packageName = "";
		try {
			 packageName= cu.getPackageDeclaration().get().getNameAsString();
		} catch (NoSuchElementException nsee) {
			System.out.println(nsee);
		}
		
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
		.filter(d -> d.isValid)
		.forEach(d -> {
			builder.append(d);

		});
		
		methodVisitor.parsedDataList.clear();
		} catch (ParseProblemException ppe) {
			System.out.println(ppe);
		}
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
    	
    		JavaParserFacade.clearInstances();
    		ThesisMethodParser methodParser = new ThesisMethodParser(n, combinedTypeSolver);
       
        	ParsedData data = new ParsedData(methodParser.getAPISequence(), methodParser.getAnnotaion());
        	
        	if (!"".equals(data.annotation) && !"".equals(data.apiSequence)) {
        		parsedDataList.add(data);
        	}
       
            super.visit(n, arg);
        }
    }
	
	private class ParsedData {
		boolean isValid = false;
		String apiSequence = "";
		String annotation = "";
		
		public ParsedData(String apiSequence, String annotation) {
			if (apiSequence != null && annotation != null
					&& !"".equals(apiSequence) && !"".equals(annotation)) {  
				this.apiSequence = apiSequence;
				this.annotation = annotation;
				isValid = true;
			}
		}
	
		@Override
		public String toString() {
			return annotation + "\n" + apiSequence + "\n";
		}
	}
}
