package de.team33.libs.exceptional.v3;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>A tool to disclose a specific type of (typically checked) exceptions wrapped by
 * ({@linkplain Throwable#getCause() cause} of) a specific type of {@link RuntimeException}s.</p>
 *
 * @param <R> The specific type of enclosing {@link RuntimeException}s to be looked at.
 * @param <X> The specific type of exceptions to be disclosed.
 * @see Disclosing#on(Class)
 * @see Disclosing#disclose(Class)
 * @see Disclosing#disclose(Class, Consumer)
 */
public final class Disclosure<R extends RuntimeException, X extends Throwable> {

    private final CoreDisclosure<R, X, X, X> core;

    Disclosure(final Class<R> rClass, final Class<X> xClass, final Consumer<? super R> onFallback) {
        this.core = new CoreDisclosure<>(rClass, xClass, xClass, xClass, onFallback);
    }

    /**
     * Immediately executes a {@link Runnable}.
     *
     * @throws X if the {@link Runnable} causes a {@link RuntimeException} of type {@code <R>},
     *           which in turn is caused by an exception of type {@code <X>}.
     * @throws R if the {@link Runnable} causes a {@link RuntimeException} of type {@code <R>},
     *           which is NOT caused by an exception of type {@code <X>}.
     */
    public final void run(final Runnable runnable) throws X {
        core.run(runnable);
    }

    /**
     * Immediately executes a {@link Supplier} and returns its result.
     *
     * @throws X if the {@link Supplier} causes a {@link RuntimeException} of type {@code <R>},
     *           which in turn is caused by an exception of type {@code <X>}.
     * @throws R if the {@link Supplier} causes a {@link RuntimeException} of type {@code <R>},
     *           which is NOT caused by an exception of type {@code <X>}.
     */
    public final <T> T get(final Supplier<T> supplier) throws X {
        return core.get(supplier);
    }
}
