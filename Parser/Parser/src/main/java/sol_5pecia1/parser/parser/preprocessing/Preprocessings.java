package sol_5pecia1.parser.parser.preprocessing;


import sol_5pecia1.parser.parser.preprocessing.function.GetFirstLine;
import sol_5pecia1.parser.parser.preprocessing.function.GetParsedAboutBrackets;
import sol_5pecia1.parser.parser.preprocessing.function.GetParsedAboutTag;
import sol_5pecia1.parser.parser.preprocessing.function.GetParsedAboutTodo;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sol on 17. 6. 9.
 */
public enum Preprocessings {
    INSTANCE;

    private List<DataPreprocessingFunction> functions;

    Preprocessings () {
        functions = Arrays.asList(
                new GetParsedAboutTodo(),
                new GetFirstLine(),
                new GetParsedAboutTag(),
                new GetParsedAboutBrackets()
        );
    }

    public List<DataPreprocessingFunction> getFunctions() {
        return functions;
    }
}
