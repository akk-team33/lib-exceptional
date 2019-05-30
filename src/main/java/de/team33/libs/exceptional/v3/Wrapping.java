package de.team33.libs.exceptional.v3;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * A utility that can turn certain functional constructs that may throw checked exceptions into others that do
 * not.
 *
 * @see Wrapper
 */
public final class Wrapping {

    private Wrapping() {
    }

    /**
     * Wraps an {@link XRunnable} as {@link Runnable} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public static Runnable runnable(final XRunnable<?> xRunnable) {
        return Wrapper.DEFAULT.runnable(xRunnable);
    }

    /**
     * Wraps an {@link XConsumer} as {@link Consumer} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public static <T> Consumer<T> consumer(final XConsumer<T, ?> xConsumer) {
        return Wrapper.DEFAULT.consumer(xConsumer);
    }

    /**
     * Wraps an {@link XBiConsumer} as {@link BiConsumer} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public static <T, U> BiConsumer<T, U> biConsumer(final XBiConsumer<T, U, ?> xBiConsumer) {
        return Wrapper.DEFAULT.biConsumer(xBiConsumer);
    }

    /**
     * Wraps an {@link XSupplier} as {@link Supplier} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public static <R> Supplier<R> supplier(final XSupplier<R, ?> xSupplier) {
        return Wrapper.DEFAULT.supplier(xSupplier);
    }

    /**
     * Wraps an {@link XFunction} as {@link Function} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public static <T, R> Function<T, R> function(final XFunction<T, R, ?> xFunction) {
        return Wrapper.DEFAULT.function(xFunction);
    }

    /**
     * Wraps an {@link XBiFunction} as {@link BiFunction} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public static <T, U, R> BiFunction<T, U, R> biFunction(final XBiFunction<T, U, R, ?> xBiFunction) {
        return Wrapper.DEFAULT.biFunction(xBiFunction);
    }
}
