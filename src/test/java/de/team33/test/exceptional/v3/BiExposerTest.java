package de.team33.test.exceptional.v3;

import de.team33.libs.exceptional.v3.WrappedException;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.awt.AWTException;
import java.io.IOException;

import static de.team33.libs.exceptional.v3.BiExposer.expose;


public class BiExposerTest {

    @Test(expected = IOException.class)
    public void runIOException() throws IOException, AWTException {
        expose(IOException.class, AWTException.class).run(() -> {
            throw new WrappedException(new IOException());
        });
    }

    @Test(expected = AWTException.class)
    public void runAWTException() throws IOException, AWTException {
        expose(IOException.class, AWTException.class).run(() -> {
            throw new WrappedException(new AWTException(""));
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void runRuntimeException() throws IOException, AWTException {
        expose(IOException.class, AWTException.class).run(() -> {
            throw new WrappedException(new IllegalArgumentException());
        });
    }

    @Test(expected = OutOfMemoryError.class)
    public void runError() throws IOException, AWTException {
        expose(IOException.class, AWTException.class).run(() -> {
            throw new WrappedException(new OutOfMemoryError());
        });
    }

    @Test(expected = WrappedException.class)
    public void runUnexpected() throws IOException, AWTException {
        expose(IOException.class, AWTException.class).run(() -> {
            throw new WrappedException(new SAXException());
        });
    }
}
