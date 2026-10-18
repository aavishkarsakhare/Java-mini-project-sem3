package student;

import common.Person;

// Student extends Person (inheritance)
public class Student extends Person {
    private String room;
    private String course;
    private String phone;
    private String guardianPhone;

    // static variable: shared by all objects, counts students registered in this run
    public static int registeredInThisRun = 0;

    public Student(String rollNo, String name, String room, String course,
                   String phone, String guardianPhone) {
        super(rollNo, name);
        this.room = room;
        this.course = course;
        this.phone = phone;
        this.guardianPhone = guardianPhone;
    }

    public String getRole() {
        return "Student";
    }

    // the line which is saved in students.txt
    public String toFileLine() {
        return id + "," + name + "," + room + "," + course + "," + phone + "," + guardianPhone;
    }
}
