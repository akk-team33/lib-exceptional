package de.team33.test.exceptional.v3;

import de.team33.libs.exceptional.v3.WrappedException;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;

import static de.team33.libs.exceptional.v3.TriExposer.expose;


public class TriExposerTest {

    @Test(expected = IOException.class)
    public final void runIOException() throws Exception {
        expose(IOException.class, AWTException.class, SQLException.class).run(() -> {
            throw new WrappedException(new IOException());
        });
    }

    @Test(expected = AWTException.class)
    public final void runAWTException() throws Exception {
        expose(IOException.class, AWTException.class, SQLException.class).run(() -> {
            throw new WrappedException(new AWTException(""));
        });
    }

    @Test(expected = SQLException.class)
    public final void runSQLException() throws Exception {
        expose(IOException.class, AWTException.class, SQLException.class).run(() -> {
            throw new WrappedException(new SQLException());
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public final void runRuntimeException() throws Exception {
        expose(IOException.class, AWTException.class, SQLException.class).run(() -> {
            throw new WrappedException(new IllegalArgumentException());
        });
    }

    @Test(expected = OutOfMemoryError.class)
    public final void getError() throws Exception {
        expose(IOException.class, AWTException.class, SQLException.class).get(() -> {
            throw new WrappedException(new OutOfMemoryError());
        });
    }

    @Test(expected = WrappedException.class)
    public final void getUnexpected() throws Exception {
        expose(IOException.class, AWTException.class, SQLException.class).get(() -> {
            throw new WrappedException(new SAXException());
        });
    }
}
