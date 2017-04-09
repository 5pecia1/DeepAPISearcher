package sol_5pecia1.parser;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.typesystem.Type;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import sol_5pecia1.parser.methodinformation.ThesisSequenceVisitorImplements;

/**
 * 하나의 메서드를 논문형식으로 Parsing한 String 및 List를 전달한다.
 * 
 * @author sol
 *
 */
public class ThesisMethodParser {
	private final static String OBJECT_CREATE = "new";
	private String apiSequence = "";
	private ThesisSequenceVisitorImplements thesisSequenceVisitorImplements = new ThesisSequenceVisitorImplements();

	public void visit(MethodDeclaration node, JavaParserTypeSolver sourceClassSolver) {
		visit(node, sourceClassSolver, null);
	}

	//TODO rename all variable
	//TODO 내부 메서드 값 처리하기.
	public void visit(MethodDeclaration node, JavaParserTypeSolver sourceSolver,
			JavaParserTypeSolver classSolver) {
		thesisSequenceVisitorImplements.visit(node, null);
		CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
		combinedTypeSolver.add(new ReflectionTypeSolver());
		combinedTypeSolver.add(sourceSolver);
		if (classSolver != null){
			combinedTypeSolver.add(classSolver);
		}
		
		StringBuilder builder = new StringBuilder();
		for(Node visitedNode : thesisSequenceVisitorImplements.getNodeListSequenceAboutThesis()){
			
			if (visitedNode instanceof ObjectCreationExpr) {
				ObjectCreationExpr creationExpr = (ObjectCreationExpr)visitedNode;
				Node classNode = creationExpr.getChildNodes().get(0);
				builder
				.append(classNode)
				.append(".")
				.append(OBJECT_CREATE)
				.append("-");
			} else if (visitedNode instanceof MethodCallExpr) {
				JavaParserFacade facade = JavaParserFacade.get(combinedTypeSolver);
				MethodCallExpr callExpr = (MethodCallExpr)visitedNode;
				System.out.println(callExpr);
				Node methodNode = callExpr.getChildNodes().get(0);
				String simpleApiName = "";
				
				if ("com.github.javaparser.ast.expr.SimpleName".equals(methodNode.getClass().getTypeName())) {//TODO 내부 메서드 값 추가!
					SymbolReference<com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration> reference = 
							facade.solve(callExpr);
					simpleApiName = reference.getCorrespondingDeclaration().getClassName();
				} else {
					Type typeOfTheNode = facade.getType(methodNode);
					String apiName = typeOfTheNode.describe().toString();
					simpleApiName = apiName.substring(apiName.lastIndexOf(".") + 1);
				}
				
				builder
				.append(simpleApiName)
				.append(".")
				.append(callExpr.getName())
				.append("-");
			}
		}
		
		apiSequence = builder.substring(0, builder.length()-1);
	}
	
	public String getAPISequence() {
		return apiSequence;
	}
}
