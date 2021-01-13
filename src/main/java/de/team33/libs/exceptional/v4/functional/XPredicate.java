package de.team33.libs.exceptional.v4.functional;

/**
 * A kind of predicate that allows to throw a checked exception.
 *
 * @see java.util.function.Predicate
 */
@FunctionalInterface
public interface XPredicate<T, X extends Exception> {

    /**
     * Performs this operation on the given argument.
     *
     * @throws X if so.
     * @see java.util.function.Predicate#test(Object)
     */
    boolean test(T t) throws X;
}
