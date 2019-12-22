package de.team33.libs.exceptional.v3;

import java.util.function.Consumer;

public class Disclosing_<R extends RuntimeException> {

    private static final Consumer<Object> IGNORE_FALLBACK = fallback -> {
    };

    private final Class<R> rClass;

    private Disclosing_(final Class<R> rClass) {
        this.rClass = rClass;
    }

    public static <R extends RuntimeException>
    Disclosing_<R> on(final Class<R> rClass) {
        return new Disclosing_<>(rClass);
    }

    public final <X extends Throwable>
    Disclosing<R, X> disclose(final Class<X> xClass) {
        return disclose(xClass, IGNORE_FALLBACK);
    }

    public final <X extends Throwable>
    Disclosing<R, X> disclose(final Class<X> xClass, final Consumer<? super R> onFallback) {
        return new Disclosing<>(rClass, xClass, onFallback);
    }

    public final <X extends Throwable, Y extends Throwable>
    BiDisclosing<R, X, Y> disclose(final Class<X> xClass, final Class<Y> yClass) {
        return disclose(xClass, yClass, IGNORE_FALLBACK);
    }

    public final <X extends Throwable, Y extends Throwable>
    BiDisclosing<R, X, Y> disclose(final Class<X> xClass, final Class<Y> yClass, final Consumer<? super R> onFallback) {
        return new BiDisclosing<>(rClass, xClass, yClass, onFallback);
    }

    public final <X extends Throwable, Y extends Throwable, Z extends Throwable>
    TriDisclosing<R, X, Y, Z> disclose(final Class<X> xClass, final Class<Y> yClass, final Class<Z> zClass) {
        return disclose(xClass, yClass, zClass, IGNORE_FALLBACK);
    }

    public final <X extends Throwable, Y extends Throwable, Z extends Throwable>
    TriDisclosing<R, X, Y, Z> disclose(final Class<X> xClass, final Class<Y> yClass, final Class<Z> zClass, final Consumer<? super R> onFallback) {
        return new TriDisclosing<>(rClass, xClass, yClass, zClass, onFallback);
    }
}
