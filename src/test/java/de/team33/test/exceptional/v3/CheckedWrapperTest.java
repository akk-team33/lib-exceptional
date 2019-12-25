package de.team33.test.exceptional.v3;

import de.team33.libs.exceptional.v3.CheckedEnvelope;
import de.team33.libs.exceptional.v3.CheckedWrapper;
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
                {CheckedEnvelope.class, new CheckedWrapper<>(CheckedEnvelope.class, CheckedEnvelope::new)},
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
    public final void xRunnable() {
        try {
            wrapper.xRunnable(() -> tryString(false)).run();
            fail("expected to fail but worked");
        } catch (final Exception e) {
            assertEquals(exceptionType, e.getClass());
            assertEquals(SAXException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void xConsumer() {
        try {
            wrapper.xConsumer(t -> tryString(false)).accept(null);
            fail("expected to fail but worked");
        } catch (final Exception e) {
            assertEquals(exceptionType, e.getClass());
            assertEquals(SAXException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void xBiConsumer() {
        try {
            wrapper.xBiConsumer((t, u) -> tryString(false)).accept(null, null);
            fail("expected to fail but worked");
        } catch (final Exception e) {
            assertEquals(exceptionType, e.getClass());
            assertEquals(SAXException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void xSupplier() {
        try {
            final String result = wrapper.xSupplier(() -> tryString(false)).get();
            fail("expected to fail but was " + result);
        } catch (final Exception e) {
            assertEquals(exceptionType, e.getClass());
            assertEquals(SAXException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void xPredicate() {
        try {
            final boolean result = wrapper.xPredicate(t -> null == tryString(false)).test(null);
            fail("expected to fail but was " + result);
        } catch (final Exception e) {
            assertEquals(exceptionType, e.getClass());
            assertEquals(SAXException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void xBiPredicate() {
        try {
            final boolean result = wrapper.xBiPredicate((t, u) -> null == tryString(false)).test(null, null);
            fail("expected to fail but was " + result);
        } catch (final Exception e) {
            assertEquals(exceptionType, e.getClass());
            assertEquals(SAXException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void xFunction() {
        try {
            final String result = wrapper.xFunction(t -> tryString(false)).apply(null);
            fail("expected to fail but was " + result);
        } catch (final Exception e) {
            assertEquals(exceptionType, e.getClass());
            assertEquals(SAXException.class, e.getCause().getClass());
        }
    }

    @Test
    public final void xBiFunction() {
        try {
            final String result = wrapper.xBiFunction((t, u) -> tryString(false)).apply(null, null);
            fail("expected to fail but was " + result);
        } catch (final Exception e) {
            assertEquals(exceptionType, e.getClass());
            assertEquals(SAXException.class, e.getCause().getClass());
        }
    }
}
