package de.team33.libs.exceptional.v4;

/**
 * An unchecked exception that is used to signal an unexpected state, particularly an unexpected exception.
 */
public class ExpectationException extends RuntimeException {

    /**
     * Initializes a new instance with the given message.
     */
    public ExpectationException(final String message) {
        super(message);
    }

    /**
     * Initializes a new instance with the given message and cause.
     */
    public ExpectationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Initializes a new instance with the given cause and its {@link Throwable#getMessage() message}.
     */
    public ExpectationException(final Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
