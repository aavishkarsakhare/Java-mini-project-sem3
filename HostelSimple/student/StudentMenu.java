package student;

import common.DataChecker;
import common.FileHelper;
import common.HostelException;
import common.Input;
import common.Menu;
import java.time.LocalDateTime;
import java.util.ArrayList;

// Student side of the project. It implements the Menu interface.
public class StudentMenu implements Menu {

    public void showMenu() {
        while (true) {
            System.out.println("\n===== STUDENT MENU =====");
            System.out.println("1. Register (save bio-data)");
            System.out.println("2. Exit from hostel");
            System.out.println("3. Enter into hostel");
            System.out.println("4. Going home");
            System.out.println("5. Back from home");
            System.out.println("6. Reading hall (after 10 pm)");
            System.out.println("7. Submit a complaint");
            System.out.println("0. Back");
            String choice = Input.readLine("Enter your choice: ");

            // exception handling: any HostelException is caught and printed here
            try {
                if (choice.equals("1")) {
                    registerStudent();
                } else if (choice.equals("2")) {
                    exitHostel();
                } else if (choice.equals("3")) {
                    enterHostel();
                } else if (choice.equals("4")) {
                    goingHome();
                } else if (choice.equals("5")) {
                    backFromHome();
                } else if (choice.equals("6")) {
                    readingHall();
                } else if (choice.equals("7")) {
                    submitComplaint();
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

    // 1. save student bio-data
    private void registerStudent() throws HostelException {
        String roll = Input.readRequired("Roll number: ");

        // do not allow the same roll number twice
        ArrayList<String> lines = FileHelper.readLines("students.txt");
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts[0].equalsIgnoreCase(roll)) {
                throw new HostelException("This roll number is already registered.");
            }
        }

        String name = Input.readRequired("Name: ");
        String room = Input.readRequired("Room number: ");
        String course = Input.readRequired("Course: ");
        String phone = Input.readRequired("Phone number: ");
        String guardianPhone = Input.readRequired("Guardian phone number: ");

        Student s = new Student(roll, name, room, course, phone, guardianPhone);
        FileHelper.addLine("students.txt", s.toFileLine());
        Student.registeredInThisRun++;

        System.out.println("Student saved successfully.");
        System.out.println("Students registered in this run: " + Student.registeredInThisRun);
    }

    // 2. student leaves the hostel
    private void exitHostel() throws HostelException {
        String roll = Input.readRequired("Roll number: ");
        String name = DataChecker.getStudentName(roll);

        if (DataChecker.lastGateAction(roll).equals("EXIT")) {
            throw new HostelException(name + " is already outside the hostel.");
        }
        if (DataChecker.isInReadingHall(roll)) {
            throw new HostelException("Please check out of the reading hall first.");
        }

        LocalDateTime time = Input.readTime();
        FileHelper.addLine("gate.txt", roll + ",EXIT," + Input.format(time));
        System.out.println("Exit time saved for " + name);
    }

    // 3. student comes back into the hostel
    private void enterHostel() throws HostelException {
        String roll = Input.readRequired("Roll number: ");
        String name = DataChecker.getStudentName(roll);

        if (DataChecker.hasOpenTrip(roll)) {
            throw new HostelException("You are at home. Use option 5 (Back from home).");
        }
        if (!DataChecker.lastGateAction(roll).equals("EXIT")) {
            throw new HostelException(name + " is already inside the hostel.");
        }

        LocalDateTime time = Input.readTime();
        FileHelper.addLine("gate.txt", roll + ",ENTRY," + Input.format(time));
        System.out.println("Entry time saved for " + name);
    }

    // 4. student goes home
    private void goingHome() throws HostelException {
        String roll = Input.readRequired("Roll number: ");
        String name = DataChecker.getStudentName(roll);

        if (DataChecker.lastGateAction(roll).equals("EXIT")) {
            throw new HostelException(name + " is already outside the hostel.");
        }
        if (DataChecker.isInReadingHall(roll)) {
            throw new HostelException("Please check out of the reading hall first.");
        }

        String place = Input.readRequired("Where are you going: ");
        LocalDateTime time = Input.readTime();

        // trips.txt line: roll,place,leftTime,returnTime  ("-" means not returned yet)
        FileHelper.addLine("trips.txt", roll + "," + place + "," + Input.format(time) + ",-");
        FileHelper.addLine("gate.txt", roll + ",EXIT," + Input.format(time));
        System.out.println("Home trip saved. Have a safe journey, " + name);
    }

    // 5. student returns from home
    private void backFromHome() throws HostelException {
        String roll = Input.readRequired("Roll number: ");
        String name = DataChecker.getStudentName(roll);

        if (!DataChecker.hasOpenTrip(roll)) {
            throw new HostelException(name + " has no running home trip.");
        }

        LocalDateTime time = Input.readTime();

        // find the open trip and put the return time in it
        ArrayList<String> lines = FileHelper.readLines("trips.txt");
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts[0].equalsIgnoreCase(roll) && parts[3].equals("-")) {
                lines.set(i, parts[0] + "," + parts[1] + "," + parts[2] + "," + Input.format(time));
            }
        }
        FileHelper.writeLines("trips.txt", lines);
        FileHelper.addLine("gate.txt", roll + ",ENTRY," + Input.format(time));
        System.out.println("Welcome back, " + name);
    }

    // 6. reading hall use after 10 pm
    private void readingHall() throws HostelException {
        String roll = Input.readRequired("Roll number: ");
        String name = DataChecker.getStudentName(roll);

        System.out.println("1. Check in");
        System.out.println("2. Check out");
        String choice = Input.readLine("Enter your choice: ");

        if (choice.equals("1")) {
            if (DataChecker.lastGateAction(roll).equals("EXIT")) {
                throw new HostelException(name + " is outside the hostel.");
            }
            if (DataChecker.isInReadingHall(roll)) {
                throw new HostelException(name + " is already in the reading hall.");
            }
            LocalDateTime time = Input.readTime();

            // allowed only from 10 pm to 6 am
            int hour = time.getHour();
            if (hour < 22 && hour >= 6) {
                throw new HostelException("Reading hall is recorded only after 10 pm (10 pm to 6 am).");
            }

            // hall.txt line: roll,inTime,outTime  ("-" means still inside)
            FileHelper.addLine("hall.txt", roll + "," + Input.format(time) + ",-");
            System.out.println("Reading hall check-in saved for " + name);

        } else if (choice.equals("2")) {
            if (!DataChecker.isInReadingHall(roll)) {
                throw new HostelException(name + " is not in the reading hall.");
            }
            LocalDateTime time = Input.readTime();

            ArrayList<String> lines = FileHelper.readLines("hall.txt");
            for (int i = 0; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts[0].equalsIgnoreCase(roll) && parts[2].equals("-")) {
                    lines.set(i, parts[0] + "," + parts[1] + "," + Input.format(time));
                }
            }
            FileHelper.writeLines("hall.txt", lines);
            System.out.println("Reading hall check-out saved for " + name);

        } else {
            System.out.println("Invalid choice.");
        }
    }

    // 7. student writes a complaint (it starts as UNSOLVED)
    private void submitComplaint() throws HostelException {
        String roll = Input.readRequired("Roll number: ");
        DataChecker.getStudentName(roll);

        String text = Input.readRequired("Write your complaint: ");

        ArrayList<String> lines = FileHelper.readLines("complaints.txt");
        int id = lines.size() + 1;

        // complaints.txt line: id,roll,complaint,status
        FileHelper.addLine("complaints.txt", id + "," + roll + "," + text + ",UNSOLVED");
        System.out.println("Complaint saved. Your complaint ID is " + id);
    }
}
