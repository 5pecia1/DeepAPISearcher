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
        try {
            int exitValue = defaultExecutor.execute(
                    CommandLine.parse(
                            "java -jar "
                                    + "/media/sol/OS/Users/sol/DeepAPISearcher/Parser/Parser/out/artifacts/Parser_main_jar"
                                    + parsingFile
                                    + " "
                                    + parsedFilePath + PARSED_FILE
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "tt";
    }
}
