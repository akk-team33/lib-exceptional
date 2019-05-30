package de.team33.libs.exceptional.v3;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A tool that can turn certain functional constructs that may throw checked exceptions into others that do
 * not.
 *
 * @see Wrapping
 */
public class Wrapper<X extends RuntimeException> {

    public static final Wrapper<WrappedException> DEFAULT = new Wrapper<>(WrappedException::new);

    private final BiFunction<String, Throwable, X> wrapping;

    /**
     * Initiates a new instance with a function that provides a wrapping runtime exception.
     */
    public Wrapper(final BiFunction<String, Throwable, X> wrapping) {
        this.wrapping = wrapping;
    }

    private static XBiFunction<Void, Void, Void, ?> toXBiFunction(final XRunnable<?> xRunnable) {
        return (t, u) -> {
            xRunnable.run();
            return null;
        };
    }

    private static <T> XBiFunction<T, Void, Void, ?> toXBiFunction(final XConsumer<T, ?> xConsumer) {
        return (t, u) -> {
            xConsumer.accept(t);
            return null;
        };
    }

    private static <T, U> XBiFunction<T, U, Void, ?> toXBiFunction(final XBiConsumer<T, U, ?> xBiConsumer) {
        return (t, u) -> {
            xBiConsumer.accept(t, u);
            return null;
        };
    }

    private static <R> XBiFunction<Void, Void, R, ?> toXBiFunction(final XSupplier<R, ?> xSupplier) {
        return (t, u) -> xSupplier.get();
    }

    private static <T, R> XBiFunction<T, Void, R, ?> toXBiFunction(final XFunction<T, R, ?> xFunction) {
        return (t, u) -> xFunction.apply(t);
    }

    private <T, U, R> R call(final XBiFunction<T, U, R, ?> xBiFunction, final T t, final U u) {
        try {
            return xBiFunction.apply(t, u);
        } catch (final Error | RuntimeException caught) {
            throw caught;
        } catch (final Throwable caught) {
            throw wrapping.apply(caught.getMessage(), caught);
        }
    }

    /**
     * Wraps an {@link XRunnable} as {@link Runnable} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public final Runnable runnable(final XRunnable<?> xRunnable) {
        return () -> call(toXBiFunction(xRunnable), null, null);
    }

    /**
     * Wraps an {@link XConsumer} as {@link Consumer} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public final <T> Consumer<T> consumer(final XConsumer<T, ?> xConsumer) {
        return t -> call(toXBiFunction(xConsumer), t, null);
    }

    /**
     * Wraps an {@link XBiConsumer} as {@link BiConsumer} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public final <T, U> BiConsumer<T, U> biConsumer(final XBiConsumer<T, U, ?> xBiConsumer) {
        return (t, u) -> call(toXBiFunction(xBiConsumer), t, u);
    }

    /**
     * Wraps an {@link XSupplier} as {@link Supplier} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public final <R> Supplier<R> supplier(final XSupplier<R, ?> xSupplier) {
        return () -> call(toXBiFunction(xSupplier), null, null);
    }

    /**
     * Wraps an {@link XFunction} as {@link Function} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public final <T, R> Function<T, R> function(final XFunction<T, R, ?> xFunction) {
        return t -> call(toXBiFunction(xFunction), t, null);
    }

    public final <T, U, R> BiFunction<T, U, R> biFunction(final XBiFunction<T, U, R, ?> xBiFunction) {
        return (t, u) -> call(xBiFunction, t, u);
    }
}
