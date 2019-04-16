package de.team33.libs.exceptional.v2;

import java.util.function.Supplier;


/**
 * A Tool that can execute methods that may throw a {@link WrappedException} based on a checked Exception of
 * certain types. If this is the case, the latter will be unwrapped and re-thrown.
 */
public class TriInspector<X extends Throwable, Y extends Throwable, Z extends Throwable> {

    private final Class<X> xClass;
    private final Class<Y> yClass;
    private final Class<Z> zClass;

    private TriInspector(final Class<X> xClass, final Class<Y> yClass, final Class<Z> zClass) {
        this.xClass = xClass;
        this.yClass = yClass;
        this.zClass = zClass;
    }

    /**
     * Returns a new instance that handles given exception types (wrapped in a {@link WrappedException}).
     */
    public static <
            X extends Throwable,
            Y extends Throwable,
            Z extends Throwable> TriInspector<X, Y, Z> expect(final Class<X> xClass,
                                                              final Class<Y> yClass,
                                                              final Class<Z> zClass) {
        return new TriInspector<X, Y, Z>(xClass, yClass, zClass);
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
     * @throws Z                if the runnable causes a {@link WrappedException}, which in turn is caused by
     *                          an exception of type Z
     * @throws Error            if the runnable causes an {@link Error} or a {@link WrappedException},
     *                          which in turn is caused by an {@link Error}
     * @throws RuntimeException if the runnable causes a {@link RuntimeException} or a
     *                          {@link WrappedException}, which in turn is caused by a
     *                          {@link RuntimeException}
     * @throws WrappedException if the runnable causes a {@link WrappedException} that cannot be unwrapped in
     *                          any of the above ways
     */
    public final void run(final Runnable runnable) throws X, Y, Z {
        get(wrap(runnable));
    }

    /**
     * Immediately executes a supplier and returns its result.
     *
     * @throws X                if the supplier causes a {@link WrappedException}, which in turn is caused by
     *                          an exception of type X
     * @throws Y                if the supplier causes a {@link WrappedException}, which in turn is caused by
     *                          an exception of type Y
     * @throws Z                if the supplier causes a {@link WrappedException}, which in turn is caused by
     *                          an exception of type Z
     * @throws Error            if the supplier causes an {@link Error} or a {@link WrappedException},
     *                          which in turn is caused by an {@link Error}
     * @throws RuntimeException if the supplier causes a {@link RuntimeException} or a
     *                          {@link WrappedException}, which in turn is caused by a
     *                          {@link RuntimeException}
     * @throws WrappedException if the supplier causes a {@link WrappedException} that cannot be unwrapped in
     *                          any of the above ways
     */
    public final <T> T get(final Supplier<T> supplier) throws X, Y, Z {
        try {
            return supplier.get();
        } catch (final WrappedException caught) {
            throw caught
                    .reThrowCauseIf(Error.class)
                    .reThrowCauseIf(RuntimeException.class)
                    .reThrowCauseIf(xClass)
                    .reThrowCauseIf(yClass)
                    .reThrowCauseIf(zClass);
        }
    }
}
