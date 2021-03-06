package sol_5pecia1.parser;

import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import sol_5pecia1.parser.file.FileIterable;
import sol_5pecia1.parser.file.FileSearcher;
import sol_5pecia1.parser.file.function.JavaCompiler;
import sol_5pecia1.parser.log.LogSaver;
import sol_5pecia1.parser.parser.JavaParserFunction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by sol on 17. 6. 3.
 */
public class ProjectRootParser {
    private final static String PROJECT_LIST_FILE = "projectList.dat";
    private final static String JAVA_LIST_FILE = "javaList.dat";
    private final static String SAVE_DATA = "data-%d.dat";

    public static void main(String[] args) {
        if (args.length !=2 && args.length != 3) {
            System.err.println("arg err\nSTART_ROOT SAVE_ROOT");
            System.exit(0);
        }

        File startRoot = new File(args[0]);
        File saveRoot = new File(args[1]);

        if (!startRoot.exists() || !saveRoot.exists()) {
            System.err.println("root not exist");
            System.exit(0);
        }

        boolean isNewStart = (args.length == 3)? Boolean.valueOf(args[2]) : false;

        Iterable<File> projectList = projectList(startRoot, saveRoot, isNewStart);
        LogSaver logSaver = new LogSaver(saveRoot, isNewStart);


        for(File project : projectList) {
            FileIterable files = FileSearcher.search(project, new File
                    (saveRoot +
                    "/" +
                    JAVA_LIST_FILE));

            for(File file: files) {
                try {
                    boolean isCompiled = new JavaCompiler().apply(file);
                    System.out.println("Compile result : " + isCompiled);

                    if (isCompiled) {
                        File saveFile = new File(
                                        saveRoot.getAbsolutePath()
                                                + "/"
                                                + String.format(
                                                        SAVE_DATA,
                                                logSaver.getDataFileCount()));
                        boolean isParsed
                                = new JavaParserFunction()
                                .apply(file, saveFile);
                        System.out.println("Parsing result : " + isParsed);

                        if (isParsed) {
                            logSaver.saveLog(file, saveFile);
                        }
                    }
                } catch (OutOfMemoryError oome) {
                    System.err.println(oome);
                }
            }

            JavaParserFacade.clearInstances();
            System.out.println();
        }
    }

    private static Iterable<File> projectList(File startRoot, File saveRoot,
                                             boolean newProjectFile) {
        File projectListFile = new File(saveRoot + "/" + PROJECT_LIST_FILE);

        if (!projectListFile.exists() || newProjectFile) {
            try (FileWriter fileWriter = new FileWriter(projectListFile);
                 BufferedWriter bufferedWriter =
                         new BufferedWriter(fileWriter)) {
                for (File projectFile : startRoot.listFiles()) {
                    bufferedWriter.write(projectFile.getAbsolutePath());
                    bufferedWriter.newLine();
                }
            } catch (IOException ioe) {
                System.out.println(ioe);
            }

            return projectList(startRoot, saveRoot, false);
        }

        return new FileIterable(projectListFile);
    }

}
