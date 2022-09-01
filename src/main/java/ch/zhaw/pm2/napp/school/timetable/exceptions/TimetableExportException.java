package ch.zhaw.pm2.napp.school.timetable.exceptions;

/**
 * This is a custom exception thrown if exporting timetables did not work for specified reason
 *
 * @author buechad1
 */
public class TimetableExportException extends Exception {

    /**
     * Constructs a new TimetableExportException with the specified detail message.
     *
     * @param message a detail message. The detail message is saved for later retrieval by the getMessage() method.
     */
    public TimetableExportException(String message) {
        super(message);
    }

}
