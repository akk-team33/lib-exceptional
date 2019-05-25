package de.team33.libs.exceptional.v3;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * A tool that can turn certain functional constructs that may throw checked exceptions into others that do
 * not.
 */
public class Wrapper<T, U, R> {

    private final XBiFunction<T, U, R, ?> delegate;

    private Wrapper(final XBiFunction<T, U, R, ?> delegate) {
        this.delegate = delegate;
    }

    /**
     * Wraps an {@link XRunnable} as {@link Runnable} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public static Runnable runnable(final XRunnable<?> xRunnable) {
        return new Wrapper<>(toBiFunction(xRunnable)).toRunnable();
    }

    /**
     * Wraps an {@link XConsumer} as {@link Consumer} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public static <T> Consumer<T> consumer(final XConsumer<T, ?> xConsumer) {
        return new Wrapper<>(toBiFunction(xConsumer)).toConsumer();
    }

    /**
     * Wraps an {@link XBiConsumer} as {@link BiConsumer} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public static <T, U> BiConsumer<T, U> biConsumer(final XBiConsumer<T, U, ?> xBiConsumer) {
        return new Wrapper<>(toBiFunction(xBiConsumer)).toBiConsumer();
    }

    /**
     * Wraps an {@link XSupplier} as {@link Supplier} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public static <R> Supplier<R> supplier(final XSupplier<R, ?> xSupplier) {
        return new Wrapper<>(toBiFunction(xSupplier)).toSupplier();
    }

    /**
     * Wraps an {@link XFunction} as {@link Function} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public static <T, R> Function<T, R> function(final XFunction<T, R, ?> xFunction) {
        return new Wrapper<>(toBiFunction(xFunction)).toFunction();
    }

    /**
     * Wraps an {@link XBiFunction} as {@link BiFunction} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public static <T, U, R> BiFunction<T, U, R> biFunction(final XBiFunction<T, U, R, ?> xBiFunction) {
        return new Wrapper<>(xBiFunction).toBiFunction();
    }

    private static XBiFunction<Void, Void, Void, ?> toBiFunction(final XRunnable<?> xRunnable) {
        return (t, u) -> {
            xRunnable.run();
            return null;
        };
    }

    private static <T> XBiFunction<T, Void, Void, ?> toBiFunction(final XConsumer<T, ?> xConsumer) {
        return (t, u) -> {
            xConsumer.accept(t);
            return null;
        };
    }

    private static <T, U> XBiFunction<T, U, Void, ?> toBiFunction(final XBiConsumer<T, U, ?> xBiConsumer) {
        return (t, u) -> {
            xBiConsumer.accept(t, u);
            return null;
        };
    }

    private static <T> XBiFunction<Void, Void, T, ?> toBiFunction(final XSupplier<T, ?> xSupplier) {
        return (t, u) -> xSupplier.get();
    }

    private static <T, R> XBiFunction<T, Void, R, ?> toBiFunction(final XFunction<T, R, ?> xFunction) {
        return (t, u) -> xFunction.apply(t);
    }

    private R exec(final T t, final U u) {
        try {
            return delegate.apply(t, u);
        } catch (final RuntimeException caught) {
            throw caught;
        } catch (final Exception caught) {
            throw new WrappedException(caught);
        }
    }

    private Runnable toRunnable() {
        return () -> exec(null, null);
    }

    private Consumer<T> toConsumer() {
        return t -> exec(t, null);
    }

    private BiConsumer<T, U> toBiConsumer() {
        return this::exec;
    }

    private Supplier<R> toSupplier() {
        return () -> exec(null, null);
    }

    private Function<T, R> toFunction() {
        return t -> exec(t, null);
    }

    private BiFunction<T, U, R> toBiFunction() {
        return this::exec;
    }
}
