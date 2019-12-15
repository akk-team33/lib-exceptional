package de.team33.libs.exceptional.v3;

import java.util.function.Function;

/**
 * A tool that supports the differentiated handling of exceptions.
 */
public class Insight<T extends Throwable> {

    private final T subject;
    private final Throwable cause;

    private Insight(final T subject) {
        this.subject = subject;
        this.cause = subject.getCause();
    }

    /**
     * Initiates a {@link Insight} of the given {@code subject}.
     */
    public static <T extends Throwable> Insight<T> into(final T subject) {
        return new Insight<>(subject);
    }

    private static <X extends Throwable> void throwIfPresent(final X exception) throws X {
        if (null != exception) {
            throw exception;
        }
    }

    /**
     * <p>Applies the given {@code mapping} to the {@linkplain #into(Throwable) associated subject} and throws the result
     * if is it NOT {@code null}. Otherwise this {@link Insight} will be returned.</p>
     *
     * <p>This method is used to re-specify an exception. For example, if an exception was caught as a very common
     * type, e.g. an {@link Exception}. At the same time, a suitable mapping to the respective exception can be
     * applied with this variant.</p>
     *
     * @see #reThrowIf(Class)
     */
    public final <X extends Throwable> Insight<T> throwMapped(final Function<? super T, X> mapping) throws X {
        throwIfPresent(mapping.apply(subject));
        return this;
    }

    /**
     * <p>Re-throws the {@linkplain #into(Throwable) associated subject} if it matches the given exception {@code type}.
     * Otherwise this {@link Insight} will be returned.</p>
     *
     * <p>This method is used to re-specify an exception. For example, if an exception was caught as a very common
     * type, e.g. an {@link Exception}.</p>
     *
     * @see #throwMapped(Function)
     */
    public final <X extends Throwable> Insight<T> reThrowIf(final Class<X> type) throws X {
        throwIfPresent(type.isInstance(subject) ? type.cast(subject) : null);
        return this;
    }

    /**
     * <p>Applies the given {@code mapping} to the {@link Throwable#getCause() cause} of the
     * {@linkplain #into(Throwable) associated subject} and throws the result if is it NOT {@code null}.
     * Otherwise this {@link Insight} will be returned.</p>
     *
     * <p>This method is used to bring the cause of an exception back to the foreground. For example,
     * if an exception was previously caught and encapsulated in another exception for technical reasons,
     * e.g. a {@link RuntimeException}. At the same time, a suitable mapping to the respective exception can be
     * applied with this variant.</p>
     *
     * @see #reThrowCauseIf(Class)
     */
    public final <X extends Throwable> Insight<T> throwMappedCause(final Function<Throwable, X> mapping) throws X {
        throwIfPresent(mapping.apply(cause));
        return this;
    }

    /**
     * <p>Re-throws the {@link Throwable#getCause() cause} of the {@linkplain #into(Throwable) associated subject}
     * if it matches the given exception {@code type}. Otherwise this {@link Insight} will be returned.</p>
     *
     * <p>This method is used to bring the cause of an exception back to the foreground. For example,
     * if an exception was previously caught and encapsulated in another exception for technical reasons,
     * e.g. a {@link RuntimeException}.</p>
     *
     * @see #throwMappedCause(Function)
     */
    public final <X extends Throwable> Insight<T> reThrowCauseIf(final Class<X> type) throws X {
        throwIfPresent(type.isInstance(cause) ? type.cast(cause) : null);
        return this;
    }

    /**
     * Returns the associated {@code subject}.
     */
    public final T fallback() {
        return subject;
    }
}