package ch.zhaw.pm2.napp.school.building.exception;

/**
 * Is thrown if a room is looked for with a certain Identifier but can not be found in a reference Collection.
 *
 * @author wartmnic
 * @version 1.0.0
 */
public class NoSuchRoomException extends Exception {
    /**
     * Exception is thrown if a Room, searched for with Identifier, could not be found
     * @param message – the detail message. The detail message is saved for later retrieval by the getMessage() method.
     */
    public NoSuchRoomException(String message) {
        super(message);
    }
}