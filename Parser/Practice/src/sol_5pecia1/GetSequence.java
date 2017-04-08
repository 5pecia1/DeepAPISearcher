package sol_5pecia1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import sol_5pecia1.parser.methodinformation.ThesisSequenceVisitorImplements;


/**
 * java 파일에서 단순 sequence 얻기
 * @author sol
 *
 */
public class GetSequence {
	private static String SOURCE_PATH = "..\\commons-io-2.5-src-for-test\\src\\main\\java";
	private static String CLASS_PATH = "";
	
	private static String  IOUTILS_PATH = "\\org\\apache\\commons\\io\\IOUtils.java";

	public static void main(String[] args) throws FileNotFoundException {
		FileInputStream in = new FileInputStream(SOURCE_PATH + IOUTILS_PATH);

        CompilationUnit cu = JavaParser.parse(in);

//        System.out.println(cu.toString());
        new MethodVisitor().visit(cu, null);
	}

	/**
	 * @author sol
	 *
	 */
    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration n, Void arg) {
        	LinkedList<String> names = new LinkedList<>();
        	for(Node node : n.getChildNodes()) {
        		names.add(node.toString());
        	}
        	
            if (n.getName().toString().equals("copyLarge") && 
            		names.contains("final InputStream input") && 
            		names.contains("final OutputStream output") &&
            		names.contains("final byte[] buffer") &&
            		!names.contains("final long inputOffset")){
            	System.out.println(n.getName());
            	System.out.println(n);
            	System.out.println("==");
            	n.getAnnotations().forEach(System.out::println);
 
            	System.out.println("======Method visit end"); 
            	ThesisSequenceVisitorImplements thesisVisitorImplements =  new ThesisSequenceVisitorImplements();
            	thesisVisitorImplements.visit(n, null);
            	System.out.println(thesisVisitorImplements.getComment());
            	thesisVisitorImplements.getNodeListSequenceAboutThesis().forEach(System.out::println);
            	
	            System.out.println();
	            System.out.println();
	            
            }           
            super.visit(n, arg);
        }
        
    }
}
