package sol_5pecia1.parser.file;

import java.io.*;
import java.util.Iterator;

/**
 * Created by sol on 17. 6. 4.
 */
public class FileIterable implements Iterable<File>{
    private final File listFile;

    public FileIterable(File listFile) {
        this.listFile = listFile;
    }

    @Override
    public Iterator<File> iterator() {
        return new Iterator<File>() {
            @Override
            public boolean hasNext() {
                if (listFile.exists()) {
                    try (BufferedReader reader = new BufferedReader(
                            new FileReader(listFile))) {
                        boolean result = false;
                        String line = reader.readLine();

                        if (line != null) {
                            File file = new File(line);
                            result = file.exists();
                        }

                        if (!result) {
                            System.out.println("haven't next project");
                        }
                        return result;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            public File next() {
                File tempFile = new File(
                        listFile.getParentFile()
                                .getAbsolutePath() + "/temp.txt");
                String nextPath = null;

                try(BufferedReader reader = new BufferedReader(
                        new FileReader(listFile));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                    boolean isNotReadNextPath= true;

                    String line;
                    while((line = reader.readLine()) != null) {
                        if (isNotReadNextPath) {
                            isNotReadNextPath = false;
                            nextPath = line;
                            continue;
                        }
                        writer.write(line);
                        writer.newLine();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                tempFile.renameTo(listFile);
                return new File(nextPath);
            }
        };
    }
}
