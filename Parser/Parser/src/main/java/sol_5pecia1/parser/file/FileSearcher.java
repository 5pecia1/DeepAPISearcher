package sol_5pecia1.parser.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by sol on 17. 6. 3.
 */
public class FileSearcher {

    public static FileIterable search(File projectFile, File saveFile) {
        if (saveFile.exists()) {
            saveFile.delete();
        }

        searchAndSaveFileList(projectFile, saveFile);

        return new FileIterable(saveFile);
    }

    private static void searchAndSaveFileList(File file, File saveFile) {
        if (file.isFile() && Pattern.matches(".+[.]java", file.getName())) {
            try (FileWriter fileWriter = new FileWriter(saveFile,true);
                 BufferedWriter writer = new BufferedWriter(fileWriter)) {
                writer.append(file.getAbsolutePath());
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (file.isDirectory()) {
            File[] files = file.listFiles();

            if (files != null) {
                for (File f : files) {
                    searchAndSaveFileList(f, saveFile);
                }
            }
        }
    }
}
