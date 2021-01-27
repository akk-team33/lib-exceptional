package de.team33.libs.exceptional.v4;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import de.team33.libs.exceptional.v4.functional.XBiConsumer;
import de.team33.libs.exceptional.v4.functional.XBiFunction;
import de.team33.libs.exceptional.v4.functional.XBiPredicate;
import de.team33.libs.exceptional.v4.functional.XConsumer;
import de.team33.libs.exceptional.v4.functional.XFunction;
import de.team33.libs.exceptional.v4.functional.XPredicate;
import de.team33.libs.exceptional.v4.functional.XRunnable;
import de.team33.libs.exceptional.v4.functional.XSupplier;


/**
 * A tool that can convert certain functional constructs that may throw checked exceptions (e.g. {@link XFunction})
 * into more common constructs (e.g. {@link Function}) that will wrap such exceptions in unchecked exceptions.
 */
public final class FunctionalConverter {

    private final BiFunction<String, Exception, RuntimeException> wrapping;

    private FunctionalConverter(final BiFunction<String, Exception, RuntimeException> wrapping) {
        this.wrapping = wrapping;
    }

    /**
     * Returns a new instance using a given wrapping method.
     *
     * @param wrapping A {@link BiFunction} that returns a {@link RuntimeException} that uses a given {@link String}
     *                 as message and a given {@link Exception} as cause.
     */
    public static FunctionalConverter using(final BiFunction<String, Exception, RuntimeException> wrapping) {
        return new FunctionalConverter(wrapping);
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

    private static <T> XBiFunction<T, Void, Boolean, ?> toXBiFunction(final XPredicate<T, ?> xPredicate) {
        return (t, u) -> xPredicate.test(t);
    }

    private static <T, U> XBiFunction<T, U, Boolean, ?> toXBiFunction(final XBiPredicate<T, U, ?> xBiPredicate) {
        return xBiPredicate::test;
    }

    private static <T, R> XBiFunction<T, Void, R, ?> toXBiFunction(final XFunction<T, R, ?> xFunction) {
        return (t, u) -> xFunction.apply(t);
    }

    private <T, U, R> R call(final XBiFunction<T, U, R, ?> xBiFunction, final T t, final U u) {
        try {
            return xBiFunction.apply(t, u);
        } catch (final RuntimeException caught) {
            throw caught;
        } catch (final Exception caught) {
            throw wrapping.apply(caught.getMessage(), caught);
        }
    }

    /**
     * Wraps an {@link XRunnable} that may throw a checked exception as {@link Runnable} that,
     * when executed, wraps any occurring checked exception as specific unchecked exception.
     */
    public final Runnable runnable(final XRunnable<?> xRunnable) {
        final XBiFunction<Void, Void, Void, ?> xBiFunction = toXBiFunction(xRunnable);
        return () -> call(xBiFunction, null, null);
    }

    /**
     * Wraps an {@link XConsumer} that may throw a checked exception as {@link Consumer} that,
     * when executed, wraps any occurring checked exception as specific unchecked exception.
     */
    public final <T> Consumer<T> consumer(final XConsumer<T, ?> xConsumer) {
        final XBiFunction<T, Void, Void, ?> xBiFunction = toXBiFunction(xConsumer);
        return t -> call(xBiFunction, t, null);
    }

    /**
     * Wraps an {@link XBiConsumer} that may throw a checked exception as {@link BiConsumer} that,
     * when executed, wraps any occurring checked exception as specific unchecked exception.
     */
    public final <T, U> BiConsumer<T, U> biConsumer(final XBiConsumer<T, U, ?> xBiConsumer) {
        final XBiFunction<T, U, Void, ?> xBiFunction = toXBiFunction(xBiConsumer);
        return (t, u) -> call(xBiFunction, t, u);
    }

    /**
     * Wraps an {@link XSupplier} that may throw a checked exception as {@link Supplier} that,
     * when executed, wraps any occurring checked exception as specific unchecked exception.
     */
    public final <R> Supplier<R> supplier(final XSupplier<R, ?> xSupplier) {
        final XBiFunction<Void, Void, R, ?> xBiFunction = toXBiFunction(xSupplier);
        return () -> call(xBiFunction, null, null);
    }

    /**
     * Wraps an {@link XPredicate} that may throw a checked exception as {@link Predicate} that,
     * when executed, wraps any occurring checked exception as specific unchecked exception.
     */
    public final <T> Predicate<T> predicate(final XPredicate<T, ?> xPredicate) {
        final XBiFunction<T, Void, Boolean, ?> xBiFunction = toXBiFunction(xPredicate);
        return t -> call(xBiFunction, t, null);
    }

    /**
     * Wraps an {@link XBiPredicate} that may throw a checked exception as {@link BiPredicate} that,
     * when executed, wraps any occurring checked exception as a specific unchecked exception.
     */
    public <T, U> BiPredicate<T, U> biPredicate(final XBiPredicate<T, U, ?> xBiPredicate) {
        final XBiFunction<T, U, Boolean, ?> xBiFunction = toXBiFunction(xBiPredicate);
        return (t, u) -> call(xBiFunction, t, u);
    }

    /**
     * Wraps an {@link XFunction} that may throw a checked exception as {@link Function} that,
     * when executed, wraps any occurring checked exception as specific unchecked exception.
     */
    public final <T, R> Function<T, R> function(final XFunction<T, R, ?> xFunction) {
        final XBiFunction<T, Void, R, ?> xBiFunction = toXBiFunction(xFunction);
        return t -> call(xBiFunction, t, null);
    }

    /**
     * Wraps an {@link XBiFunction} that may throw a checked exception as {@link BiFunction} that,
     * when executed, wraps any occurring checked exception as a specific unchecked exception.
     */
    public final <T, U, R> BiFunction<T, U, R> biFunction(final XBiFunction<T, U, R, ?> xBiFunction) {
        return (t, u) -> call(xBiFunction, t, u);
    }
}
