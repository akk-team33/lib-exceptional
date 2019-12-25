package de.team33.test.exceptional.v3;

import de.team33.libs.exceptional.v3.CheckedEnvelope;
import de.team33.libs.exceptional.v3.WrappedException;
import de.team33.libs.exceptional.v3.Wrapping;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class WrappingTest {

    private static String tryString(final boolean success) throws IOException {
        if (success) {
            return UUID.randomUUID().toString();
        } else {
            throw new IOException();
        }
    }

    @Test
    public final void runnable() {
        // Formally expects a RuntimeEnvelope but for compatibly reasons preliminary expects a WrappedException
        // noinspection deprecation
        try {
            Wrapping.runnable(() -> tryString(false)).run();
            fail("expected to fail but worked");
        } catch (final WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void consumer() {
        // Formally expects a RuntimeEnvelope but for compatibly reasons preliminary expects a WrappedException
        // noinspection deprecation
        try {
            Wrapping.consumer(t -> tryString(false)).accept(null);
            fail("expected to fail but worked");
        } catch (final WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void biConsumer() {
        // Formally expects a RuntimeEnvelope but for compatibly reasons preliminary expects a WrappedException
        // noinspection deprecation
        try {
            Wrapping.biConsumer((t, u) -> tryString(false)).accept(null, null);
            fail("expected to fail but worked");
        } catch (final WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void supplier() {
        // Formally expects a RuntimeEnvelope but for compatibly reasons preliminary expects a WrappedException
        // noinspection deprecation
        try {
            final String result = Wrapping.supplier(() -> tryString(false)).get();
            fail("expected to fail but was " + result);
        } catch (final WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void predicate() {
        // Formally expects a RuntimeEnvelope but for compatibly reasons preliminary expects a WrappedException
        // noinspection deprecation
        try {
            final boolean result = Wrapping.predicate(t -> null == tryString(false)).test(null);
            fail("expected to fail but was " + result);
        } catch (final WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void biPredicate() {
        // Formally expects a RuntimeEnvelope but for compatibly reasons preliminary expects a WrappedException
        // noinspection deprecation
        try {
            final boolean result = Wrapping.biPredicate((t, u) -> null == tryString(false)).test(null, null);
            fail("expected to fail but was " + result);
        } catch (final WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void function() {
        // Formally expects a RuntimeEnvelope but for compatibly reasons preliminary expects a WrappedException
        // noinspection deprecation
        try {
            final String result = Wrapping.function(t -> tryString(false)).apply(null);
            fail("expected to fail but was " + result);
        } catch (final WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void biFunction() {
        // Formally expects a RuntimeEnvelope but for compatibly reasons preliminary expects a WrappedException
        // noinspection deprecation
        try {
            final String result = Wrapping.biFunction((t, u) -> tryString(false)).apply(null, null);
            fail("expected to fail but was " + result);
        } catch (final WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void xRunnable() {
        try {
            Wrapping.xRunnable(() -> tryString(false)).run();
            fail("expected to fail but worked");
        } catch (final CheckedEnvelope e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void xConsumer() {
        try {
            Wrapping.xConsumer(t -> tryString(false)).accept(null);
            fail("expected to fail but worked");
        } catch (final CheckedEnvelope e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void xBiConsumer() {
        try {
            Wrapping.xBiConsumer((t, u) -> tryString(false)).accept(null, null);
            fail("expected to fail but worked");
        } catch (final CheckedEnvelope e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void xSupplier() {
        try {
            final String result = Wrapping.xSupplier(() -> tryString(false)).get();
            fail("expected to fail but was " + result);
        } catch (final CheckedEnvelope e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void xPredicate() {
        // Formally expects a RuntimeEnvelope but for compatibly reasons preliminary expects a WrappedException
        // noinspection deprecation
        try {
            final boolean result = Wrapping.xPredicate(t -> null == tryString(false)).test(null);
            fail("expected to fail but was " + result);
        } catch (final CheckedEnvelope e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void xBiPredicate() {
        // Formally expects a RuntimeEnvelope but for compatibly reasons preliminary expects a WrappedException
        // noinspection deprecation
        try {
            final boolean result = Wrapping.xBiPredicate((t, u) -> null == tryString(false)).test(null, null);
            fail("expected to fail but was " + result);
        } catch (final CheckedEnvelope e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void xFunction() {
        try {
            final String result = Wrapping.xFunction(t -> tryString(false)).apply(null);
            fail("expected to fail but was " + result);
        } catch (final CheckedEnvelope e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void xBiFunction() {
        try {
            final String result = Wrapping.xBiFunction((t, u) -> tryString(false)).apply(null, null);
            fail("expected to fail but was " + result);
        } catch (final CheckedEnvelope e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }
}
