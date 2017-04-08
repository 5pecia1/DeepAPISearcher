package sol_5pecia1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class UseIOUtils extends StartFunction{
	protected static String SOURCE_PATH = "..\\commons-io-2.5-src-for-test\\src\\main\\java";

	protected static String  IOUTILS_PATH = "\\org\\apache\\commons\\io\\IOUtils.java";
	
	@Override
	MethodDeclaration start() {
		MethodDeclaration node = null;
		try {
			FileInputStream in;
			in = new FileInputStream(UseIOUtils.SOURCE_PATH + IOUTILS_PATH);
			CompilationUnit cu = JavaParser.parse(in);
			MethodVisitor methodVisitor = new MethodVisitor();
			methodVisitor.visit(cu, null);
			node = methodVisitor.getNode();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return node;
	}

	private static class MethodVisitor extends VoidVisitorAdapter<Void> {
		private MethodDeclaration node = null;
		
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
            	System.out.println("method name : " + n.getName());
            	System.out.println("method body : ");
            	System.out.println(n);
            	System.out.println("======Method visit end"); 
            	
            	node = n;
            }           
            super.visit(n, arg);
        }
        
        public MethodDeclaration getNode() {
        	return node;
        }
        
    }
}
