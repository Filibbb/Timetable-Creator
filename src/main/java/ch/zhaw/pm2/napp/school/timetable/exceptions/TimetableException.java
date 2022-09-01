package ch.zhaw.pm2.napp.school.timetable.exceptions;

/**
 * This class is a custom exception thrown when timetable generation fails.
 *
 * @author fupat002
 */
public class TimetableException extends Exception {

    /**
     * Constructs a new TimetableException with the specified detail message.
     *
     * @param message a detail message. The detail message is saved for later retrieval by the getMessage() method.
     */
    public TimetableException(String message) {
        super(message);
    }

}
