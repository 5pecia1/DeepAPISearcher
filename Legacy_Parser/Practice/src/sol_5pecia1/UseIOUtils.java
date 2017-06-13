package sol_5pecia1;

import java.io.FileNotFoundException;

import com.github.javaparser.ast.body.MethodDeclaration;

import sol_5pecia1.parser.ThesisClassParser;

public class UseIOUtils extends StartFunction{
	protected static String SOURCE_PATH = "..\\commons-io-2.5-src-for-test\\src\\main\\java";
	
	protected static String  IOUTILS_PATH = "\\org\\apache\\commons\\io\\IOUtils.java";

	@Override
	MethodDeclaration start() {
		try {
			ThesisClassParser classParser = new ThesisClassParser(SOURCE_PATH + IOUTILS_PATH);
			System.out.println("result : ");
			System.out.println(classParser.getParsedDatas());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
