package de.team33.libs.exceptional.v3;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


/**
 * A utility that can turn certain functional constructs that may throw several, or unspecific, checked exceptions
 * into others that wraps such exceptions as {@link RuntimeEnvelope} or {@link CheckedEnvelope}.
 */
public final class Wrapping {

    @SuppressWarnings("deprecation") // preliminary wraps into a WrappedException for compatibility reasons
    private static final RuntimeWrapper<RuntimeEnvelope> RUNTIME_WRAPPER = new RuntimeWrapper<>(WrappedException::new);
    private static final CheckedWrapper<CheckedEnvelope> CHECKED_WRAPPER = new CheckedWrapper<>(CheckedEnvelope.class,
                                                                                                CheckedEnvelope::new);

    private Wrapping() {
    }

    /**
     * Wraps an {@link XRunnable} as {@link Runnable} that, when executed, wraps any occurring
     * checked exception as {@link RuntimeEnvelope}.
     */
    public static Runnable runnable(final XRunnable<?> xRunnable) {
        return RUNTIME_WRAPPER.runnable(xRunnable);
    }

    /**
     * Wraps an {@link XConsumer} as {@link Consumer} that, when executed, wraps any occurring
     * checked exception as {@link RuntimeEnvelope}.
     */
    public static <T> Consumer<T> consumer(final XConsumer<T, ?> xConsumer) {
        return RUNTIME_WRAPPER.consumer(xConsumer);
    }

    /**
     * Wraps an {@link XBiConsumer} as {@link BiConsumer} that, when executed, wraps any occurring
     * checked exception as {@link RuntimeEnvelope}.
     */
    public static <T, U> BiConsumer<T, U> biConsumer(final XBiConsumer<T, U, ?> xBiConsumer) {
        return RUNTIME_WRAPPER.biConsumer(xBiConsumer);
    }

    /**
     * Wraps an {@link XSupplier} as {@link Supplier} that, when executed, wraps any occurring
     * checked exception as {@link RuntimeEnvelope}.
     */
    public static <R> Supplier<R> supplier(final XSupplier<R, ?> xSupplier) {
        return RUNTIME_WRAPPER.supplier(xSupplier);
    }

    /**
     * Wraps an {@link XPredicate} as {@link Predicate} that, when executed, wraps any occurring
     * checked exception as {@link RuntimeEnvelope}.
     */
    public static <T> Predicate<T> predicate(final XPredicate<T, ?> xPredicate) {
        return RUNTIME_WRAPPER.predicate(xPredicate);
    }

    /**
     * Wraps an {@link XBiPredicate} as {@link BiPredicate} that, when executed, wraps any occurring
     * checked exception as {@link RuntimeEnvelope}.
     */
    public static <T, U> BiPredicate<T, U> biPredicate(final XBiPredicate<T, U, ?> xBiPredicate) {
        return RUNTIME_WRAPPER.biPredicate(xBiPredicate);
    }

    /**
     * Wraps an {@link XFunction} as {@link Function} that, when executed, wraps any occurring
     * checked exception as {@link RuntimeEnvelope}.
     */
    public static <T, R> Function<T, R> function(final XFunction<T, R, ?> xFunction) {
        return RUNTIME_WRAPPER.function(xFunction);
    }

    /**
     * Wraps an {@link XBiFunction} as {@link BiFunction} that, when executed, wraps any occurring
     * checked exception as {@link RuntimeEnvelope}.
     */
    public static <T, U, R> BiFunction<T, U, R> biFunction(final XBiFunction<T, U, R, ?> xBiFunction) {
        return RUNTIME_WRAPPER.biFunction(xBiFunction);
    }

    /**
     * Wraps an {@link XRunnable} that may throw an unspecific checked exception as {@link XRunnable} that,
     * when executed, wraps any occurring checked exception as {@link CheckedEnvelope}.
     */
    public static XRunnable<CheckedEnvelope> xRunnable(final XRunnable<?> xRunnable) {
        return CHECKED_WRAPPER.xRunnable(xRunnable);
    }

    /**
     * Wraps an {@link XConsumer} that may throw an unspecific checked exception as {@link XConsumer} that,
     * when executed, wraps any occurring checked exception as {@link CheckedEnvelope}.
     */
    public static <T> XConsumer<T, CheckedEnvelope> xConsumer(final XConsumer<T, ?> xConsumer) {
        return CHECKED_WRAPPER.xConsumer(xConsumer);
    }

    /**
     * Wraps an {@link XBiConsumer} that may throw an unspecific checked exception as {@link XBiConsumer} that,
     * when executed, wraps any occurring checked exception as {@link CheckedEnvelope}.
     */
    public static <T, U> XBiConsumer<T, U, CheckedEnvelope> xBiConsumer(final XBiConsumer<T, U, ?> xBiConsumer) {
        return CHECKED_WRAPPER.xBiConsumer(xBiConsumer);
    }

    /**
     * Wraps an {@link XSupplier} that may throw an unspecific checked exception as {@link XSupplier} that,
     * when executed, wraps any occurring checked exception as {@link CheckedEnvelope}.
     */
    public static <R> XSupplier<R, CheckedEnvelope> xSupplier(final XSupplier<R, ?> xSupplier) {
        return CHECKED_WRAPPER.xSupplier(xSupplier);
    }

    /**
     * Wraps an {@link XPredicate} as {@link Predicate} that, when executed, wraps any occurring
     * checked exception as {@link RuntimeEnvelope}.
     */
    public static <T> XPredicate<T, CheckedEnvelope> xPredicate(final XPredicate<T, ?> xPredicate) {
        return CHECKED_WRAPPER.xPredicate(xPredicate);
    }

    /**
     * Wraps an {@link XBiPredicate} as {@link BiPredicate} that, when executed, wraps any occurring
     * checked exception as {@link RuntimeEnvelope}.
     */
    public static <T, U> XBiPredicate<T, U, CheckedEnvelope> xBiPredicate(final XBiPredicate<T, U, ?> xBiPredicate) {
        return CHECKED_WRAPPER.xBiPredicate(xBiPredicate);
    }

    /**
     * Wraps an {@link XFunction} that may throw an unspecific checked exception as {@link XFunction} that,
     * when executed, wraps any occurring checked exception as {@link CheckedEnvelope}.
     */
    public static <T, R> XFunction<T, R, CheckedEnvelope> xFunction(final XFunction<T, R, ?> xFunction) {
        return CHECKED_WRAPPER.xFunction(xFunction);
    }

    /**
     * Wraps an {@link XBiFunction} that may throw an unspecific checked exception as {@link XBiFunction} that,
     * when executed, wraps any occurring checked exception as {@link CheckedEnvelope}.
     */
    public static <T, U, R> XBiFunction<T, U, R, CheckedEnvelope> xBiFunction(final XBiFunction<T, U, R, ?> xBiFunction) {
        return CHECKED_WRAPPER.xBiFunction(xBiFunction);
    }
}
