package de.team33.libs.exceptional.v3;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>A tool to disclose three specific types of (typically checked) exceptions wrapped by
 * ({@linkplain Throwable#getCause() cause} of) a specific type of {@link RuntimeException}s.</p>
 *
 * @param <R> The specific type of enclosing {@link RuntimeException}s to be looked at.
 * @param <X> The first specific type of exceptions to be disclosed.
 * @param <Y> The second specific type of exceptions to be disclosed.
 * @param <Z> The third specific type of exceptions to be disclosed.
 * @see Disclosing#disclose(Class, Class, Class)
 * @see Disclosing#disclose(Class, Class, Class, Consumer)
 */
public final class TriDisclosure<
        R extends RuntimeException,
        X extends Throwable,
        Y extends Throwable,
        Z extends Throwable> {

    private final Class<R> rClass;
    private final Class<X> xClass;
    private final Class<Y> yClass;
    private final Class<Z> zClass;
    private final Consumer<? super R> onFallback;

    TriDisclosure(final Class<R> rClass,
                  final Class<X> xClass,
                  final Class<Y> yClass,
                  final Class<Z> zClass,
                  final Consumer<? super R> onFallback) {
        this.rClass = rClass;
        this.xClass = xClass;
        this.yClass = yClass;
        this.zClass = zClass;
        this.onFallback = onFallback;
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
    public final void run(final Runnable runnable) throws X, Y, Z {
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
    public final <T> T get(final Supplier<T> supplier) throws X, Y, Z {
        try {
            return supplier.get();
        } catch (final RuntimeException caught) {
            throw rClass.isInstance(caught) ? insight(rClass.cast(caught)) : caught;
        }
    }

    private R insight(final R caught) throws X, Y, Z {
        return fallback(Insight.into(caught)
                               .reThrowCauseIf(xClass)
                               .reThrowCauseIf(yClass)
                               .reThrowCauseIf(zClass)
                               .fallback());
    }

    private R fallback(final R fallback) {
        onFallback.accept(fallback);
        return fallback;
    }
}
