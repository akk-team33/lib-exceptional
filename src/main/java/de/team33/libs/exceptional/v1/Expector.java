package de.team33.libs.exceptional.v1;

import java.util.function.Supplier;


/**
 * Expects a specific exception type wrapped as a {@link WrappedException}.
 */
public class Expector<X extends Throwable> {

    private final Class<X> xClass;

    private Expector(final Class<X> xClass) {
        this.xClass = xClass;
    }

    /**
     * Provides a new instance that expects a given exception type.
     */
    public static <T extends Throwable> Expector<T> expect(final Class<T> xClass) {
        return new Expector<>(xClass);
    }

    /**
     * Führt ein
     */
    public final void run(final Runnable runnable) throws X {
        get(wrap(runnable));
    }

    private Supplier<Object> wrap(final Runnable runnable) {
        return () -> {
            runnable.run();
            return null;
        };
    }

    public final <T> T get(final Supplier<T> supplier) throws X {
        try {
            return supplier.get();
        } catch (final WrappedException caught) {
            caught
                    .reThrowCauseIf(Error.class)
                    .reThrowCauseIf(RuntimeException.class)
                    .reThrowCauseIf(xClass);
            throw new ExpectationException(caught.getCause());
        }
    }
}
