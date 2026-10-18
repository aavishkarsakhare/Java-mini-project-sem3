package common;

import java.util.ArrayList;

// Small helper methods used by both the student and the warden menus
public class DataChecker {

    // returns the student's name, or throws an error if roll number is not registered
    public static String getStudentName(String roll) throws HostelException {
        ArrayList<String> lines = FileHelper.readLines("students.txt");
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts[0].equalsIgnoreCase(roll)) {
                return parts[1];
            }
        }
        throw new HostelException("Roll number " + roll + " is not registered. Please register first.");
    }

    // returns "EXIT" or "ENTRY" (the last gate record of the student) or "" if none
    public static String lastGateAction(String roll) throws HostelException {
        ArrayList<String> lines = FileHelper.readLines("gate.txt");
        String last = "";
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts[0].equalsIgnoreCase(roll)) {
                last = parts[1];
            }
        }
        return last;
    }

    // true if the student has gone home and not come back yet
    public static boolean hasOpenTrip(String roll) throws HostelException {
        ArrayList<String> lines = FileHelper.readLines("trips.txt");
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts[0].equalsIgnoreCase(roll) && parts[3].equals("-")) {
                return true;
            }
        }
        return false;
    }

    // true if the student is inside the reading hall right now
    public static boolean isInReadingHall(String roll) throws HostelException {
        ArrayList<String> lines = FileHelper.readLines("hall.txt");
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts[0].equalsIgnoreCase(roll) && parts[2].equals("-")) {
                return true;
            }
        }
        return false;
    }
}
