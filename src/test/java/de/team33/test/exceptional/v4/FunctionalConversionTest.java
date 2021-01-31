package de.team33.test.exceptional.v4;

import de.team33.libs.exceptional.v4.FunctionalConversion;
import de.team33.libs.exceptional.v4.WrappedException;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class FunctionalConversionTest {

    private static <X extends Exception> String rise(final Function<String, X> newException, Object... args) throws X {
        throw newException.apply("args: " + Arrays.asList(args));
    }

    @Test
    public final void runnable() {
        try {
            FunctionalConversion.runnable(() -> rise(IOException::new))
                                .run();
            fail("expected to fail but worked");
        } catch (final RuntimeException e) {
            assertEquals(WrappedException.class, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
            assertEquals("args: []", e.getCause().getMessage());
        }
    }

    @Test
    public final void consumer() {
        try {
            FunctionalConversion.consumer(t -> rise(IOException::new, t))
                                .accept(278);
            fail("expected to fail but worked");
        } catch (final RuntimeException e) {
            assertEquals(WrappedException.class, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
            assertEquals("args: [278]", e.getCause().getMessage());
        }
    }

    @Test
    public final void biConsumer() {
        try {
            FunctionalConversion.biConsumer((t, u) -> rise(IOException::new, t, u))
                                .accept(3.141592654, "a string");
            fail("expected to fail but worked");
        } catch (final RuntimeException e) {
            assertEquals(WrappedException.class, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
            assertEquals("args: [3.141592654, a string]", e.getCause().getMessage());
        }
    }

    @Test
    public final void supplier() {
        try {
            final String result = FunctionalConversion.supplier(() -> rise(IOException::new))
                                                      .get();
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertEquals(WrappedException.class, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
            assertEquals("args: []", e.getCause().getMessage());
        }
    }

    @Test
    public final void predicate() {
        try {
            final boolean result = FunctionalConversion.predicate(t -> null == rise(IOException::new, t))
                                                       .test(null);
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertEquals(WrappedException.class, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
            assertEquals("args: [null]", e.getCause().getMessage());
        }
    }

    @Test
    public final void biPredicate() {
        try {
            final boolean result = FunctionalConversion.biPredicate((t, u) -> null == rise(IOException::new, t, u))
                                                       .test(0, 'x');
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertEquals(WrappedException.class, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
            assertEquals("args: [0, x]", e.getCause().getMessage());
        }
    }

    @Test
    public final void function() {
        try {
            final String result = FunctionalConversion.function(t -> rise(IOException::new, t))
                                                      .apply("another string");
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertEquals(WrappedException.class, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
            assertEquals("args: [another string]", e.getCause().getMessage());
        }
    }

    @Test
    public final void biFunction() {
        try {
            final String result = FunctionalConversion.biFunction((t, u) -> rise(IOException::new, t, u))
                                                      .apply('a', 'b');
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertEquals(WrappedException.class, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
            assertEquals("args: [a, b]", e.getCause().getMessage());
        }
    }
}
