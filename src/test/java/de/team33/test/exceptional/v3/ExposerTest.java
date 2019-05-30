package de.team33.test.exceptional.v3;

import de.team33.libs.exceptional.v3.WrappedException;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static de.team33.libs.exceptional.v3.Exposer.expose;


public class ExposerTest {

    @Test(expected = IOException.class)
    public final void runException() throws IOException {
        expose(IOException.class).run(() -> {
            throw new WrappedException(new IOException());
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public final void runRuntimeException() throws IOException {
        expose(IOException.class).run(() -> {
            throw new WrappedException(new IllegalArgumentException());
        });
    }

    @Test(expected = OutOfMemoryError.class)
    public final void runError() throws IOException {
        expose(IOException.class).run(() -> {
            throw new WrappedException(new OutOfMemoryError());
        });
    }

    @Test(expected = WrappedException.class)
    public final void runUnexpected() throws IOException {
        expose(IOException.class).run(() -> {
            throw new WrappedException(new SAXException());
        });
    }
}
