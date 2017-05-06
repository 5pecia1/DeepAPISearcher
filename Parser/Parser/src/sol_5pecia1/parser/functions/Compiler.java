package sol_5pecia1.parser.functions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class Compiler {
	static int i = 1;
	public void compile(File file){
		try {
			System.out.println(i++);
			FileInputStream in;
			in = new FileInputStream(file);
			CompilationUnit cu = JavaParser.parse(in);
			String packageName = cu.getPackageDeclaration().get().getNameAsString();
			
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

//			System.out.println(1);
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

//			System.out.println(2);
			String line;
//			while ((line = br.readLine()) != null) {
//				System.out.println(line);
//				System.out.flush();
//			}
//			System.out.println(3);
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					try {
//						Thread.sleep(300);
//						proc.destroy();
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}).start();
			
			System.out.println(proc.waitFor(300, TimeUnit.MILLISECONDS));
			br.close();
			isr.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
