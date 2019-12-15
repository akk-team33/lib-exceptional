package de.team33.libs.exceptional.v3;

/**
 * A checked {@link Exception} dedicated to wrap other exceptions, especially other checked exceptions.
 */
public class CheckedEnvelope extends Exception {

    public CheckedEnvelope(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CheckedEnvelope(final Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
