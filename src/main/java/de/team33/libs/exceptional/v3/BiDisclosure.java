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
 * @see Disclosing#on(Class)
 * @see Disclosing#disclose(Class, Class)
 * @see Disclosing#disclose(Class, Class, Consumer)
 */
public final class BiDisclosure<R extends RuntimeException, X extends Throwable, Y extends Throwable> {

    private final TriDisclosure<R, X, Y, Y> core;

    BiDisclosure(final Class<R> rClass,
                 final Class<X> xClass, final Class<Y> yClass, final Consumer<? super R> onFallback) {
        this.core = new TriDisclosure<>(rClass, xClass, yClass, yClass, onFallback);
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
        core.run(runnable);
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
        return core.get(supplier);
    }
}
