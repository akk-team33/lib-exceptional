package de.team33.test.exceptional.v3;

import java.io.IOException;
import java.util.UUID;

import de.team33.libs.exceptional.v3.Inspector;
import de.team33.libs.exceptional.v3.WrappedException;
import de.team33.libs.exceptional.v3.Wrapper;
import org.junit.Test;

import static de.team33.libs.exceptional.v3.Inspector.expect;
import static org.junit.Assert.*;


public class WrapperTest {

    private static final Inspector<IOException> EXPECT_IOX = expect(IOException.class);

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
            Wrapper.runnable(() -> tryString(false)).run();
            fail("expected to fail but worked");
        } catch (WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public void consumer() {
        try {
            Wrapper.consumer(t -> tryString(false)).accept(null);
            fail("expected to fail but worked");
        } catch (WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public void biConsumer() {
        try {
            Wrapper.biConsumer((t, u) -> tryString(false)).accept(null, null);
            fail("expected to fail but worked");
        } catch (WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public void supplier() {
        try {
            final String result = Wrapper.supplier(() -> tryString(false)).get();
            fail("expected to fail but was " + result);
        } catch (WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public void function() {
        try {
            final String result = Wrapper.function(t -> tryString(false)).apply(null);
            fail("expected to fail but was " + result);
        } catch (WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public void biFunction() {
        try {
            final String result = Wrapper.biFunction((t, u) -> tryString(false)).apply(null, null);
            fail("expected to fail but was " + result);
        } catch (WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }
}
