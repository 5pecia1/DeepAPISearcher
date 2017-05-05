package sol_5pecia1.parser.File;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class JavaFile extends DFSFile{
	private String packageName;

	private JavaFile(File file) {
		super(file);
		try {
			FileInputStream in = new FileInputStream(this); //TODO use try-catch-resource
			CompilationUnit cu = JavaParser.parse(in);
			packageName = cu.getPackageDeclaration().get().getNameAsString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			packageName = null;
		}
	}
	
	public static JavaFile factory(File file) {
		if (file.isFile() && Pattern.matches(".+[.]java", file.getName())) {
			return new JavaFile(file);
		} else {
			return null;
		}
	}

	public String getPackageName()  {
		return packageName;
	}
}
