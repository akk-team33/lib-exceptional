package de.team33.libs.exceptional.v3;

import java.util.function.Consumer;
import java.util.function.Supplier;

class CoreDisclosure<R extends RuntimeException, X extends Throwable, Y extends Throwable, Z extends Throwable> {

    private final Class<R> rClass;
    private final Class<X> xClass;
    private final Class<Y> yClass;
    private final Class<Z> zClass;
    private final Consumer<? super R> onFallback;
    private final Inspection<R, X, Y, Z> inspection;

    CoreDisclosure(final Class<R> rClass,
                   final Class<X> xClass, final Class<Y> yClass, final Class<Z> zClass,
                   final Consumer<? super R> onFallback) {
        this.rClass = rClass;
        this.xClass = xClass;
        this.yClass = yClass;
        this.zClass = zClass;
        this.onFallback = onFallback;
        this.inspection = inspectionMethod(xClass, yClass, zClass);
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

    final void run(final Runnable runnable) throws X, Y, Z {
        get(wrap(runnable));
    }

    final <T> T get(final Supplier<T> supplier) throws X, Y, Z {
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

    @FunctionalInterface
    private interface Inspection<
            R extends RuntimeException, X extends Throwable, Y extends Throwable, Z extends Throwable> {
        R insight(R r) throws X, Y, Z;
    }
}
