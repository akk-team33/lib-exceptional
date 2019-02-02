package de.team33.libs.exceptional.v2;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * A tool that can turn certain functional constructs into others.
 */
public class Wrapper<T, R, X extends Exception> {

    private final XFunction<T, R, ?> delegate;

    private Wrapper(final XFunction<T, R, ?> delegate) {
        this.delegate = delegate;
    }

    /**
     * Wraps an {@link XRunnable} as {@link Runnable} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public static Runnable runnable(final XRunnable<?> xRunnable) {
        return new Wrapper<>(toXFunction(xRunnable)).toRunnable();
    }

    /**
     * Wraps an {@link XConsumer} as {@link Consumer} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public static <T> Consumer<T> consumer(final XConsumer<T, ?> xConsumer) {
        return new Wrapper<>(toXFunction(xConsumer)).toConsumer();
    }

    /**
     * Wraps an {@link XSupplier} as {@link Supplier} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public static <R> Supplier<R> supplier(final XSupplier<R, ?> xSupplier) {
        return new Wrapper<>(toXFunction(xSupplier)).toSupplier();
    }

    /**
     * Wraps an {@link XFunction} as {@link Function} that, when executed, wraps any occurring
     * checked exception as {@link WrappedException}.
     */
    public static <T, R> Function<T, R> function(final XFunction<T, R, ?> xFunction) {
        return new Wrapper<>(xFunction).toFunction();
    }

    private static <X extends Exception> XFunction<Void, Void, X> toXFunction(final XRunnable<X> xRunnable) {
        return t -> {
            xRunnable.run();
            return null;
        };
    }

    private static <T, X extends Exception> XFunction<T, Void, X> toXFunction(final XConsumer<T, X> xConsumer) {
        return t -> {
            xConsumer.accept(t);
            return null;
        };
    }

    private static <T, X extends Exception> XFunction<Void, T, X> toXFunction(final XSupplier<T, X> xSupplier) {
        return t -> xSupplier.get();
    }

    private R exec(final T t) {
        try {
            return delegate.apply(t);
        } catch (final Exception caught) {
            throw new WrappedException(caught);
        }
    }

    private Runnable toRunnable() {
        return () -> exec(null);
    }

    private Consumer<T> toConsumer() {
        return this::exec;
    }

    private Supplier<R> toSupplier() {
        return () -> exec(null);
    }

    private Function<T, R> toFunction() {
        return this::exec;
    }
}
