package de.team33.libs.exceptional.v4;

public class CheckedEnvelope extends Exception {

    public CheckedEnvelope(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CheckedEnvelope(final Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public Review<CheckedEnvelope> review() {
        return Review.of(this);
    }
}
