package de.team33.libs.exceptional.v3;

/**
 * A {@link RuntimeException} dedicated to wrap other exceptions, especially checked exceptions.
 */
public class RuntimeEnvelope extends RuntimeException {

    public RuntimeEnvelope(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RuntimeEnvelope(final Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
