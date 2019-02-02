package de.team33.test.exceptional.v1;

import java.io.IOException;
import java.util.UUID;

import de.team33.libs.exceptional.v1.ExpectationException;
import de.team33.libs.exceptional.v1.WrappedException;
import org.junit.Test;
import org.xml.sax.SAXException;

import static de.team33.libs.exceptional.v1.Ejector.eject;
import static org.junit.Assert.assertNotNull;


public class EjectorTest {

    @Test(expected = IOException.class)
    public void runException() throws IOException {
        eject(IOException.class).run(this::throwWrappedIOException);
    }

    @Test(expected = ExpectationException.class)
    public void runUnexpected() throws SAXException {
        eject(SAXException.class).run(this::throwWrappedIOException);
    }

    @Test(expected = IOException.class)
    public void getException() throws IOException {
        eject(IOException.class).get(this::throwWrappedIOException);
    }

    @Test
    public void getString() throws IOException {
        assertNotNull(eject(IOException.class).get(this::getAString));
    }

    private String getAString() {
        return throwWrappedIOException(false);
    }

    private String throwWrappedIOException() {
        return throwWrappedIOException(true);
    }

    private String throwWrappedIOException(final boolean doIt) {
        try {
            return throwIOException(doIt);
        } catch (IOException e) {
            throw new WrappedException(e);
        }
    }

    private String throwIOException(final boolean doIt) throws IOException {
        if (doIt) {
            throw new IOException();
        } else {
            return UUID.randomUUID().toString();
        }
    }
}
