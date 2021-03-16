package de.team33.test.exceptional.v4;

import de.team33.libs.exceptional.v4.ExpectationException;
import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("ThrowableNotThrown")
public class ExpectationExceptionTest {

    @Test
    public final void initByCause() {
        assertEquals("Unexpected: null",
                     new ExpectationException((Throwable) null).getMessage());
        assertEquals("Unexpected: IllegalArgumentException",
                     new ExpectationException(new IllegalArgumentException()).getMessage());
        assertEquals("Unexpected: the message",
                     new ExpectationException(new IllegalArgumentException("the message")).getMessage());
    }

    @Test
    public final void initByMessage() {
        assertNull(new ExpectationException((String) null).getMessage());
        assertEquals("the message",
                     new ExpectationException("the message").getMessage());
    }

    @Test
    public final void initByBoth() {
        assertNull(new ExpectationException(null, null).getMessage());
        assertEquals("the message",
                     new ExpectationException("the message").getMessage());
    }
}