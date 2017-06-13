package sol_5pecia1.parser.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Created by sol on 17. 6. 10.
 */
public class ThesisClassParser {
    private final File javaPath;
    private final File classPackagePath;
    private File javaPackagePath;
    private StringBuilder builder = new StringBuilder();

    public ThesisClassParser(File javaClassPath) {
        this(javaClassPath, javaClassPath);
    }

    public ThesisClassParser(File javaPath, File classPackagePath) {
        this.javaPath = javaPath;
        this.classPackagePath = javaPath;
    }

    public String getParsedDatas() {
        if ("".equals(builder.toString())) {
            try {
                FileInputStream in = new FileInputStream(this.javaPath);
                System.out.println("class : " + this.javaPath);
                CompilationUnit cu = JavaParser.parse(in);

                String packageName = "";
                try {
                    packageName = cu.getPackageDeclaration().get().getNameAsString();
                } catch (NoSuchElementException nsee) {
                    System.out.println(nsee);
                }

                File javaPackagePathForLoop = javaPath;
                for (int i = packageName.split("[.]").length; i >= 0; i--) {
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
            } catch (ParseProblemException | FileNotFoundException ppe) {
                System.out.println(ppe);
            }
        }

        return builder.toString();
    }

    private class MethodVisitor extends VoidVisitorAdapter<Void> {
        LinkedList<ParsedData> parsedDataList = new LinkedList<>();

        @Override
        public void visit(MethodDeclaration n, Void arg) {
            JavaParserTypeSolver javaSolver = new JavaParserTypeSolver(javaPackagePath);
            JavaParserTypeSolver classSolver = new JavaParserTypeSolver(classPackagePath);

            CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
            combinedTypeSolver.add(new ReflectionTypeSolver());
            combinedTypeSolver.add(javaSolver);

            if (javaPackagePath.equals(classPackagePath)) {
                combinedTypeSolver.add(classSolver);
            }

            ThesisMethodParser methodParser = new ThesisMethodParser(n, combinedTypeSolver);

            methodParser.parsing();

            ParsedData data = new ParsedData(
                    methodParser.getAPISequence(), methodParser.getAnnotaion());

            if (data.isValid) {
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
