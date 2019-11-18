package de.team33.libs.exceptional.v4;

import javax.xml.stream.StreamFilter;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Supports a differentiated handling of an exception.
 */
public class Review<T extends Throwable> {

    private static final Predicate ALWAYS = s -> true;
    private static final Function KEEP = s -> s;

    private final T subject;
    private final Throwable cause;

    private Review(final T subject) {
        this.subject = subject;
        this.cause = subject.getCause();
    }

    public static <T extends Throwable> Review<T> of(final T subject) {
        return new Review<>(subject);
    }

    private static <R> Function<Object, R> cast(final Class<R> rClass) {
        return rClass::cast;
    }

    private static <R> Predicate<Object> isInstance(final Class<R> rClass) {
        return rClass::isInstance;
    }

    private static <S, X extends Throwable> void throwTransformationIf(final S subject,
                                                                       final Predicate<S> condition,
                                                                       final Function<S, X> transformation) throws X {
        if (condition.test(subject)) {
            throw transformation.apply(subject);
        }
    }

    /**
     * Throws a transformation of the associated {@code subject} if it matches the given type and the given
     * {@link Predicate}. Otherwise it returns this {@link Review} for further aspects.
     */
    public final <X extends Throwable> Review<T> throwIf(final Predicate<? super T> condition,
                                                         final Function<? super T, X> transformation) throws X {
        if (condition.test(subject)) {
            throw transformation.apply(subject);
        }
        return this;
    }

    /**
     * Throws a transformation of the associated {@code subject} if it matches the given type.
     * Otherwise it returns this {@link Review} for further aspects.
     */
    public final <S, X extends Throwable> Review<T> throwIf(final Class<S> type,
                                                            final Function<? super S, X> transformation) throws X {
        final Function<? super S, ? extends X> t = transformation;
        return throwIf(isInstance(type), cast(type).andThen(t));
    }

    /**
     * Re-throws the associated {@code subject} if it matches the given exception type.
     * Otherwise it returns this {@link Review}.
     */
    public final <X extends T> Review<T> reThrowIf(final Class<X> type) throws X {
        //noinspection unchecked
        return throwIf(isInstance(type), cast(type));
    }

    /**
     * Re-throws the {@link Throwable#getCause() cause} of the associated {@code subject} if it matches the given
     * exception type. Otherwise it returns this {@link Review}.
     */
    public final <X extends Throwable> Review<T> reThrowCauseIf(final Class<X> xClass) throws X {
        if (xClass.isInstance(cause)) {
            throw xClass.cast(cause);
        }
        return this;
    }

    /**
     * Returns the associated {@code subject}.
     */
    public final <X extends Throwable> X fallback(final Function<? super T, X> mapping) {
        return mapping.apply(subject);
    }

    /**
     * Returns the associated {@code subject}.
     */
    public final T subject() {
        return subject;
    }
}
