package ch.zhaw.pm2.napp.fileio.writer;

import java.io.IOException;

/**
 * A custom file creation exception that can occur if file could not be created as expected. Therefore it extends IoException instead of Exception
 *
 * @author buechad1
 */
public class FileCreationException extends IOException {

    /**
     * Constructs a new FileCreationException with the specified detail message.
     *
     * @param message a detail message. The detail message is saved for later retrieval by the getMessage() method.
     */
    public FileCreationException(String message) {
        super(message);
    }
}
