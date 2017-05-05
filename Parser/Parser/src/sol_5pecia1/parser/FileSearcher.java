package sol_5pecia1.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.swing.plaf.synth.SynthSeparatorUI;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class FileSearcher {
	private final File rootFile;
	private final Iterator<String> rootFileList;

	private File currentProject;
	private File currentCompilableParentPath;
	private Iterator<String> currentSiblingFileList;
	private File currentCompilablePath;
	
	public FileSearcher(String path) throws FileNotFoundException {
		rootFile = new File(path);
		if (!rootFile.exists()) {
			throw new FileNotFoundException();
		}
		rootFileList = Arrays.asList(rootFile.list()).iterator();
	}
	
//	public boolean hasNextFile() {
//		
//	}
//	
//	public String nextFile() {
//		
//	}
//	
//	public boolean hasNextPackage() {
//		
//	}
	
	public File nextCompilablePackage() {
		System.out.println("cp : " + currentProject);
		System.out.println("cc : " + currentCompilablePath);
		System.out.println("ccp : " + currentCompilableParentPath);
		if (currentProject != null && currentCompilablePath != null &&
				!currentProject.equals(currentCompilablePath)) {
			if (currentSiblingFileList.hasNext()) {
				String nextSibling = currentSiblingFileList.next();
				System.out.println(nextSibling);
				return getCompilablePath(new File(currentCompilableParentPath + "\\" + nextSibling));
			} else {
				currentCompilablePath = currentCompilableParentPath;
				currentCompilableParentPath = currentCompilablePath.getParentFile();
				currentSiblingFileList = Arrays.asList(currentCompilableParentPath.list()).iterator();
				currentSiblingFileList.next();//current file path;
				return nextCompilablePackage();
			}
		} 	else if (hasNextProject()) {
			currentProject = new File(rootFile + "\\" + nextProject());
			return  getCompilablePath(currentProject);
		} else {
			return null;
		}
	}
	
	private boolean hasNextProject() {
		return rootFileList.hasNext();
	}
	
	private String nextProject(){
		return rootFileList.next();
	}
	
	private File getCompilablePath(File file) {
		File firstJavaFile = searchFirstJavaFile(file);
		if (firstJavaFile == null) {
			return nextCompilablePackage();
		}
		
		FileInputStream in = null;
		try {
			in = new FileInputStream(firstJavaFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		CompilationUnit cu = JavaParser.parse(in);
		String currentPackage = cu.getPackageDeclaration().get().getNameAsString();
		
		StringBuilder compilePath = new StringBuilder(firstJavaFile.toString());
		compilePath.delete(compilePath.lastIndexOf("\\"), compilePath.length());
		for (String splitedName: currentPackage.split("[.]")) {
			compilePath.delete(compilePath.lastIndexOf("\\"), compilePath.length());
		}
		
		currentCompilablePath = new File(compilePath.toString());
		currentCompilableParentPath = currentCompilablePath.getParentFile();
		currentSiblingFileList = Arrays.asList(currentCompilableParentPath.list()).iterator();
		currentSiblingFileList.next();//current file path;
		return currentCompilablePath;
	}
	
	private File searchFirstJavaFile(File file) {
		if (file.isFile() && Pattern.matches(".+[.]java", file.getName())) {
			return file;
		} else if (file.exists() && file.isDirectory()) {
			for (String f : file.list()) {
				File searchedFile = searchFirstJavaFile(new File(file.getAbsolutePath() + "\\" + f));
				if (searchedFile != null) {
					return searchedFile;
				}
			}
			
			return null;
		} else {
			return null;

		}
	}
}
