package ch.zhaw.pm2.napp.fileio.loader.exception;

/**
 * Exception that is thrown upon bad csv values.
 *
 * @author wartminc
 * @version 1.0.0
 */
public class BadCsvFormatException extends Exception {
    /**
     * Exception is thrown if the Values of a csv File does not match the requirements.
     * @param message – the detail message. The detail message is saved for later retrieval by the getMessage() method.
     */
    public BadCsvFormatException(String message) {
        super(message);
    }
}