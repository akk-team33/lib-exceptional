package de.team33.libs.exceptional.v4.functional;

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
    R apply(T t) throws X;
}
