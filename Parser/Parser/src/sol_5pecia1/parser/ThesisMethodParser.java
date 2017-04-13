package sol_5pecia1.parser;

import java.util.regex.Pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.UnsolvedSymbolException;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.typesystem.Type;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import sol_5pecia1.parser.methodinformation.ThesisSequenceVisitorImplements;

/**
 * 하나의 메서드를 논문형식으로 Parsing한 apiSequence 및 annotation을 갖는다.
 * 
 * @author sol
 *
 */
public class ThesisMethodParser {
	private final static String OBJECT_CREATE = "new";
	private String apiSequence = "";
	private String annotation = "";
	private ThesisSequenceVisitorImplements thesisSequenceVisitorImplements = new ThesisSequenceVisitorImplements();

//	public ThesisMethodParser (MethodDeclaration node, JavaParserTypeSolver sourceClassSolver) {
//		this(node, sourceClassSolver, null);
//	}
//
//	public ThesisMethodParser (MethodDeclaration node, JavaParserTypeSolver sourceSolver, JavaParserTypeSolver classSolver) {
//		System.out.println(node);
//		
//		thesisSequenceVisitorImplements.visit(node, null);
//
//		CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
//		combinedTypeSolver.add(new ReflectionTypeSolver());
//		combinedTypeSolver.add(sourceSolver);
//
//		if (classSolver != null) {
//			combinedTypeSolver.add(classSolver);
//		}
//
//		apiSequence = parsingApi(combinedTypeSolver);
//		apiSequence = apiSequence.substring(0, apiSequence.length() - 1);
//		annotation = parsingAnnotation();
//		System.out.println("apiSequence : " + apiSequence);
//		System.out.println("annotation : " + annotation);
//		System.out.println("==log end");
//		System.out.println();
//	}
	
	public ThesisMethodParser(MethodDeclaration node, CombinedTypeSolver combinedTypeSolver) {
		System.out.println("==log start");
		System.out.println(node);
		
		thesisSequenceVisitorImplements.visit(node, null);

		apiSequence = parsingApi(combinedTypeSolver);
		apiSequence = apiSequence.substring(0, apiSequence.length() - 1);
		annotation = parsingAnnotation();
		System.out.println("apiSequence : " + apiSequence);
		System.out.println("annotation : " + annotation);
		System.out.println("==log end");
	}

	private String parsingApi(CombinedTypeSolver combinedTypeSolver) {
		StringBuilder builder = new StringBuilder();
		for (Node visitedNode : thesisSequenceVisitorImplements.getNodeListSequenceAboutThesis()) {
			
			if (visitedNode instanceof ObjectCreationExpr) { //생성자
				ObjectCreationExpr creationExpr = (ObjectCreationExpr) visitedNode;
				Node classNode = creationExpr.getChildNodes().get(0);
				builder.append(classNode).append(".").append(OBJECT_CREATE).append("-");
			} else if (visitedNode instanceof MethodCallExpr) { // 메소드 콜
				try {
					JavaParserFacade facade = JavaParserFacade.get(combinedTypeSolver);
					MethodCallExpr callExpr = (MethodCallExpr) visitedNode;
					Node methodNode = callExpr.getChildNodes().get(0);
					String simpleApiName = "";
	
					//TODO 내부 메서드 검색해서 값 추가!
					if ("com.github.javaparser.ast.expr.SimpleName".equals(methodNode.getClass().getTypeName())) {// 내부 메서드 콜
						SymbolReference<com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration> reference 
						= facade.solve(callExpr);
						simpleApiName = reference.getCorrespondingDeclaration().getClassName();
					} else {
						try {
						 	Type typeOfTheNode = facade.getType(methodNode);
						 	String apiName = typeOfTheNode.describe().toString();
						 	simpleApiName = apiName.substring(apiName.lastIndexOf(".") + 1);
						} catch (UnsolvedSymbolException e) { // static 메서드 콜
							simpleApiName = methodNode.toString();
						}
					}
	
					builder.append(simpleApiName).append(".").append(callExpr.getName()).append("-");
				} catch (IllegalStateException e) {
					System.err.println(e);
					//TODO 예외 처리하기 builder 값 지워야 할 듯
				}
			}
		}

		return builder.toString();
	}
	
	/**
	 * filter about tag, todo, brackets
	 * and return first line
	 * 
	 * @return
	 */
	private String parsingAnnotation() {
		JavadocComment comment = thesisSequenceVisitorImplements.getComment();
		String description = comment.parse().getDescription().toText();
		String firstLine = "";
		
		description = parsingTodo(description);
		firstLine = getFirstLine(description);
		
		firstLine = parsingTag(firstLine);
		firstLine = parsingBrackets(firstLine);
		
		return firstLine;
	}
	
	private String getFirstLine(String string) {
		String firstLine = "";

		if (!string.toLowerCase().contains("todo") && 
				(string.contains(".\n") || string.contains(". ") || string.contains("."))) {
			String splited[] = string.split("([.]\n|[.] )");
			firstLine= splited[0].replaceAll("\n", " ");
		} else if (string.contains("\n")){
			firstLine = string.substring(0, string.indexOf("\n"));
		} 
		return firstLine;
	}
	
	private String parsingTag(String string) {
		if (Pattern.matches(".*<.+>.*", string)) {
			return parsingTag(
					string.substring(0, string.indexOf("<")) + 
					string.substring(string.indexOf(">") + 1)
					);
		} else {
			return string;
		}
	}
	
	/**
	 * TODO 좀 더 확실히 튜닝 가능한지 확인하기, class 사용해서 괄호안의 객체 정보 얻어오기
	 * @param string
	 * @return
	 */
	private String parsingTodo(String string) {
		String firstLine = getFirstLine(string);
		if (firstLine.toLowerCase().contains("todo")) {
			return parsingTodo(string.substring(firstLine.length() + 1));
		} else {
			return string;
		}
	}
	
	private String parsingBrackets(String string) {
		if (Pattern.matches(".*[{].+[}].*", string)) {
			return parsingTag(
					string.substring(0, string.indexOf("{")) + 
					string.substring(string.indexOf("}") + 1)
					);
		} else {
			return string;
		}
	}

	public String getAPISequence() {
		return apiSequence;
	}

	public String getAnnotaion() {
		return annotation;
	}
}
