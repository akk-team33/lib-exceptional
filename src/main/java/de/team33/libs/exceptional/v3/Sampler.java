package de.team33.libs.exceptional.v3;

/**
 * A tool to prove and expose causes of exceptions.
 */
public class Sampler<T extends Throwable> {

    private final T backing;

    /**
     * Initializes a new instance.
     */
    public Sampler(final T backing) {
        this.backing = backing;
    }

    /**
     * Rethrows the cause of the associated Throwable if it matches the given exception type.
     * Otherwise it returns this Sampler.
     */
    public final <X extends Throwable> Sampler<T> reThrowCauseIf(final Class<X> xClass) throws X {
        final Throwable cause = backing.getCause();
        if (xClass.isInstance(cause)) {
            throw xClass.cast(cause);
        }
        return this;
    }

    public final T retrieve() {
        return backing;
    }
}
