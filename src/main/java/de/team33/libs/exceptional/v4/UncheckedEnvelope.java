package de.team33.libs.exceptional.v4;

public class UncheckedEnvelope extends RuntimeException {
    
    public UncheckedEnvelope(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UncheckedEnvelope(final Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
