package de.team33.test.exceptional.v3;

import java.io.IOException;
import java.util.UUID;

import de.team33.libs.exceptional.v3.Exposer;
import de.team33.libs.exceptional.v3.WrappedException;
import de.team33.libs.exceptional.v3.Wrapping;
import org.junit.Test;

import static de.team33.libs.exceptional.v3.Exposer.expose;
import static org.junit.Assert.*;


public class WrappingTest {

    private static final Exposer<IOException> EXPECT_IOX = expose(IOException.class);

    private static String tryString(final boolean success) throws IOException {
        if (success) {
            return UUID.randomUUID().toString();
        } else {
            throw new IOException();
        }
    }

    @Test
    public void runnable() {
        try {
            Wrapping.runnable(() -> tryString(false)).run();
            fail("expected to fail but worked");
        } catch (WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public void consumer() {
        try {
            Wrapping.consumer(t -> tryString(false)).accept(null);
            fail("expected to fail but worked");
        } catch (WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public void biConsumer() {
        try {
            Wrapping.biConsumer((t, u) -> tryString(false)).accept(null, null);
            fail("expected to fail but worked");
        } catch (WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public void supplier() {
        try {
            final String result = Wrapping.supplier(() -> tryString(false)).get();
            fail("expected to fail but was " + result);
        } catch (WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public void function() {
        try {
            final String result = Wrapping.function(t -> tryString(false)).apply(null);
            fail("expected to fail but was " + result);
        } catch (WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public void biFunction() {
        try {
            final String result = Wrapping.biFunction((t, u) -> tryString(false)).apply(null, null);
            fail("expected to fail but was " + result);
        } catch (WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }
}
