package de.team33.libs.exceptional.v3;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>A tool to disclose three specific types of (typically checked) exceptions wrapped by
 * ({@linkplain Throwable#getCause() cause} of) a specific type of {@link RuntimeException}s.</p>
 *
 * @param <R> The specific type of enclosing {@link RuntimeException}s to be looked at.
 * @param <X> The first specific type of exceptions to be disclosed.
 * @param <Y> The second specific type of exceptions to be disclosed.
 * @param <Z> The third specific type of exceptions to be disclosed.
 * @see Disclosing#on(Class)
 * @see Disclosing#disclose(Class, Class, Class)
 * @see Disclosing#disclose(Class, Class, Class, Consumer)
 */
public final class TriDisclosure<
        R extends RuntimeException,
        X extends Throwable,
        Y extends Throwable,
        Z extends Throwable> {

    private final Class<R> rClass;
    private final Class<X> xClass;
    private final Class<Y> yClass;
    private final Class<Z> zClass;
    private final Consumer<? super R> onFallback;
    private final Inspection<R, X, Y, Z> inspection;

    TriDisclosure(final Class<R> r, final Class<X> x, final Class<Y> y, final Class<Z> z,
                  final Consumer<? super R> onFallback) {
        this.rClass = r;
        this.xClass = x;
        this.yClass = y;
        this.zClass = z;
        this.onFallback = onFallback;
        this.inspection = inspectionMethod(x, y, z);
    }

    private Inspection<R, X, Y, Z> inspectionMethod(final Class<X> x, final Class<Y> y, final Class<Z> z) {
        return (y == z) ? ((x == y) ? this::xInsight : this::xyInsight) : this::xyzInsight;
    }

    private static Supplier<Void> wrap(final Runnable runnable) {
        return () -> {
            runnable.run();
            return null;
        };
    }

    /**
     * Immediately executes a {@link Runnable}.
     *
     * @throws X if the {@link Runnable} causes a {@link RuntimeException} of type {@code <R>},
     *           which in turn is caused by an exception of type {@code <X>}.
     * @throws Y if the {@link Runnable} causes a {@link RuntimeException} of type {@code <R>},
     *           which in turn is caused by an exception of type {@code <Y>}.
     * @throws Z if the {@link Runnable} causes a {@link RuntimeException} of type {@code <R>},
     *           which in turn is caused by an exception of type {@code <Z>}.
     * @throws R if the {@link Runnable} causes a {@link RuntimeException} of type {@code <R>},
     *           which is NOT caused by an exception of type {@code <X>}, {@code <Y>} or {@code <Z>}.
     */
    public final void run(final Runnable runnable) throws X, Y, Z {
        get(wrap(runnable));
    }

    /**
     * Immediately executes a {@link Supplier} and returns its result.
     *
     * @throws X if the {@link Supplier} causes a {@link RuntimeException} of type {@code <R>},
     *           which in turn is caused by an exception of type {@code <X>}.
     * @throws Y if the {@link Supplier} causes a {@link RuntimeException} of type {@code <R>},
     *           which in turn is caused by an exception of type {@code <Y>}.
     * @throws Z if the {@link Runnable} causes a {@link RuntimeException} of type {@code <R>},
     *           which in turn is caused by an exception of type {@code <Z>}.
     * @throws R if the {@link Runnable} causes a {@link RuntimeException} of type {@code <R>},
     *           which is NOT caused by an exception of type {@code <X>}, {@code <Y>} or {@code <Z>}.
     */
    public final <T> T get(final Supplier<T> supplier) throws X, Y, Z {
        try {
            return supplier.get();
        } catch (final RuntimeException caught) {
            throw rClass.isInstance(caught) ? handle(inspection.insight(rClass.cast(caught))) : caught;
        }
    }

    private R xInsight(final R caught) throws X {
        return Insight.into(caught)
                      .reThrowCauseIf(xClass)
                      .fallback();
    }

    private R xyInsight(final R caught) throws X, Y {
        return Insight.into(caught)
                      .reThrowCauseIf(xClass)
                      .reThrowCauseIf(yClass)
                      .fallback();
    }

    private R xyzInsight(final R caught) throws X, Y, Z {
        return Insight.into(caught)
                      .reThrowCauseIf(xClass)
                      .reThrowCauseIf(yClass)
                      .reThrowCauseIf(zClass)
                      .fallback();
    }

    private R handle(final R fallback) {
        onFallback.accept(fallback);
        return fallback;
    }

    private interface Inspection<
            R extends RuntimeException, X extends Throwable, Y extends Throwable, Z extends Throwable> {
        R insight(R r) throws X, Y, Z;
    }
}
