package de.team33.test.exceptional.v3;

import de.team33.libs.exceptional.v3.WrappedException;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.awt.AWTException;
import java.io.IOException;

import static de.team33.libs.exceptional.v3.BiExposer.expose;

@Deprecated
public class BiExposerTest {

    @Test(expected = IOException.class)
    public final void runIOException() throws Exception {
        expose(IOException.class, AWTException.class).run(() -> {
            throw new WrappedException(new IOException());
        });
    }

    @Test(expected = AWTException.class)
    public final void runAWTException() throws Exception {
        expose(IOException.class, AWTException.class).run(() -> {
            throw new WrappedException(new AWTException(""));
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public final void runRuntimeException() throws Exception {
        expose(IOException.class, AWTException.class).run(() -> {
            throw new WrappedException(new IllegalArgumentException());
        });
    }

    @Test(expected = OutOfMemoryError.class)
    public final void runError() throws Exception {
        expose(IOException.class, AWTException.class).run(() -> {
            throw new WrappedException(new OutOfMemoryError());
        });
    }

    @Test(expected = WrappedException.class)
    public final void runUnexpected() throws Exception {
        expose(IOException.class, AWTException.class).run(() -> {
            throw new WrappedException(new SAXException());
        });
    }
}
