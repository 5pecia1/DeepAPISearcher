package sol_5pecia1.parser.parser.preprocessing.function;

import sol_5pecia1.parser.parser.preprocessing.DataPreprocessingFunction;

/**
 * Created by sol on 17. 6. 10.
 */
public class GetParsedAboutTodo implements DataPreprocessingFunction {
    @Override
    public String apply(String string) {
        GetFirstLine getFirstLine = new GetFirstLine();

        String firstLine = getFirstLine.apply(string);

        if (firstLine.toLowerCase().contains("todo")) {
            return apply(string.substring(firstLine.length() + 1));
        } else {
            return string;
        }
    }
}
