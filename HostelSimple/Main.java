import common.Input;
import common.Menu;
import student.StudentMenu;
import warden.WardenMenu;

// Start the project by running this file
public class Main {
    public static void main(String[] args) {
        System.out.println("===== HOSTEL MANAGEMENT SYSTEM =====");

        while (true) {
            System.out.println("\n1. Student");
            System.out.println("2. Warden");
            System.out.println("0. Exit");
            String choice = Input.readLine("Enter your choice: ");

            // polymorphism: one Menu variable can hold StudentMenu or WardenMenu
            Menu menu = null;
            if (choice.equals("1")) {
                menu = new StudentMenu();
            } else if (choice.equals("2")) {
                menu = new WardenMenu();
            } else if (choice.equals("0")) {
                System.out.println("Thank you. Bye!");
                break;
            } else {
                System.out.println("Invalid choice, try again.");
            }

            if (menu != null) {
                menu.showMenu();
            }
        }
    }
}
