package de.team33.libs.exceptional.v2;

/**
 * A kind of consumer that allows to throw a checked exception.
 *
 * @see java.util.function.BiConsumer
 */
@FunctionalInterface
public interface XBiConsumer<T, U, X extends Exception> {

    /**
     * Performs this operation on the given argument.
     *
     * @throws X if so.
     * @see java.util.function.BiConsumer#accept(Object, Object)
     */
    void accept(T t, U u) throws X;
}
