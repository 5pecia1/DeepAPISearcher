package sol_5pecia1.parser;

import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.*;
import java.util.Iterator;

/**
 * Created by sol on 17. 6. 3.
 */
public class ProjectRootParser {
    private final static int PROJECT_MAX_COUNT = 10;
    private final static String PROJECT_LIST_FILE = "projectList.dat";

    public static void main(String[] args) {
        if (args.length !=2 && args.length != 3) {
            System.err.println("arg err\nSTART_ROOT SAVE_ROOT");
            System.exit(0);
        }

        File startRoot = new File(args[0]);
        File saveRoot = new File(args[1]);

        if (!startRoot.exists() || !saveRoot.exists()) {
            System.err.println("root not exist");
            System.exit(0);
        }

        Iterable<File> projectList = projectList(startRoot, saveRoot,
                (args.length == 3)? Boolean.getBoolean(args[2]) : false);


        for(File project : projectList) {
            
        }
    }

    public static Iterable<File> projectList(File startRoot, File saveRoot,
                                             boolean newProjectFile) {
        File projectListFile = new File(saveRoot + "/" + PROJECT_LIST_FILE);

        if (!projectListFile.exists() || newProjectFile) {
            try (FileWriter fileWriter = new FileWriter(projectListFile);
                 BufferedWriter bufferedWriter =
                         new BufferedWriter(fileWriter)) {
                for (File projectFile : startRoot.listFiles()) {
                    bufferedWriter.write(projectFile.getAbsolutePath());
                }
            } catch (IOException ioe) {
                System.out.println(ioe);
            }

            return projectList(startRoot, saveRoot, false);
        }

        return new ProjectIterable(projectListFile);
    }

    static class ProjectIterable implements Iterable<File> {
        private final File projectListFile;

        public ProjectIterable(File projectListFile) {
            this.projectListFile = projectListFile;
        }

        @Override
        public Iterator<File> iterator() {
            return new ProjectIterator(projectListFile);
        }
    }

    static class ProjectIterator implements Iterator<File> {
        private File projectListFile;

        public ProjectIterator(File projectListFile) {
            this.projectListFile = projectListFile;
        }

        @Override
        public boolean hasNext() {
            try(BufferedReader reader = new BufferedReader(
                    new FileReader(projectListFile))) {
                boolean result = false;
                String line = reader.readLine();

                if (line != null) {
                    File file = new File(line);
                    result = file.exists();
                }

                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public File next() {
            File tempFile = new File(
                    projectListFile.getParentFile()
                            .getAbsolutePath() + "/temp.txt");
            String nextPath = null;

            try(BufferedReader reader = new BufferedReader(
                    new FileReader(projectListFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                boolean isNotReadNextPath= true;

                String line;
                while((line = reader.readLine()) != null) {
                    if (isNotReadNextPath) {
                        isNotReadNextPath = false;
                        nextPath = line;
                        continue;
                    }
                    writer.write(line + System.getProperty("line.separator"));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            tempFile.renameTo(projectListFile);
            projectListFile = tempFile;
            return new File(nextPath);
        }
    }
}
