package de.team33.libs.exceptional.v3;

/**
 * A RuntimeException used to wrap checked exceptions where it is not allowed to throw or pass them directly.
 */
public class WrappedException extends RuntimeException {

    /**
     * Initializes a new instance.
     */
    public WrappedException(final Throwable cause) {
        super(cause.getMessage(), cause);
    }

    /**
     * Rethrows the cause of this WrappedException if it matches the given exception type.
     * Otherwise it returns this WrappedException.
     */
    public final <X extends Throwable> WrappedException reThrowCauseIf(final Class<X> xClass) throws X {
        final Throwable cause = getCause();
        if (xClass.isInstance(cause)) {
            throw xClass.cast(cause);
        }
        return this;
    }
}
