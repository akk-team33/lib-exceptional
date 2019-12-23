package de.team33.libs.exceptional.v3;

import java.util.function.Consumer;

/**
 * A tool to create Disclosures ({@link Disclosure}, {@link BiDisclosure}, {@link TriDisclosure}).
 *
 * @see #on(Class)
 */
public class Disclosing<R extends RuntimeException> {

    private static final Consumer<Object> IGNORE_FALLBACK = fallback -> {
    };

    private final Class<R> rClass;

    private Disclosing(final Class<R> rClass) {
        this.rClass = rClass;
    }

    /**
     * Creates a new {@link Disclosing} instance based on a specific type of {@link RuntimeException}s, which is
     * considered as an enclosing class and whose {@linkplain Throwable#getCause() cause} is to be disclosed.
     */
    public static <R extends RuntimeException> Disclosing<R> on(final Class<R> rClass) {
        return new Disclosing<>(rClass);
    }

    /**
     * Creates a new {@link Disclosure} instance that looks at the associated type of {@link RuntimeException}s and
     * discloses its {@linkplain Throwable#getCause() cause} if it is an exception of the given type.
     */
    public final <X extends Throwable>
    Disclosure<R, X> disclose(final Class<X> xClass) {
        return disclose(xClass, IGNORE_FALLBACK);
    }

    /**
     * <p>Creates a new {@link Disclosure} instance that looks at the associated type of {@link RuntimeException}s and
     * discloses its {@linkplain Throwable#getCause() cause} if it is an exception of the given type.</p>
     *
     * <p>If an exception of the associated type occurs, the {@linkplain Throwable#getCause() cause} of which is not
     * of the given type or does not exist at all, it is handed over to a given {@link Consumer} for further
     * handling before it is re-thrown.</p>
     */
    public final <X extends Throwable>
    Disclosure<R, X> disclose(final Class<X> xClass, final Consumer<? super R> onFallback) {
        return new Disclosure<>(rClass, xClass, onFallback);
    }

    /**
     * Creates a new {@link BiDisclosure} instance that looks at the associated type of {@link RuntimeException}s and
     * discloses its {@linkplain Throwable#getCause() cause} if it is an exception of one of the given types.
     */
    public final <X extends Throwable, Y extends Throwable>
    BiDisclosure<R, X, Y> disclose(final Class<X> xClass, final Class<Y> yClass) {
        return disclose(xClass, yClass, IGNORE_FALLBACK);
    }

    /**
     * <p>Creates a new {@link BiDisclosure} instance that looks at the associated type of {@link RuntimeException}s
     * and discloses its {@linkplain Throwable#getCause() cause} if it is an exception of one of the given types.</p>
     *
     * <p>If an exception of the associated type occurs, the {@linkplain Throwable#getCause() cause} of which is not
     * of one of the given types or does not exist at all, it is handed over to a given {@link Consumer} for further
     * handling before it is re-thrown.</p>
     */
    public final <X extends Throwable, Y extends Throwable>
    BiDisclosure<R, X, Y> disclose(final Class<X> xClass, final Class<Y> yClass, final Consumer<? super R> onFallback) {
        return new BiDisclosure<>(rClass, xClass, yClass, onFallback);
    }

    /**
     * Creates a new {@link TriDisclosure} instance that looks at the associated type of {@link RuntimeException}s and
     * discloses its {@linkplain Throwable#getCause() cause} if it is an exception of one of the given types.
     */
    public final <X extends Throwable, Y extends Throwable, Z extends Throwable>
    TriDisclosure<R, X, Y, Z> disclose(final Class<X> xClass, final Class<Y> yClass, final Class<Z> zClass) {
        return disclose(xClass, yClass, zClass, IGNORE_FALLBACK);
    }

    /**
     * <p>Creates a new {@link TriDisclosure} instance that looks at the associated type of {@link RuntimeException}s
     * and discloses its {@linkplain Throwable#getCause() cause} if it is an exception of one of the given types.</p>
     *
     * <p>If an exception of the associated type occurs, the {@linkplain Throwable#getCause() cause} of which is not
     * of one of the given types or does not exist at all, it is handed over to a given {@link Consumer} for further
     * handling before it is re-thrown.</p>
     */
    public final <X extends Throwable, Y extends Throwable, Z extends Throwable>
    TriDisclosure<R, X, Y, Z> disclose(final Class<X> xClass, final Class<Y> yClass, final Class<Z> zClass, final Consumer<? super R> onFallback) {
        return new TriDisclosure<>(rClass, xClass, yClass, zClass, onFallback);
    }
}
