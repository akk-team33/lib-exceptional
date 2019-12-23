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
 * @see Disclosing#on(Class)
 * @see Disclosing#disclose(Class, Class, Class)
 * @see Disclosing#disclose(Class, Class, Class, Consumer)
 */
public final class TriDisclosure<
        R extends RuntimeException,
        X extends Throwable,
        Y extends Throwable,
        Z extends Throwable> {

    private final CoreDisclosure<R, X, Y, Z> core;

    TriDisclosure(final Class<R> r, final Class<X> x, final Class<Y> y, final Class<Z> z,
                  final Consumer<? super R> onFallback) {
        this.core = new CoreDisclosure<>(r, x, y, z, onFallback);
    }

    /**
     * Immediately executes a {@link Runnable}.
     *
     * @throws X if the {@link Runnable} causes a {@link RuntimeException} of type {@code <R>},
     *           which in turn is caused by an exception of type {@code <X>}.
     * @throws Y if the {@link Runnable} causes a {@link RuntimeException} of type {@code <R>},
     *           which in turn is caused by an exception of type {@code <Y>}.
     * @throws Z if the {@link Runnable} causes a {@link RuntimeException} of type {@code <R>},
     *           which in turn is caused by an exception of type {@code <Z>}.
     * @throws R if the {@link Runnable} causes a {@link RuntimeException} of type {@code <R>},
     *           which is NOT caused by an exception of type {@code <X>}, {@code <Y>} or {@code <Z>}.
     */
    public final void run(final Runnable runnable) throws X, Y, Z {
        core.run(runnable);
    }

    /**
     * Immediately executes a {@link Supplier} and returns its result.
     *
     * @throws X if the {@link Supplier} causes a {@link RuntimeException} of type {@code <R>},
     *           which in turn is caused by an exception of type {@code <X>}.
     * @throws Y if the {@link Supplier} causes a {@link RuntimeException} of type {@code <R>},
     *           which in turn is caused by an exception of type {@code <Y>}.
     * @throws Z if the {@link Runnable} causes a {@link RuntimeException} of type {@code <R>},
     *           which in turn is caused by an exception of type {@code <Z>}.
     * @throws R if the {@link Runnable} causes a {@link RuntimeException} of type {@code <R>},
     *           which is NOT caused by an exception of type {@code <X>}, {@code <Y>} or {@code <Z>}.
     */
    public final <T> T get(final Supplier<T> supplier) throws X, Y, Z {
        return core.get(supplier);
    }
}
