package de.team33.libs.exceptional.v3;

import java.util.function.Supplier;


/**
 * A Tool that can execute methods that may throw a {@link WrappedException} caused by a checked Exception of
 * certain types. If this is the case, the latter will be unwrapped and re-thrown.
 *
 * @see Exposer
 * @see TriExposer
 */
public class BiExposer<X extends Throwable, Y extends Throwable> {

    private final Class<X> xClass;
    private final Class<Y> yClass;

    private BiExposer(final Class<X> xClass, final Class<Y> yClass) {
        this.xClass = xClass;
        this.yClass = yClass;
    }

    /**
     * Returns a new instance that handles given exception types (when wrapped in a {@link WrappedException}).
     */
    public static <X extends Throwable, Y extends Throwable> BiExposer<X, Y> expose(final Class<X> xClass,
                                                                                    final Class<Y> yClass) {
        return new BiExposer<X, Y>(xClass, yClass);
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
     * @throws Y                if the runnable causes a {@link WrappedException}, which in turn is caused by
     *                          an exception of type Y
     * @throws Error            if the runnable causes an {@link Error} or a {@link WrappedException},
     *                          which in turn is caused by an {@link Error}
     * @throws RuntimeException if the runnable causes a {@link RuntimeException} or a
     *                          {@link WrappedException} which in turn is caused by another {@link RuntimeException}
     * @throws WrappedException if the runnable causes a {@link WrappedException} that cannot be unwrapped in
     *                          any of the above ways
     */
    public final void run(final Runnable runnable) throws X, Y {
        get(wrap(runnable));
    }

    /**
     * Immediately executes a supplier and returns its result.
     *
     * @throws X                if the supplier causes a {@link WrappedException}, which in turn is caused by
     *                          an exception of type X
     * @throws Y                if the supplier causes a {@link WrappedException}, which in turn is caused by
     *                          an exception of type Y
     * @throws Error            if the supplier causes an {@link Error} or a {@link WrappedException},
     *                          which in turn is caused by an {@link Error}
     * @throws RuntimeException if the supplier causes a {@link RuntimeException} or a
     *                          {@link WrappedException} which in turn is caused by another {@link RuntimeException}
     * @throws WrappedException if the supplier causes a {@link WrappedException} that cannot be unwrapped in
     *                          any of the above ways
     */
    public final <T> T get(final Supplier<T> supplier) throws X, Y {
        try {
            return supplier.get();
        } catch (final WrappedException caught) {
            throw caught
                    .reThrowCauseIf(Error.class)
                    .reThrowCauseIf(RuntimeException.class)
                    .reThrowCauseIf(xClass)
                    .reThrowCauseIf(yClass);
        }
    }
}
