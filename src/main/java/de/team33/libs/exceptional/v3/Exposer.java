package de.team33.libs.exceptional.v3;

import java.util.function.Supplier;

/**
 * @deprecated Use {@linkplain Disclosing} or {@link Insight} within an explicit {@code catch()} block instead.
 */
@Deprecated
public final class Exposer<X extends Throwable> {

    private final Class<X> xClass;

    private Exposer(final Class<X> xClass) {
        this.xClass = xClass;
    }

    /**
     * Returns a new instance that handles a given exception type (when wrapped in a {@link WrappedException}).
     */
    public static <T extends Throwable> Exposer<T> expose(final Class<T> xClass) {
        return new Exposer<>(xClass);
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
     * @throws RuntimeException if the runnable causes a {@link RuntimeException} or a
     *                          {@link WrappedException} which in turn is caused by another {@link RuntimeException}
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
     * @throws RuntimeException if the supplier causes a {@link RuntimeException} or a
     *                          {@link WrappedException} which in turn is caused by another {@link RuntimeException}
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
