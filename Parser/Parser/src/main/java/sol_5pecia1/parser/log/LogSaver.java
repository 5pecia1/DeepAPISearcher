package sol_5pecia1.parser.log;

import java.io.*;

/**
 * == File constructor ==<br>
 * Data File Count<br>
 * Data File Line Count<br>
 * Least Parsing File Name<br>
 * Created by sol on 17. 6. 14.
 */
public class LogSaver {
    private final static int MAX_FILE_COUNT = 10000;
    private final static String LOG_FILE_NAME = "parsing.log";

    private final File logFile;

    private int dataFileCount;
    private int dataFileLineCount;
    private String leastParsingFile = "/";

    public LogSaver(File savePath, boolean isNewStart) {
        this.logFile = new File(savePath.getAbsolutePath()
                + "/" + LOG_FILE_NAME);

        if (logFile.exists()) {
            if (isNewStart) {
                logFile.delete();
                saveLog(new File("/"));
            } else {
                readLog();
            }
        }
    }

    private void readLog() {
        try(FileReader fileReader = new FileReader(logFile);
            BufferedReader reader = new BufferedReader(fileReader)) {
            String dataFileCount = reader.readLine();
            String dataFileLineCount = reader.readLine();
            String leastParsingFile = reader.readLine();

            this.dataFileCount = Integer.parseInt(dataFileCount);
            this.dataFileLineCount = Integer.parseInt(dataFileLineCount);
            this.leastParsingFile
                    = (leastParsingFile == null)? leastParsingFile : "";
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveLog(File parsingFile) {
        changeLogData(parsingFile);

        try(FileWriter fileWriter = new FileWriter(logFile);
            BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.append(String.valueOf(dataFileCount));
            writer.newLine();
            writer.append(String.valueOf(dataFileLineCount));
            writer.newLine();
            writer.append(leastParsingFile);
            
            System.out.println("line count : " + dataFileLineCount);
            System.out.println("log save data : " + leastParsingFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeLogData(File parsingFile) {
        try {
            dataFileLineCount = countLines(parsingFile);

            if (dataFileLineCount >= MAX_FILE_COUNT) {
                dataFileLineCount = 0;
                dataFileCount++;
                System.out.println("update file count : " + dataFileCount);
            }
        } catch (IOException e) {
            dataFileCount++;
            dataFileLineCount = 0;

            e.printStackTrace();
        } finally {
            leastParsingFile = parsingFile.getAbsolutePath();
        }

    }

    private int countLines(File file) throws IOException {
        try(InputStream is = new BufferedInputStream(new FileInputStream(file))) {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        }
    }

    public int getDataFileLineCount() {
        return dataFileLineCount;
    }
}
