package de.team33.libs.exceptional.v4.functional;

/**
 * A kind of supplier that allows to throw a checked exception.
 *
 * @see java.util.function.Supplier
 */
@FunctionalInterface
public interface XSupplier<T, X extends Exception> {

    /**
     * Performs this operation on the given argument.
     *
     * @throws X if so.
     * @see java.util.function.Supplier#get()
     */
    T get() throws X;
}
