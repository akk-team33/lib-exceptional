package de.team33.libs.exceptional.v4;

public class Review<T extends Throwable> {

    private final T subject;
    private final Throwable cause;

    Review(final T subject) {
        this.subject = subject;
        this.cause = subject.getCause();
    }

    /**
     * Re-throws the {@link Throwable#getCause() cause} of the associated {@link Throwable} if it matches the given
     * exception type. Otherwise it returns this {@link Review}.
     */
    public final <X extends Throwable> Review<T> reThrowCauseIf(final Class<X> xClass) throws X {
        if (xClass.isInstance(cause)) {
            throw xClass.cast(cause);
        }
        return this;
    }

    /**
     * Returns the associated {@link Throwable}.
     */
    public final T subject() {
        return subject;
    }
}
