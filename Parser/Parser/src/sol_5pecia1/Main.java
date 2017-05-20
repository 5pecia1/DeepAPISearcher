package sol_5pecia1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import sol_5pecia1.parser.FileSearcher;
import sol_5pecia1.parser.functions.Compiler;
import sol_5pecia1.parser.functions.Parser;

public class Main {

	/**
	 * TODO 1. make file about root file list.
	 * TODO 2. make multi-processing or multi-threading
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
		try {
			fileSearcher.search();
		} catch(Throwable e) {
			File errorFile = new File(saveRoot.toString() + "\\error.txt");
			
			try(FileWriter fileWriter = new FileWriter(errorFile);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
				bufferedWriter.write(e.getMessage());
				
				System.out.println("---------------------------------------------");
				
				for(StackTraceElement ste : e.getStackTrace()) {
					bufferedWriter.write(ste.toString());
				}
				
				System.out.println("saved error file!!!!!!");
			} catch (IOException ioe) {
				System.out.println(ioe);
			}
		}
	}

}
