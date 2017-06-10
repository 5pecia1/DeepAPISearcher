package sol_5pecia1.parser.parser;

import java.io.File;
import java.util.function.Function;

/**
 * Created by sol on 17. 6. 9.
 */
public class JavaParsing implements Function<File, String> {

    @Override
    public String apply(File parsingFile) {
        ThesisClassParser thesisClassParser = new ThesisClassParser(parsingFile);

        return thesisClassParser.getParsedDatas();
    }
}
