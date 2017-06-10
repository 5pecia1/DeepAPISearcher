package sol_5pecia1.parser.parser.preprocessing.function;

import sol_5pecia1.parser.parser.preprocessing.DataPreprocessingFunction;

import java.util.regex.Pattern;

/**
 * Created by sol on 17. 6. 10.
 */
public class GetParsedAboutBrackets implements DataPreprocessingFunction {
    @Override
    public String apply(String s) {
        if (Pattern.matches(".*[{].+[}].*", s)) {
            return apply(
                    s.substring(0, s.indexOf("{")) +
                            s.substring(s.indexOf("}") + 1)
            );
        } else {
            return s;
        }
    }
}
