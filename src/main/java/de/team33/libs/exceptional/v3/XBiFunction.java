package de.team33.libs.exceptional.v3;

/**
 * A kind of function that allows to throw a checked exception.
 *
 * @see java.util.function.BiFunction
 */
@FunctionalInterface
public interface XBiFunction<T, U, R, X extends Exception> {

    /**
     * Performs this operation on the given argument.
     *
     * @throws X if so.
     * @see java.util.function.BiFunction#apply(Object, Object)
     */
    R apply(T t, U u) throws X;
}
