package de.team33.libs.exceptional.v1;

import java.util.function.Supplier;


/**
 * Ejects a specific exception type wrapped in a {@link WrappedException}.
 */
public class Ejector<X extends Throwable> {

    private final Class<X> xClass;

    private Ejector(final Class<X> xClass) {
        this.xClass = xClass;
    }

    /**
     * Supplies a new instance that ejects a given exception type.
     */
    public static <T extends Throwable> Ejector<T> eject(final Class<T> xClass) {
        return new Ejector<>(xClass);
    }

    private Supplier<Void> wrap(final Runnable runnable) {
        return () -> {
            runnable.run();
            return null;
        };
    }

    /**
     * Runs a runnable. If a WrappedException occurs, which contains an exception of the associated type,
     * it will be "ejected" (unwrapped and rethrown).
     */
    public final void run(final Runnable runnable) throws X {
        get(wrap(runnable));
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
