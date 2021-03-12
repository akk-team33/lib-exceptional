package de.team33.libs.exceptional.v4;

import java.util.function.Function;

/**
 * A tool that supports the differentiated handling of an exception.
 * <p>
 * To get an Instance use {@link #of(Throwable)}.
 */
public class Handling<T extends Throwable> {

    private final T subject;
    private final Throwable cause;

    private Handling(final T subject) {
        this.subject = subject;
        this.cause = subject.getCause();
    }

    /**
     * Returns a new instance to handle a given exception.
     *
     * @param subject the exception to be handled
     * @param <T>     the type of the given exception
     */
    public static <T extends Throwable> Handling<T> of(final T subject) {
        return new Handling<>(subject);
    }

    private static <X extends Throwable> void throwIfPresent(final X exception) throws X {
        if (null != exception) {
            throw exception;
        }
    }

    /**
     * Applies a given {@link Function mapping} to the {@linkplain #of(Throwable) associated subject} and throws the
     * result if is it NOT {@code null}. Otherwise this {@link Handling} will be returned.
     *
     * @param mapping A {@link Function} that converts the {@linkplain #of(Throwable) associated subject} to a specific
     *                type of exception to be thrown at that point, or returns {@code null} if handling should continue.
     * @param <X>     The exception type that is intended as a result of the given mapping and that is thrown by this
     *                method, if applicable.
     * @return This handling, which can be continued if no exception has been thrown.
     * @throws X The converted exception, if present.
     * @see #throwMappedCause(Function)
     * @see #reThrowIf(Class)
     */
    public final <X extends Throwable> Handling<T> throwMapped(final Function<? super T, X> mapping) throws X {
        throwIfPresent(mapping.apply(subject));
        return this;
    }

    /**
     * Re-throws the {@linkplain #of(Throwable) associated subject} if it matches the given exception {@code type}.
     * Otherwise this {@link Handling} will be returned.
     *
     * @param type The {@link Class} that represents the type of exception that is expected.
     * @param <X>  The type of exception that is expected and, if applicable, thrown by this method.
     * @return This handling, which can be continued if no exception has been thrown.
     * @throws X the {@linkplain #of(Throwable) associated subject}, cast to the expected type, if applicable.
     * @see #reThrowCauseIf(Class)
     * @see #throwMapped(Function)
     */
    public final <X extends Throwable> Handling<T> reThrowIf(final Class<X> type) throws X {
        throwIfPresent(type.isInstance(subject) ? type.cast(subject) : null);
        return this;
    }

    /**
     * Applies a given {@link Function mapping} to the {@link Throwable#getCause() cause} of the
     * {@linkplain #of(Throwable) associated subject} and throws the result if is it NOT {@code null}.
     * Otherwise this {@link Handling} will be returned.
     *
     * @param mapping A {@link Function} that converts the {@link Throwable#getCause() cause} of the
     *                {@linkplain #of(Throwable) associated subject} to a specific type of exception to be thrown at
     *                that point, or returns {@code null} if handling should continue.
     * @param <X>     The exception type that is intended as a result of the given mapping and that is thrown by this
     *                method, if applicable.
     * @return This handling, which can be continued if no exception has been thrown.
     * @throws X The converted exception, if present.
     * @see #throwMapped(Function)
     * @see #reThrowCauseIf(Class)
     */
    public final <X extends Throwable> Handling<T> throwMappedCause(final Function<Throwable, X> mapping) throws X {
        throwIfPresent(mapping.apply(cause));
        return this;
    }

    /**
     * Re-throws the {@link Throwable#getCause() cause} of the {@linkplain #of(Throwable) associated subject}
     * if it matches the given exception {@code type}. Otherwise this {@link Handling} will be returned.
     *
     * @param type The {@link Class} that represents the type of exception that is expected.
     * @param <X>  The type of exception that is expected and, if applicable, thrown by this method.
     * @return This handling, which can be continued if no exception has been thrown.
     * @throws X the {@linkplain #of(Throwable) associated subject}, cast to the expected type, if applicable.
     * @see #reThrowIf(Class)
     * @see #throwMappedCause(Function)
     */
    public final <X extends Throwable> Handling<T> reThrowCauseIf(final Class<X> type) throws X {
        throwIfPresent(type.isInstance(cause) ? type.cast(cause) : null);
        return this;
    }

    /**
     * Returns the {@linkplain #of(Throwable) associated subject}.
     */
    public final T fallback() {
        return subject;
    }

    /**
     * Returns the {@linkplain #of(Throwable) associated subject} wrapped in an {@link ExpectationException}.
     */
    public final ExpectationException toExpectationException() {
        return new ExpectationException(subject);
    }

    /**
     * Returns the {@linkplain #of(Throwable) associated subject} wrapped in an {@link ExpectationException}.
     */
    public final ExpectationException toExpectationException(final String message) {
        return new ExpectationException(message, subject);
    }
}
