package de.team33.test.exceptional.v4;

import de.team33.libs.exceptional.v4.CheckedEnvelope;
import de.team33.libs.exceptional.v4.CheckedWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class CheckedWrapperTest<X extends Exception> {

    private final Class<X> exceptionType;
    private final CheckedWrapper<X> wrapper;

    public CheckedWrapperTest(final Class<X> exceptionType, final CheckedWrapper<X> wrapper) {
        this.exceptionType = exceptionType;
        this.wrapper = wrapper;
    }

    @Parameters(name = "{index}: {0} + {1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {CheckedEnvelope.class, CheckedWrapper.DEFAULT},
                {IOException.class, new CheckedWrapper<>(IOException.class, IOException::new)},
                {SQLException.class, new CheckedWrapper<>(SQLException.class, SQLException::new)}
        });
    }

    private static String tryString(final boolean success) throws SAXException {
        if (success) {
            return UUID.randomUUID().toString();
        } else {
            throw new SAXException();
        }
    }

    @Test
    public final void runnable() {
        try {
            wrapper.xRunnable(() -> tryString(false)).run();
            fail("expected to fail but worked");
        } catch (final Exception e) {
            assertEquals(exceptionType, e.getClass());
            assertEquals(SAXException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void consumer() {
        try {
            wrapper.xConsumer(t -> tryString(false)).accept(null);
            fail("expected to fail but worked");
        } catch (final Exception e) {
            assertEquals(exceptionType, e.getClass());
            assertEquals(SAXException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void biConsumer() {
        try {
            wrapper.xBiConsumer((t, u) -> tryString(false)).accept(null, null);
            fail("expected to fail but worked");
        } catch (final Exception e) {
            assertEquals(exceptionType, e.getClass());
            assertEquals(SAXException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void supplier() {
        try {
            final String result = wrapper.xSupplier(() -> tryString(false)).get();
            fail("expected to fail but was " + result);
        } catch (final Exception e) {
            assertEquals(exceptionType, e.getClass());
            assertEquals(SAXException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void function() {
        try {
            final String result = wrapper.xFunction(t -> tryString(false)).apply(null);
            fail("expected to fail but was " + result);
        } catch (final Exception e) {
            assertEquals(exceptionType, e.getClass());
            assertEquals(SAXException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void biFunction() {
        try {
            final String result = wrapper.xBiFunction((t, u) -> tryString(false)).apply(null, null);
            fail("expected to fail but was " + result);
        } catch (final Exception e) {
            assertEquals(exceptionType, e.getClass());
            assertEquals(SAXException.class, e.getCause().getClass());
        }
    }
}
