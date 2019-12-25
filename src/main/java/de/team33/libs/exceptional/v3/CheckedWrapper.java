package de.team33.libs.exceptional.v3;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * A tool that can be used to turn certain functional constructs that can throw different or unspecific checked
 * exceptions into those that catch such exceptions, wrap them in a specific checked exception, and throw them again.
 */
public class CheckedWrapper<X extends Exception> {

    private final Class<X> xClass;
    private final BiFunction<String, Throwable, X> wrapping;

    /**
     * Initiates a new instance with a function that provides a wrapping runtime exception.
     */
    public CheckedWrapper(final Class<X> xClass, final BiFunction<String, Throwable, X> wrapping) {
        this.xClass = xClass;
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

    private static <T> XBiFunction<T, Void, Boolean, ?> toXBiFunction(final XPredicate<T, ?> xPredicate) {
        return (t, u) -> xPredicate.test(t);
    }

    private static  <T, U> XBiFunction<T, U, Boolean, ?> toXBiFunction(final XBiPredicate<T, U, ?> xBiPredicate) {
        return xBiPredicate::test;
    }

    private static <T, R> XBiFunction<T, Void, R, ?> toXBiFunction(final XFunction<T, R, ?> xFunction) {
        return (t, u) -> xFunction.apply(t);
    }

    private <T, U, R> R call(final XBiFunction<T, U, R, ?> xBiFunction, final T t, final U u) throws X {
        try {
            return xBiFunction.apply(t, u);
        } catch (final Error | RuntimeException caught) {
            throw caught;
        } catch (final Throwable caught) {
            Insight.into(caught)
                   .reThrowIf(xClass);
            throw wrapping.apply(caught.getMessage(), caught);
        }
    }

    /**
     * Wraps an {@link XRunnable} that may throw an unspecific checked exception as {@link XRunnable} that,
     * when executed, wraps any occurring checked exception as specific checked exception.
     */
    public final XRunnable<X> xRunnable(final XRunnable<?> xRunnable) {
        return () -> call(toXBiFunction(xRunnable), null, null);
    }

    /**
     * Wraps an {@link XConsumer} that may throw an unspecific checked exception as {@link XConsumer} that,
     * when executed, wraps any occurring checked exception as specific checked exception.
     */
    public final <T> XConsumer<T, X> xConsumer(final XConsumer<T, ?> xConsumer) {
        return t -> call(toXBiFunction(xConsumer), t, null);
    }

    /**
     * Wraps an {@link XBiConsumer} that may throw an unspecific checked exception as {@link XBiConsumer} that,
     * when executed, wraps any occurring checked exception as a specific checked exception.
     */
    public final <T, U> XBiConsumer<T, U, X> xBiConsumer(final XBiConsumer<T, U, ?> xBiConsumer) {
        return (t, u) -> call(toXBiFunction(xBiConsumer), t, u);
    }

    /**
     * Wraps an {@link XSupplier} that may throw an unspecific checked exception as {@link XSupplier} that,
     * when executed, wraps any occurring checked exception as a specific checked exception.
     */
    public final <R> XSupplier<R, X> xSupplier(final XSupplier<R, ?> xSupplier) {
        return () -> call(toXBiFunction(xSupplier), null, null);
    }

    /**
     * Wraps an {@link XPredicate} that may throw an unspecific checked exception as {@link Predicate} that,
     * when executed, wraps any occurring checked exception as specific unchecked exception.
     */
    public final <T> XPredicate<T, X> xPredicate(final XPredicate<T, ?> xPredicate) {
        return t -> call(toXBiFunction(xPredicate), t, null);
    }

    /**
     * Wraps an {@link XBiPredicate} that may throw an unspecific checked exception as {@link BiPredicate} that,
     * when executed, wraps any occurring checked exception as a specific unchecked exception.
     */
    public <T, U> XBiPredicate<T, U, X> xBiPredicate(final XBiPredicate<T, U, ?> xBiPredicate) {
        return (t, u) -> call(toXBiFunction(xBiPredicate), t, u);
    }

    /**
     * Wraps an {@link XFunction} that may throw an unspecific checked exception as {@link XFunction} that,
     * when executed, wraps any occurring checked exception as a specific checked exception.
     */
    public final <T, R> XFunction<T, R, X> xFunction(final XFunction<T, R, ?> xFunction) {
        return t -> call(toXBiFunction(xFunction), t, null);
    }

    /**
     * Wraps an {@link XBiFunction} that may throw an unspecific checked exception as {@link XBiFunction} that,
     * when executed, wraps any occurring checked exception as a specific checked exception.
     */
    public final <T, U, R> XBiFunction<T, U, R, X> xBiFunction(final XBiFunction<T, U, R, ?> xBiFunction) {
        return (t, u) -> call(xBiFunction, t, u);
    }
}
