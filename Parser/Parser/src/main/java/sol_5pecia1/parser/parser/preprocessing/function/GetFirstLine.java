package sol_5pecia1.parser.parser.preprocessing.function;

import sol_5pecia1.parser.parser.preprocessing.DataPreprocessingFunction;

/**
 * Created by sol on 17. 6. 9.
 */
public class GetFirstLine implements DataPreprocessingFunction {
    @Override
    public String apply(String s) {
        String firstLine = "";

        if (!s.toLowerCase().contains("todo") &&
                (s.contains(".\n") || s.contains(".\r")
                        || s.contains(". ") || s.contains("."))) {
            String splited[] = s.split("([.]\n|[.]|[.]<p|[.]<P)");
            firstLine= splited[0].replaceAll("\n", " ").replaceAll("\r", "");
        } else if (s.contains("\n")){
            firstLine = s.substring(0, s.indexOf("\n"));
        }
        return firstLine;
    }
}
