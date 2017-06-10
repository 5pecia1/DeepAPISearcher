package sol_5pecia1.parser.parser;

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
import sol_5pecia1.parser.parser.preprocessing.DataPreprocessingFunction;
import sol_5pecia1.parser.parser.preprocessing.Preprocessings;

import java.util.List;


/**
 * Created by sol on 17. 6. 9.
 */
public class ThesisMethodParser {
    private final static String OBJECT_CREATE = "new";
    private final static String CONCAT_CHAR = "-";
    private final static String DOT = ".";

    private final static String INNER_METHOD_CALL = "com.github.javaparser.ast.expr.SimpleName";

    private final MethodDeclaration node;
    private final CombinedTypeSolver combinedTypeSolver;
    private final ThesisSequenceVisitorImplements thesisSequenceVisitorImplements
            = new ThesisSequenceVisitorImplements();
    private String apiSequence = "";
    private String annotation = "";
    private String annotaion;


    public ThesisMethodParser(MethodDeclaration node, CombinedTypeSolver combinedTypeSolver) {
        this.node = node;
        this.combinedTypeSolver = combinedTypeSolver;
    }

    public void parsing() {
        thesisSequenceVisitorImplements.visit(node, null);

        apiSequence = parsingApi(combinedTypeSolver);

        if (apiSequence != null && !"".equals(apiSequence)) {
            apiSequence = apiSequence.substring(0, apiSequence.length() - 1);
        }
        annotation = parsingAnnotation();
    }

    private String parsingApi(CombinedTypeSolver combinedTypeSolver) {
        StringBuilder builder = new StringBuilder();
        for (Node visitedNode : thesisSequenceVisitorImplements.getNodeListSequenceAboutThesis()) {
            String parsedData = "";

            if (visitedNode instanceof ObjectCreationExpr) { //생성자
                parsedData = constructorParsing(
                        (ObjectCreationExpr) visitedNode);

            } else if (visitedNode instanceof MethodCallExpr) { // 메소드 콜
                parsedData = methodParsing(
                        (MethodCallExpr) visitedNode);
            }

            if (!"".equals(parsedData)) {
                builder.append(parsedData);
            }
        }

        return builder.toString();
    }

    private String parsingAnnotation() {
        JavadocComment comment = thesisSequenceVisitorImplements.getComment();
        if (comment == null) {
            return null;
        }
        String description = comment.parse().getDescription().toText();
        StringBuilder resultData = new StringBuilder(description);

        List<DataPreprocessingFunction> parsingFunctions =
                Preprocessings.INSTANCE.getFunctions();

        parsingFunctions
            .forEach(f -> {
                String data = f.apply(resultData.toString());
                resultData
                    .delete(0, resultData.length())
                    .append(data);
            });

        return resultData.toString();
    }

    private String constructorParsing(ObjectCreationExpr visitedNode) {
        ObjectCreationExpr creationExpr = visitedNode;
        Node classNode = creationExpr.getChildNodes().get(0);
        return classNode.toString() + DOT + OBJECT_CREATE + CONCAT_CHAR;
    }

    private String methodParsing(MethodCallExpr visitedNode) {
        try {
            JavaParserFacade facade = JavaParserFacade.get(combinedTypeSolver);
            MethodCallExpr callExpr = visitedNode;
            Node methodNode = callExpr.getChildNodes().get(0);
            String simpleApiName = "";

            //TODO 내부 메서드 검색해서 값 추가!
            try {
                if (INNER_METHOD_CALL.equals(methodNode.getClass().getTypeName())) {// 내부 메서드 콜
                    simpleApiName = innerMethodParsing(callExpr,
                            facade, methodNode);
                } else {
                    simpleApiName = outerMethodCall(callExpr, facade, methodNode);
                }

                if (!"".equals(simpleApiName)) {
                    return simpleApiName + DOT + callExpr.getName() + CONCAT_CHAR;
                }
            } catch (RuntimeException re) {
                System.out.println(re);
            }
        } catch (IllegalStateException e) {
            System.err.println(e);
        }

        return "";
    }

    private String innerMethodParsing(MethodCallExpr callExpr,
                                      JavaParserFacade facade, Node methodNode) {
        String simpleApiName = "";

        try {
            SymbolReference
                    <com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration>
                    reference = facade.solve(callExpr);
            simpleApiName = reference.getCorrespondingDeclaration().getClassName();
        } catch (UnsolvedSymbolException e) { // 왜 에러 나는지 몰겠..
            simpleApiName = methodNode.toString();

        }

        return simpleApiName;
    }

    private String outerMethodCall(MethodCallExpr callExpr,
            JavaParserFacade facade, Node methodNode) {

        String simpleApiName = "";

        try {
            Type typeOfTheNode = facade.getType(methodNode);
            String apiName = typeOfTheNode.describe().toString();
            simpleApiName = apiName //<>를 사용하는 변수는 이상하게 '>'가 추가된다 TODO 확인 요망
                            .substring(apiName.lastIndexOf(".") + 1)
                            .replace(">", "");
        } catch (UnsolvedSymbolException e) { // static 메서드 콜
            simpleApiName = methodNode.toString();
        }

        return simpleApiName;
    }

    public String getAPISequence() {
        return apiSequence;
    }

    public String getAnnotaion() {
        return annotaion;
    }
}
