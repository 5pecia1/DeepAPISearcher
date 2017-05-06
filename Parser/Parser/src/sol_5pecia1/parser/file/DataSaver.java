package sol_5pecia1.parser.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.Diagnostic;

public class DataSaver {
	private final static int MAX_COUNT = 10000;
	private final static int MAX_LINE = MAX_COUNT * 2;
	private final static int SPLIT_COUNT = 10;
	private final static int CUT_LINE = MAX_LINE / SPLIT_COUNT;
	
	private static File saveRoot;
	private static int fileCount = -1;
	private static int count = MAX_LINE; 
	private static int splitCouont = 0;
	
	private static File currentFile;
	private static BufferedWriter currentWriter;
	
	private static StringBuilder savedLine;

	private DataSaver() {
	}


	public static void setRootFile(File file) {
		saveRoot = file;
	}
	
	public static void flushFile() {
		try {
			currentWriter.write(savedLine.toString());
			currentWriter.close();
			currentWriter = null;
			System.out.println("count : " + count);
			System.out.println("splitCount : " + splitCouont);
			System.out.println("fileCount : " + fileCount);
			System.out.println("max flush Success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized void save(String l) {
		Matcher matcher = Pattern.compile("\n").matcher(l);

		if (count < MAX_LINE) {
			StringBuilder saveLine = new StringBuilder();
	
			int previousLine = 0;
			int i = 0;
			while(matcher.find(i)) {
				i = matcher.end();
				
				count++;
				
				if (count >= MAX_LINE || 
						count / CUT_LINE > splitCouont) { // 다음 파일 대상 || 다음 IO 대상
					saveLine.append(l.substring(previousLine, i));
				} else {
					savedLine.append(l.substring(previousLine, i));
				}
				previousLine = i;
			}
			
			System.out.println("===============\ncount : " + count+ "\n==============");
			if (!"".equals(saveLine.toString())) {
				try {
					currentWriter.flush();
					System.out.println("===============\nwrite data\n==============");
					System.out.println("===============\npage : " + splitCouont + "\n==============");
					splitCouont++;
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				savedLine = saveLine;
			}
		} else {
			try {
				if (currentWriter != null) {
					currentWriter.close();
				}
				if (savedLine == null || "".equals(savedLine.toString())) {
					savedLine = new StringBuilder();
					count = 0;
				} else {
					for(int i = 0; matcher.find(i); i = matcher.end()) {
						count++;
					}
				}
				currentFile = new File(saveRoot.getAbsolutePath() + "\\data-" + ++fileCount + ".txt");
				System.out.println(
						currentFile.createNewFile()
						? "make new file : " + fileCount 
						: "exist"
				);
			
				FileWriter fileWriter = new FileWriter(currentFile);
				currentWriter = new BufferedWriter(fileWriter);
				
				save(l);
			} catch (IOException ioe) {
				System.out.println(ioe);
			}
		}
	}
}
