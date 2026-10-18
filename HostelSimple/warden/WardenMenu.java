package warden;

import common.DataChecker;
import common.FileHelper;
import common.HostelException;
import common.Input;
import common.Menu;
import common.Person;
import java.util.ArrayList;

// Warden side of the project. It also implements the Menu interface.
public class WardenMenu implements Menu {

    public void showMenu() {
        // polymorphism: a Person variable holds a Warden object
        Person p = new Warden("W01", "Hostel Warden");
        System.out.println("\nLogged in as " + p.getRole() + ": " + p.getName());

        while (true) {
            System.out.println("\n===== WARDEN MENU =====");
            System.out.println("1. Night attendance sheet");
            System.out.println("2. Students who have gone home");
            System.out.println("3. View all complaints");
            System.out.println("4. Mark complaint as solved / unsolved");
            System.out.println("0. Back");
            String choice = Input.readLine("Enter your choice: ");

            try {
                if (choice.equals("1")) {
                    attendanceSheet();
                } else if (choice.equals("2")) {
                    studentsAtHome();
                } else if (choice.equals("3")) {
                    viewComplaints();
                } else if (choice.equals("4")) {
                    changeComplaintStatus();
                } else if (choice.equals("0")) {
                    return;
                } else {
                    System.out.println("Invalid choice, try again.");
                }
            } catch (HostelException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // 1. shows bio-data of every student with where he/she is right now
    private void attendanceSheet() throws HostelException {
        ArrayList<String> students = FileHelper.readLines("students.txt");
        if (students.size() == 0) {
            System.out.println("No students are registered yet.");
            return;
        }

        System.out.println("\n----- NIGHT ATTENDANCE SHEET -----");
        for (int i = 0; i < students.size(); i++) {
            String[] parts = students.get(i).split(",");
            // parts: 0 roll, 1 name, 2 room, 3 course, 4 phone, 5 guardian phone
            String roll = parts[0];

            String status;
            if (DataChecker.hasOpenTrip(roll)) {
                status = "GONE HOME";
            } else if (DataChecker.lastGateAction(roll).equals("EXIT")) {
                status = "OUTSIDE";
            } else {
                status = "IN HOSTEL";
            }

            System.out.println(roll + " | " + parts[1] + " | Room " + parts[2] + " | " + parts[3]
                    + " | Phone " + parts[4] + " | Guardian " + parts[5] + " | " + status);
        }
    }

    // 2. list of students who are at home right now
    private void studentsAtHome() throws HostelException {
        ArrayList<String> trips = FileHelper.readLines("trips.txt");
        int count = 0;

        System.out.println("\n----- STUDENTS WHO HAVE GONE HOME -----");
        for (int i = 0; i < trips.size(); i++) {
            String[] parts = trips.get(i).split(",");
            // parts: 0 roll, 1 place, 2 left time, 3 return time
            if (parts[3].equals("-")) {
                String name = DataChecker.getStudentName(parts[0]);
                System.out.println(parts[0] + " | " + name + " | went to " + parts[1]
                        + " | left on " + parts[2].replace("T", " "));
                count++;
            }
        }

        if (count == 0) {
            System.out.println("No student is at home right now.");
        }
    }

    // 3. show all complaints with their status
    private void viewComplaints() throws HostelException {
        ArrayList<String> complaints = FileHelper.readLines("complaints.txt");
        if (complaints.size() == 0) {
            System.out.println("No complaints yet.");
            return;
        }

        System.out.println("\n----- COMPLAINTS -----");
        for (int i = 0; i < complaints.size(); i++) {
            String[] parts = complaints.get(i).split(",");
            // parts: 0 id, 1 roll, 2 complaint, 3 status
            System.out.println("ID " + parts[0] + " | Roll " + parts[1] + " | " + parts[2] + " | " + parts[3]);
        }
    }

    // 4. warden sets a complaint to SOLVED or UNSOLVED
    private void changeComplaintStatus() throws HostelException {
        String id = Input.readRequired("Enter complaint ID: ");

        System.out.println("1. Solved");
        System.out.println("2. Unsolved");
        String choice = Input.readLine("Enter your choice: ");

        String status;
        if (choice.equals("1")) {
            status = "SOLVED";
        } else if (choice.equals("2")) {
            status = "UNSOLVED";
        } else {
            throw new HostelException("Invalid choice.");
        }

        ArrayList<String> complaints = FileHelper.readLines("complaints.txt");
        boolean found = false;
        for (int i = 0; i < complaints.size(); i++) {
            String[] parts = complaints.get(i).split(",");
            if (parts[0].equals(id)) {
                complaints.set(i, parts[0] + "," + parts[1] + "," + parts[2] + "," + status);
                found = true;
            }
        }

        if (!found) {
            throw new HostelException("No complaint found with ID " + id);
        }
        FileHelper.writeLines("complaints.txt", complaints);
        System.out.println("Complaint " + id + " is now marked " + status);
    }
}
