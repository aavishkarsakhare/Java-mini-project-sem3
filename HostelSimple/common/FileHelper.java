package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

// File handling: reads and writes the text files in the data folder
public class FileHelper {
    public static final String FOLDER = "data/";

    // reads all lines of a file and returns them in a list
    public static ArrayList<String> readLines(String fileName) throws HostelException {
        ArrayList<String> lines = new ArrayList<String>();
        File file = new File(FOLDER + fileName);

        if (!file.exists()) {
            return lines; // file not created yet, so no records
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals("")) {
                    lines.add(line);
                }
            }
            br.close();
        } catch (IOException e) {
            throw new HostelException("Could not read the file " + fileName);
        }
        return lines;
    }

    // adds one new line at the end of a file
    public static void addLine(String fileName, String line) throws HostelException {
        try {
            new File(FOLDER).mkdirs(); // makes the data folder if it is not there
            PrintWriter pw = new PrintWriter(new FileWriter(FOLDER + fileName, true));
            pw.println(line);
            pw.close();
        } catch (IOException e) {
            throw new HostelException("Could not write to the file " + fileName);
        }
    }

    // replaces the whole file with the given lines (used when we change a record)
    public static void writeLines(String fileName, ArrayList<String> lines) throws HostelException {
        try {
            new File(FOLDER).mkdirs();
            PrintWriter pw = new PrintWriter(new FileWriter(FOLDER + fileName));
            for (int i = 0; i < lines.size(); i++) {
                pw.println(lines.get(i));
            }
            pw.close();
        } catch (IOException e) {
            throw new HostelException("Could not write to the file " + fileName);
        }
    }
}
