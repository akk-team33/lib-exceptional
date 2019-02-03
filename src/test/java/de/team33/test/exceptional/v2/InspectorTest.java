package de.team33.test.exceptional.v2;

import de.team33.libs.exceptional.v2.WrappedException;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static de.team33.libs.exceptional.v2.Inspector.expect;


public class InspectorTest {

    @Test(expected = IOException.class)
    public void runException() throws IOException {
        expect(IOException.class).run(() -> {
            throw new WrappedException(new IOException());
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void runRuntimeException() throws IOException {
        expect(IOException.class).run(() -> {
            throw new WrappedException(new IllegalArgumentException());
        });
    }

    @Test(expected = OutOfMemoryError.class)
    public void runError() throws IOException {
        expect(IOException.class).run(() -> {
            throw new WrappedException(new OutOfMemoryError());
        });
    }

    @Test(expected = WrappedException.class)
    public void runUnexpected() throws IOException {
        expect(IOException.class).run(() -> {
            throw new WrappedException(new SAXException());
        });
    }
}
