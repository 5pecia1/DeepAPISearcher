package sol_5pecia1.parser.file.function;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by sol on 17. 6. 3.
 */
public class JavaCompiler implements Function<File, Boolean> {

    @Override
    public Boolean apply(File file) {
        System.out.println("Start compile about : " + file);

        try {
            FileInputStream in = new FileInputStream(file);
            CompilationUnit cu = JavaParser.parse(in);

            String packageName =
                    cu.getPackageDeclaration().get().getNameAsString();

            String osName = System.getProperty("os.name");
            Runtime rt = Runtime.getRuntime();
            File filePath = file.getParentFile();

            for (int i = packageName.split("[.]").length; i > 0 ; i--) {
                filePath = filePath.getParentFile();
            }
            String pn = packageName.replaceAll("[.]", "/");
            String[] cmd = (osName.toLowerCase().startsWith("window"))
                    ? new String[]{
                    "cmd.exe", "/c", "cd ", filePath.toString(),
                    "&&",  "javac", pn + "/" + file.getName()}
                    : new String[]{
                    "/bin/sh", "/c", "cd ", filePath.toString(),
                    "&&",  "javac", pn + "/" + file.getName()};

            CommandLine commandLine = new CommandLine(
                    "javac -sourcepath \"" + filePath + "\" \""
                            + file.getAbsolutePath() + "\""
            );
//            CommandLine commandLine = new CommandLine("cd");

            DefaultExecutor defaultExecutor = new DefaultExecutor();
            int exitValue = defaultExecutor.execute(commandLine);
            System.out.println(exitValue);
//            Process process = rt.exec(cmd);

//            process.getErrorStream().close();
//            process.getInputStream().close();
//            process.getOutputStream().close();

//            boolean exitResult = process.waitFor(2000, TimeUnit.MILLISECONDS);
//            process.destroy();
//            System.out.println(Arrays.asList(cmd));
//            return exitResult != -1;
            return true;
        } catch (NoSuchElementException nsee) {
            nsee.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
        return false;
    }
}
