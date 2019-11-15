package de.team33.libs.exceptional.v4;

public class RuntimeEnvelope extends RuntimeException {
    
    public RuntimeEnvelope(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RuntimeEnvelope(final Throwable cause) {
        super(cause.getMessage(), cause);
    }
    
    public Review<RuntimeEnvelope> review() {
        return new Review<>(this);
    }
}
