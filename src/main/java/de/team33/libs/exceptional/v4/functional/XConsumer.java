package de.team33.libs.exceptional.v4.functional;

/**
 * A kind of consumer that allows to throw a checked exception.
 *
 * @see java.util.function.Consumer
 */
@FunctionalInterface
public interface XConsumer<T, X extends Exception> {

    /**
     * Performs this operation on the given argument.
     *
     * @throws X if so.
     * @see java.util.function.Consumer#accept(Object)
     */
    void accept(T t) throws X;
}
