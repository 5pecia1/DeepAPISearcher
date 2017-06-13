package sol_5pecia1.parser.parser;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

import java.io.File;
import java.io.IOException;
import java.util.function.BiFunction;

/**
 * Created by sol on 17. 6. 9.
 */
public class JavaParserFunction implements BiFunction<File, File, String> {
    private static final String PARSED_FILE= "parsed.dat";

    @Override
    public String apply(File parsingFile, File parsedFilePath) {
        DefaultExecutor defaultExecutor = new DefaultExecutor();
        int exitValue = 2;
        try {
            exitValue = defaultExecutor.execute(
                    CommandLine.parse(
                            "java -jar "
                                    + "/home/sol/DeepAPISearcher/Parser/Parser/out/JavaParser.jar "
                                    + parsingFile
                                    + " "
                                    + parsedFilePath + "/" + PARSED_FILE
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (exitValue == 1) + "";
    }
}
