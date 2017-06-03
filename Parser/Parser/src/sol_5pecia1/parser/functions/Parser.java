package sol_5pecia1.parser.functions;

import java.io.File;
import java.io.FileNotFoundException;

import sol_5pecia1.parser.ThesisClassParser;
import sol_5pecia1.parser.file.DataSaver;

public class Parser{
	public void run(File file) {
		try {
			ThesisClassParser classParser = new ThesisClassParser(file.toString());
			DataSaver.save(classParser.getParsedDatas());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
