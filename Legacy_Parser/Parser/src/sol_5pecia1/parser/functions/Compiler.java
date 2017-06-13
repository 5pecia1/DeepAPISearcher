package sol_5pecia1.parser.functions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;

public class Compiler {
	private static long i = 1;
	
	public void compile(File file){
		try {
			System.out.println(i++);
			FileInputStream in = new FileInputStream(file);
			CompilationUnit cu = JavaParser.parse(in);
			
			String packageName = "";
			try {
				packageName = cu.getPackageDeclaration().get().getNameAsString();
			} catch (NoSuchElementException nsee) { // 패키지가 없는 경우는아님...
				System.out.println(nsee);
			}
			
			String osName = System.getProperty("os.name");

			Runtime rt = Runtime.getRuntime();
			File filePath = file.getParentFile();
			for (int i = packageName.split("[.]").length; i > 0 ; i--) {
				filePath = filePath.getParentFile();
			}
			String pn = packageName.replaceAll("[.]", "/");
			String[] cmd = null;

			if (osName.toLowerCase().startsWith("window")) {
				cmd = new String[]{"cmd.exe", "/c", "cd ", filePath.toString(), "&&",  "javac", pn + "/" + file.getName()};

			} else {
				cmd = new String[]{"/bin/sh", "/c", "cd ", filePath.toString(), "&&",  "javac", pn + "/" + file.getName()};

			}

			System.out.println(Arrays.asList(cmd));
			Process proc = rt.exec(cmd);

//			InputStream is = proc.getInputStream();
//			InputStreamReader isr = new InputStreamReader(is);
//			BufferedReader br = new BufferedReader(isr);

			System.out.println(proc.waitFor(1000, TimeUnit.MILLISECONDS));
			proc.destroy();
//			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ParseProblemException ppe) {
			ppe.printStackTrace();
		}

	}
}
