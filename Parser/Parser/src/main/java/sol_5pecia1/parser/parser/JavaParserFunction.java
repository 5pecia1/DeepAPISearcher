package sol_5pecia1.parser.parser;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

import java.io.File;
import java.io.IOException;
import java.util.function.BiFunction;

/**
 * Created by sol on 17. 6. 9.
 */
public class JavaParserFunction implements BiFunction<File, File, Boolean> {

    @Override
    public Boolean apply(File parsingFile, File saveFile) {
        DefaultExecutor defaultExecutor = new DefaultExecutor();

        try {
            int exitValue = defaultExecutor.execute(
                    CommandLine.parse(
                            "java -jar "
                                    + "/home/sol/DeepAPISearcher/Parser/Parser/out/JavaParser.jar "
                                    + parsingFile
                                    + " "
                                    + saveFile
                    )
            );

            return exitValue == 0;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
