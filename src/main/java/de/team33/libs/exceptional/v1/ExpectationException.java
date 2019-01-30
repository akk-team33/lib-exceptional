package de.team33.libs.exceptional.v1;

public class ExpectationException extends RuntimeException {

    public ExpectationException(final Throwable cause) {
        super("Unexpected: " + cause.getMessage(), cause);
    }
}
