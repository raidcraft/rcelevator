package de.raidcraft.rcelevator.exceptions;

/**
 * Author: Philip
 * Date: 17.09.12 - 22:48
 * Description:
 */
public class WrongSignFormatException extends Throwable {
    public WrongSignFormatException() {
        super("Ein oder mehrere Schild-Parameter sind falsch!");
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public WrongSignFormatException(String message) {
        super(message);
    }
}
