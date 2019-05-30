package de.team33.test.exceptional.v3;

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
        try {
            Wrapping.runnable(() -> tryString(false)).run();
            fail("expected to fail but worked");
        } catch (final WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void consumer() {
        try {
            Wrapping.consumer(t -> tryString(false)).accept(null);
            fail("expected to fail but worked");
        } catch (final WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void biConsumer() {
        try {
            Wrapping.biConsumer((t, u) -> tryString(false)).accept(null, null);
            fail("expected to fail but worked");
        } catch (final WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void supplier() {
        try {
            final String result = Wrapping.supplier(() -> tryString(false)).get();
            fail("expected to fail but was " + result);
        } catch (final WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void function() {
        try {
            final String result = Wrapping.function(t -> tryString(false)).apply(null);
            fail("expected to fail but was " + result);
        } catch (final WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void biFunction() {
        try {
            final String result = Wrapping.biFunction((t, u) -> tryString(false)).apply(null, null);
            fail("expected to fail but was " + result);
        } catch (final WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }
}
