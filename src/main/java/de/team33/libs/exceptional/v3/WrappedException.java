package de.team33.libs.exceptional.v3;

/**
 * @deprecated Use {@link RuntimeEnvelope} instead.
 */
@Deprecated
public class WrappedException extends RuntimeEnvelope {

    /**
     * Initializes a new instance.
     */
    public WrappedException(final Throwable cause) {
        super(cause.getMessage(), cause);
    }

    /**
     * Initializes a new instance.
     */
    public WrappedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Rethrows the cause of this WrappedException if it matches the given exception type.
     * Otherwise it returns this WrappedException.
     */
    public final <X extends Throwable> WrappedException reThrowCauseIf(final Class<X> xClass) throws X {
        return Insight.into(this)
                      .reThrowCauseIf(xClass)
                      .fallback();
    }
}
