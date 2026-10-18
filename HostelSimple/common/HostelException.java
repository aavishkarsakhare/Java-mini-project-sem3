package common;

// Our own exception class, used for all hostel related errors
public class HostelException extends Exception {
    public HostelException(String message) {
        super(message);
    }
}
