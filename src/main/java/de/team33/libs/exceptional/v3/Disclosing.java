package de.team33.libs.exceptional.v3;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>A tool to disclose a specific type of (typically checked) exceptions wrapped by
 * ({@linkplain Throwable#getCause() cause} of) a specific type of {@link RuntimeException}s.</p>
 *
 * @param <R> The specific type of enclosing {@link RuntimeException}s to be looked at.
 * @param <X> The specific type of exceptions to be disclosed.
 */
public final class Disclosing<R extends RuntimeException, X extends Throwable> {

    private final Class<R> rClass;
    private final Class<X> xClass;
    private final Consumer<? super R> onFallback;

    /**
     * Initializes a new instance.
     *
     * @param rClass     The specific type of enclosing {@link RuntimeException}s to be looked at.
     * @param xClass     The specific type of exceptions to be disclosed.
     * @param onFallback A method that is applied to an (alleged) enclosing exception if it actually is not caused by
     *                   an exception of the expected type.
     */
    public Disclosing(final Class<R> rClass, final Class<X> xClass, final Consumer<? super R> onFallback) {
        this.rClass = rClass;
        this.xClass = xClass;
        this.onFallback = onFallback;
    }

    public static <R extends RuntimeException> Stage<R> on(final Class<R> rClass) {
        return new Stage<R>() {
            @Override
            public <X extends Throwable> Disclosing<R, X> disclose(final Class<X> xClass,
                                                                   final Consumer<? super R> onFallback) {
                return new Disclosing<>(rClass, xClass, onFallback);
            }
        };
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
        } catch (final RuntimeException caught) {
            throw rClass.isInstance(caught) ? insight(rClass.cast(caught)) : caught;
        }
    }

    private R insight(final R caught) throws X {
        return fallback(Insight.into(caught)
                               .reThrowCauseIf(xClass)
                               .fallback());
    }

    private R fallback(final R fallback) {
        onFallback.accept(fallback);
        return fallback;
    }

    public interface Stage<R extends RuntimeException> {
        default <X extends Throwable> Disclosing<R, X> disclose(Class<X> xClass) {
            return disclose(xClass, fallback -> {});
        }

        <X extends Throwable> Disclosing<R, X> disclose(Class<X> xClass, Consumer<? super R> onFallback);
    }
}
