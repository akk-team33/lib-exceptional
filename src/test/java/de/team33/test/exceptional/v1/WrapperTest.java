package de.team33.test.exceptional.v1;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.team33.libs.exceptional.v1.Ejector;
import de.team33.libs.exceptional.v1.WrappedException;
import de.team33.libs.exceptional.v1.Wrapper;
import org.junit.Test;

import static de.team33.libs.exceptional.v1.Ejector.eject;
import static org.junit.Assert.*;


public class WrapperTest {

    private static final Ejector<IOException> EXPECT_IOX = eject(IOException.class);

    private static String tryString(final boolean success) throws IOException {
        if (success) {
            return UUID.randomUUID().toString();
        } else {
            throw new IOException();
        }
    }

    @Test(expected = IOException.class)
    public void runConsumerFail() throws IOException {
        EXPECT_IOX.run(() -> Stream.of("abc", "def", "ghi").forEach(Wrapper.consumer(str -> {
            throw new IOException();
        })));
    }

    @Test(expected = IOException.class)
    public void getFunctionFail() throws IOException {
        EXPECT_IOX.get(() -> Stream.of("abc", "def", "ghi").map(Wrapper.function(str -> {
            throw new IOException();
        })).collect(Collectors.toList()));
    }

    @Test
    public void runnableFail() {
        try {
            Wrapper.runnable(() -> tryString(false)).run();
            fail("expected to fail but worked");
        } catch (WrappedException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public void runnableSuccess() {
        Wrapper.runnable(() -> tryString(true)).run();
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
}
