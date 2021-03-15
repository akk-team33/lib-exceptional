package de.team33.libs.exceptional.v4;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A tool that supports the differentiated revision of an exception, especially its {@link Throwable#getCause() cause}.
 * <p>
 * In general, there is little need for a special tool for differentiated handling of exceptions.
 * The possibilities of Java’s own language elements (try-catch) are compact, expressive and efficient.
 * <p>
 * The situation is different if the {@link Throwable#getCause() cause of an exception} is to be handled in a
 * differentiated manner. This often results in extensive and sometimes difficult to read code.
 * In any case, the regular code takes a back seat.
 * <p>
 * For example, in order not to have to deal with it all the time, we would like to temporarily wrap checked exceptions
 * into unchecked exceptions. However, we would then like to bring the checked exceptions back to the fore in order to
 * enforce a structured approach. A {@link Revision} supports the latter, as the following code example shows:
 * <pre>
 * try {
 *     doSomethingThatMayThrowAWrappedException();
 * } catch (final WrappedException caught) {
 *     // We want to unwrap the cause of the caught exception and rethrow
 *     // it as a certain type of exception that meets our expectations ...
 *     throw Revision.of(caught)
 *                   .reThrowWhen(IOException.class)
 *                   .reThrowWhen(SQLException.class)
 *                   .reThrowWhen(URISyntaxException.class)
 *                   // Technically, it could happen that our expectations are not met.
 *                   // To be on the safe side, this should lead to a meaningful exception ...
 *                   .mapped(ExpectationException::new);
 * }
 * </pre>
 * <p>
 * To get an Instance use {@link #of(Throwable)}.
 */
public class Revision<T extends Throwable> {

    private final T subject;
    private final Throwable cause;

    private Revision(final T subject) {
        this.subject = subject;
        this.cause = subject.getCause();
    }

    /**
     * Returns a new instance to handle a given exception.
     *
     * @param subject the exception to be handled
     * @param <T>     the type of the given exception
     */
    public static <T extends Throwable> Revision<T> of(final T subject) {
        return new Revision<>(subject);
    }

    private static <X extends Throwable> void throwIfPresent(final X exception) throws X {
        if (null != exception) {
            throw exception;
        }
    }

    /**
     * Applies a given {@link Function mapping} to the {@linkplain #of(Throwable) associated exception} and throws the
     * result if it is NOT {@code null}. Otherwise this {@link Revision} will be returned.
     *
     * @param mapping A {@link Function} that converts the {@linkplain #of(Throwable) associated exception} to a specific
     *                type of exception to be thrown at that point, or returns {@code null} if handling should continue.
     * @param <X>     The exception type that is intended as a result of the given mapping and that is thrown by this
     *                method, if applicable.
     * @return This handling, which can be continued if no exception has been thrown.
     * @throws X The converted exception, if present.
     * @see #throwMappedCause(Function)
     */
    public final <X extends Throwable> Revision<T> throwMapped(final Function<? super T, X> mapping) throws X {
        throwIfPresent(mapping.apply(subject));
        return this;
    }

    /**
     * Applies a given {@link Function mapping} to the {@link Throwable#getCause() cause} of the
     * {@linkplain #of(Throwable) associated exception} and throws the result if it is NOT {@code null}.
     * Otherwise this {@link Revision} will be returned.
     *
     * @param mapping A {@link Function} that converts the {@link Throwable#getCause() cause} of the
     *                {@linkplain #of(Throwable) associated exception} to a specific type of exception to be thrown at
     *                that point, or returns {@code null} if handling should continue.
     * @param <X>     The exception type that is intended as a result of the given mapping and that is thrown by this
     *                method, if applicable.
     * @return This handling, which can be continued if no exception has been thrown.
     * @throws X The converted exception, if present.
     * @see #throwMapped(Function)
     * @see #reThrowWhen(Class)
     */
    public final <X extends Throwable> Revision<T> throwMappedCause(final Function<Throwable, X> mapping) throws X {
        throwIfPresent(mapping.apply(cause));
        return this;
    }

    /**
     * Re-throws the {@link Throwable#getCause() cause} (!) of the {@linkplain #of(Throwable) original exception}
     * if it matches the given exception type. Otherwise this {@link Revision} will be returned.
     *
     * @param type The {@link Class} that represents the type of exception that is expected.
     * @param <X>  The type of exception that is expected and, if applicable, thrown by this method.
     * @return This {@link Revision}, which may be continued if no exception has been thrown.
     * @throws X The {@link Throwable#getCause() cause} of the {@linkplain #of(Throwable) original exception},
     *           cast to the expected type, if applicable.
     * @see #throwMappedCause(Function)
     */
    public final <X extends Throwable> Revision<T> reThrowWhen(final Class<X> type) throws X {
        throwIfPresent(type.isInstance(cause) ? type.cast(cause) : null);
        return this;
    }

    /**
     * Returns the {@linkplain #of(Throwable) associated exception}.
     */
    public final T fallback() {
        return subject;
    }

    /**
     * Applies a given {@link Function mapping} to the {@linkplain #of(Throwable) associated exception} and returns the
     * result.
     */
    public final <X extends Throwable> X mapped(final Function<? super T, X> mapping) {
        return mapping.apply(subject);
    }

    /**
     * Applies a given {@link Function mapping} to the {@link Throwable#getCause() cause} of the
     * {@linkplain #of(Throwable) associated exception} and returns the result.
     */
    public final <X extends Throwable> X mappedCause(final Function<Throwable, X> mapping) {
        return mapping.apply(cause);
    }

    public final Conditional when(final Predicate<Throwable> condition) {
        return new Conditional(condition);
    }

    public final class Conditional {

        private final Predicate<Throwable> condition;

        private Conditional(final Predicate<Throwable> condition) {
            this.condition = condition;
        }

        public <X extends Throwable> Revision<T> thenThrow(final Function<Throwable, X> method) throws X {
            if (condition.test(cause)) {
                throw method.apply(cause);
            }
            return Revision.this;
        }
    }
}
