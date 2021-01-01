package de.raidcraft.rcelevator.exceptions;

/**
 * Author: Philip
 * Date: 17.09.12 - 22:48
 * Description:
 */
public class NoTargetException extends Throwable {
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public NoTargetException(String message) {
        super(message);
    }
}
