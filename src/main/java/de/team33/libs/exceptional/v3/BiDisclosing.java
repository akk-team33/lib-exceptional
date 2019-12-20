package de.team33.libs.exceptional.v3;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>A tool to disclose two specific types of (typically checked) exceptions wrapped by
 * ({@linkplain Throwable#getCause() cause} of) a specific type of {@link RuntimeException}s.</p>
 *
 * @param <R> The specific type of enclosing {@link RuntimeException}s to be looked at.
 * @param <X> The first specific type of exceptions to be disclosed.
 * @param <Y> The other specific type of exceptions to be disclosed.
 */
public final class BiDisclosing<R extends RuntimeException, X extends Throwable, Y extends Throwable> {

    private final Class<R> rClass;
    private final Class<X> xClass;
    private final Class<Y> yClass;
    private final Consumer<? super R> onFallback;

    /**
     * Initializes a new instance.
     *
     * @param rClass     The specific type of enclosing {@link RuntimeException}s to be looked at.
     * @param xClass     The first specific type of exceptions to be disclosed.
     * @param yClass     The other specific type of exceptions to be disclosed.
     * @param onFallback A method that is applied to an (alleged) enclosing exception if it actually is not caused by
     *                   an exception of the expected type.
     */
    public BiDisclosing(final Class<R> rClass,
                        final Class<X> xClass, final Class<Y> yClass, final Consumer<? super R> onFallback) {
        this.rClass = rClass;
        this.xClass = xClass;
        this.yClass = yClass;
        this.onFallback = onFallback;
    }

    public static <R extends RuntimeException> Stage<R> on(final Class<R> rClass) {
        return new Stage<R>() {
            @Override
            public <X extends Throwable, Y extends Throwable> BiDisclosing<R, X, Y> disclose(final Class<X> xClass,
                                                                                             final Class<Y> yClass,
                                                                                             final Consumer<? super R>
                                                                                                     onFallback) {
                return new BiDisclosing<>(rClass, xClass, yClass, onFallback);
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
     * Immediately executes a {@link Runnable}.
     *
     * @throws X if the {@link Runnable} causes a {@link RuntimeException} of type {@code <R>},
     *           which in turn is caused by an exception of type {@code <X>}.
     * @throws Y if the {@link Runnable} causes a {@link RuntimeException} of type {@code <R>},
     *           which in turn is caused by an exception of type {@code <Y>}.
     * @throws R if the {@link Runnable} causes a {@link RuntimeException} of type {@code <R>},
     *           which is NOT caused by an exception of type {@code <X>} or {@code <Y>}.
     */
    public final void run(final Runnable runnable) throws X, Y {
        get(wrap(runnable));
    }

    /**
     * Immediately executes a {@link Supplier} and returns its result.
     *
     * @throws X if the {@link Supplier} causes a {@link RuntimeException} of type {@code <R>},
     *           which in turn is caused by an exception of type {@code <X>}.
     * @throws Y if the {@link Supplier} causes a {@link RuntimeException} of type {@code <R>},
     *           which in turn is caused by an exception of type {@code <Y>}.
     * @throws R if the {@link Supplier} causes a {@link RuntimeException} of type {@code <R>},
     *           which is NOT caused by an exception of type {@code <X>} or {@code <Y>}.
     */
    public final <T> T get(final Supplier<T> supplier) throws X, Y {
        try {
            return supplier.get();
        } catch (final RuntimeException caught) {
            throw rClass.isInstance(caught) ? insight(rClass.cast(caught)) : caught;
        }
    }

    private R insight(final R caught) throws X, Y {
        return fallback(Insight.into(caught)
                               .reThrowCauseIf(xClass)
                               .reThrowCauseIf(yClass)
                               .fallback());
    }

    private R fallback(final R fallback) {
        onFallback.accept(fallback);
        return fallback;
    }

    public interface Stage<R extends RuntimeException> {
        default <X extends Throwable, Y extends Throwable> BiDisclosing<R, X, Y> disclose(final Class<X> xClass,
                                                                                          final Class<Y> yClass) {
            return disclose(xClass, yClass, fallback -> {
            });
        }

        <X extends Throwable, Y extends Throwable> BiDisclosing<R, X, Y> disclose(Class<X> xClass,
                                                                                  Class<Y> yClass,
                                                                                  Consumer<? super R> onFallback);
    }
}
