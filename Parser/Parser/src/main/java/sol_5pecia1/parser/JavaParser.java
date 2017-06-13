package sol_5pecia1.parser;

import sol_5pecia1.parser.parser.ThesisClassParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by sol on 17. 6. 10.
 */
public class JavaParser {
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("please input two arg");
			System.exit(0);
		}

		ThesisClassParser thesisClassParser = new ThesisClassParser(new File(args[0]));

		try (FileWriter writer = new FileWriter(new File(args[1]), true);
				BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
			bufferedWriter.append(thesisClassParser.getParsedDatas());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
