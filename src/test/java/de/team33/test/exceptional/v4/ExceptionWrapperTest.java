package de.team33.test.exceptional.v4;

import de.team33.libs.exceptional.v4.ExceptionWrapper;
import de.team33.libs.exceptional.v4.WrappedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@RunWith(Parameterized.class)
public class ExceptionWrapperTest {

    private final Class<?> runtimeExceptionType;
    private final ExceptionWrapper wrapper;

    public ExceptionWrapperTest(final Class<?> runtimeExceptionType, final ExceptionWrapper wrapper) {
        this.runtimeExceptionType = runtimeExceptionType;
        this.wrapper = wrapper;
    }

    @Parameters(name = "{index}: {0} + {1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {WrappedException.class, ExceptionWrapper.using(WrappedException::new)},
                {IllegalStateException.class, ExceptionWrapper.using(IllegalStateException::new)},
                {RuntimeException.class, ExceptionWrapper.using(RuntimeException::new)}
        });
    }

    private static <X extends Exception> String rise(final Function<String, X> newException, Object... args) throws X {
        throw newException.apply("args: " + Arrays.asList(args));
    }

    @Test
    public final void runnable() {
        try {
            wrapper.runnable(() -> rise(IOException::new))
                   .run();
            fail("expected to fail but worked");
        } catch (final RuntimeException e) {
            assertEquals(runtimeExceptionType, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
            assertEquals("args: []", e.getCause().getMessage());
        }
    }

    @Test
    public final void consumer() {
        try {
            wrapper.consumer(t -> rise(IOException::new, t))
                   .accept(278);
            fail("expected to fail but worked");
        } catch (final RuntimeException e) {
            assertEquals(runtimeExceptionType, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
            assertEquals("args: [278]", e.getCause().getMessage());
        }
    }

    @Test
    public final void biConsumer() {
        try {
            wrapper.biConsumer((t, u) -> rise(IOException::new, t, u))
                   .accept(3.141592654, "a string");
            fail("expected to fail but worked");
        } catch (final RuntimeException e) {
            assertEquals(runtimeExceptionType, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
            assertEquals("args: [3.141592654, a string]", e.getCause().getMessage());
        }
    }

    @Test
    public final void supplier() {
        try {
            final String result = wrapper.supplier(() -> rise(IOException::new))
                                         .get();
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertEquals(runtimeExceptionType, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
            assertEquals("args: []", e.getCause().getMessage());
        }
    }

    @Test
    public final void predicate() {
        try {
            final boolean result = wrapper.predicate(t -> null == rise(IOException::new, t))
                                          .test(null);
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertEquals(runtimeExceptionType, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
            assertEquals("args: [null]", e.getCause().getMessage());
        }
    }

    @Test
    public final void biPredicate() {
        try {
            final boolean result = wrapper.biPredicate((t, u) -> null == rise(IOException::new, t, u))
                                          .test(0, 'x');
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertEquals(runtimeExceptionType, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
            assertEquals("args: [0, x]", e.getCause().getMessage());
        }
    }

    @Test
    public final void function() {
        try {
            final String result = wrapper.function(t -> rise(IOException::new, t))
                                         .apply("another string");
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertEquals(runtimeExceptionType, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
            assertEquals("args: [another string]", e.getCause().getMessage());
        }
    }

    @Test
    public final void biFunction() {
        try {
            final String result = wrapper.biFunction((t, u) -> rise(IOException::new, t, u))
                                         .apply('a', 'b');
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertEquals(runtimeExceptionType, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
            assertEquals("args: [a, b]", e.getCause().getMessage());
        }
    }
}
