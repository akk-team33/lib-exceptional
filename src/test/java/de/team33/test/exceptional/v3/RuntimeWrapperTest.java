package de.team33.test.exceptional.v3;

import de.team33.libs.exceptional.v3.RuntimeEnvelope;
import de.team33.libs.exceptional.v3.RuntimeWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class RuntimeWrapperTest<X extends RuntimeException> {

    private final Class<X> runtimeExceptionType;
    private final RuntimeWrapper<X> wrapper;

    public RuntimeWrapperTest(final Class<X> runtimeExceptionType, final RuntimeWrapper<X> wrapper) {
        this.runtimeExceptionType = runtimeExceptionType;
        this.wrapper = wrapper;
    }

    @Parameters(name = "{index}: {0} + {1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {RuntimeEnvelope.class, new RuntimeWrapper<>(RuntimeEnvelope::new)},
                {IllegalStateException.class, new RuntimeWrapper<>(IllegalStateException::new)},
                {RuntimeException.class, new RuntimeWrapper<>(RuntimeException::new)}
        });
    }

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
            wrapper.runnable(() -> tryString(false)).run();
            fail("expected to fail but worked");
        } catch (final RuntimeException e) {
            assertEquals(runtimeExceptionType, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void consumer() {
        try {
            wrapper.consumer(t -> tryString(false)).accept(null);
            fail("expected to fail but worked");
        } catch (final RuntimeException e) {
            assertEquals(runtimeExceptionType, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void biConsumer() {
        try {
            wrapper.biConsumer((t, u) -> tryString(false)).accept(null, null);
            fail("expected to fail but worked");
        } catch (final RuntimeException e) {
            assertEquals(runtimeExceptionType, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void supplier() {
        try {
            final String result = wrapper.supplier(() -> tryString(false)).get();
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertEquals(runtimeExceptionType, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void function() {
        try {
            final String result = wrapper.function(t -> tryString(false)).apply(null);
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertEquals(runtimeExceptionType, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void biFunction() {
        try {
            final String result = wrapper.biFunction((t, u) -> tryString(false)).apply(null, null);
            fail("expected to fail but was " + result);
        } catch (final RuntimeException e) {
            assertEquals(runtimeExceptionType, e.getClass());
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }
}
