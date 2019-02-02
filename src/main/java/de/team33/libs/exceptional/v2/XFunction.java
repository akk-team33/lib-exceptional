package de.team33.libs.exceptional.v2;

/**
 * A kind of function that allows to throw a checked exception.
 *
 * @see java.util.function.Function
 */
@FunctionalInterface
public interface XFunction<T, R, X extends Exception> {

    /**
     * Performs this operation on the given argument.
     *
     * @throws X if so.
     * @see java.util.function.Function#apply(Object)
     */
    R apply(final T t) throws X;
}
