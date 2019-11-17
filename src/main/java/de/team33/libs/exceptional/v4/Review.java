package de.team33.libs.exceptional.v4;

import java.util.function.Function;

public class Review<T extends Throwable> {

    private final T subject;
    private final Throwable cause;

    Review(final T subject) {
        this.subject = subject;
        this.cause = subject.getCause();
    }

    /**
     * Re-throws the associated {@code subject} if it matches the given exception type.
     * Otherwise it returns this {@link Review}.
     */
    public final <X extends T> Review<T> reThrowIf(final Class<X> xClass) throws X {
        if (xClass.isInstance(subject)) {
            throw xClass.cast(subject);
        }
        return this;
    }

    /**
     * Re-throws the {@link Throwable#getCause() cause} of the associated {@code subject} if it matches the given
     * exception type. Otherwise it returns this {@link Review}.
     */
    public final <X extends Throwable> Review<T> reThrowCauseIf(final Class<X> xClass) throws X {
        if (xClass.isInstance(cause)) {
            throw xClass.cast(cause);
        }
        return this;
    }

    /**
     * Returns the associated {@code subject}.
     */
    public final <X extends Throwable> X fallback(final Function<? super T, X> mapping) {
        return mapping.apply(subject);
    }

    /**
     * Returns the associated {@code subject}.
     */
    public final T subject() {
        return subject;
    }
}
