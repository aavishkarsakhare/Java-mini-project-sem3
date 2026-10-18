package common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

// Takes all input from the keyboard. Everything is static so we
// do not need to create an object and we use only one Scanner.
public class Input {
    private static Scanner sc = new Scanner(System.in);

    // reads one line. Commas are replaced because our files use commas
    public static String readLine(String message) {
        System.out.print(message);
        if (!sc.hasNextLine()) {
            System.out.println("\nInput ended. Bye.");
            System.exit(0);
        }
        String text = sc.nextLine().trim();
        return text.replace(",", " ");
    }

    // same as readLine but the user cannot leave it empty
    public static String readRequired(String message) throws HostelException {
        String text = readLine(message);
        if (text.equals("")) {
            throw new HostelException("This field cannot be empty.");
        }
        return text;
    }

    // asks for a time. If the user just presses Enter, current time is used
    public static LocalDateTime readTime() throws HostelException {
        String text = readLine("Enter time as HH:mm (press Enter for current time): ");
        if (text.equals("")) {
            return LocalDateTime.now().withSecond(0).withNano(0);
        }
        try {
            LocalTime time = LocalTime.parse(text);
            return LocalDateTime.of(LocalDate.now(), time);
        } catch (DateTimeParseException e) {
            throw new HostelException("Wrong time format. Use HH:mm like 22:30");
        }
    }

    // converts a time to text that we can save in the file
    public static String format(LocalDateTime time) {
        return time.toString();
    }
}
