package de.team33.libs.exceptional.v2;

import java.util.function.Supplier;


/**
 * A Tool that can execute methods that may throw a {@link WrappedException} based on a checked Exception of
 * a certain type. If this is the case, the latter will be unwrapped and re-thrown.
 */
public class Inspector<X extends Throwable> {

    private final Class<X> xClass;

    private Inspector(final Class<X> xClass) {
        this.xClass = xClass;
    }

    /**
     * Returns a new instance that handles a given exception type (wrapped in a {@link WrappedException}).
     */
    public static <T extends Throwable> Inspector<T> expect(final Class<T> xClass) {
        return new Inspector<>(xClass);
    }

    private static Supplier<Void> wrap(final Runnable runnable) {
        return () -> {
            runnable.run();
            return null;
        };
    }

    /**
     * Immediately executes a runnable.
     *
     * @throws X                if the runnable causes a {@link WrappedException}, which in turn is caused by
     *                          an exception of type X
     * @throws Error            if the runnable causes an {@link Error} or a {@link WrappedException},
     *                          which in turn is caused by an {@link Error}
     * @throws RuntimeException if the runnable causes a {@link RuntimeException} or a,
     *                          {@link WrappedException} which in turn is caused by a {@link RuntimeException}
     * @throws WrappedException if the runnable causes a {@link WrappedException} that cannot be unwrapped in
     *                          any of the above ways
     */
    public final void run(final Runnable runnable) throws X {
        get(wrap(runnable));
    }

    /**
     * Immediately executes a supplier and returns its result.
     *
     * @throws X                if the supplier causes a {@link WrappedException}, which in turn is caused by
     *                          an exception of type X
     * @throws Error            if the supplier causes an {@link Error} or a {@link WrappedException},
     *                          which in turn is caused by an {@link Error}
     * @throws RuntimeException if the supplier causes a {@link RuntimeException} or a,
     *                          {@link WrappedException} which in turn is caused by a {@link RuntimeException}
     * @throws WrappedException if the supplier causes a {@link WrappedException} that cannot be unwrapped in
     *                          any of the above ways
     */
    public final <T> T get(final Supplier<T> supplier) throws X {
        try {
            return supplier.get();
        } catch (final WrappedException caught) {
            throw caught
                    .reThrowCauseIf(Error.class)
                    .reThrowCauseIf(RuntimeException.class)
                    .reThrowCauseIf(xClass);
        }
    }
}
