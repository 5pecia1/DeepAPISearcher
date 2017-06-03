package sol_5pecia1;

import java.io.File;

import sol_5pecia1.parser.FileSearcher;
import sol_5pecia1.parser.functions.Compiler;
import sol_5pecia1.parser.functions.Parser;

public class Main {

	/**
	 * 
	 * @param args
	 * [0] : start root
	 * [1] : save root
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("arg err\nSTART_ROOT SAVE_ROOT");
			return;
		}
		
		File startRoot = new File(args[0]);
		File saveRoot = new File(args[1]);
		if (!startRoot.exists() || !saveRoot.exists()) {
			System.err.println("root not exist");
			return;
		}
		
		FileSearcher fileSearcher = new FileSearcher(startRoot, saveRoot, new Compiler(), new Parser());
		fileSearcher.search();
	}

}
