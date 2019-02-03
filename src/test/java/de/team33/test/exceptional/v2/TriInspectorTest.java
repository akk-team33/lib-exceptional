package de.team33.test.exceptional.v2;

import de.team33.libs.exceptional.v2.WrappedException;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;

import static de.team33.libs.exceptional.v2.TriInspector.expect;


public class TriInspectorTest {

    @Test(expected = IOException.class)
    public void runIOException() throws IOException, AWTException, SQLException {
        expect(IOException.class, AWTException.class, SQLException.class).run(() -> {
            throw new WrappedException(new IOException());
        });
    }

    @Test(expected = AWTException.class)
    public void runAWTException() throws IOException, AWTException, SQLException {
        expect(IOException.class, AWTException.class, SQLException.class).run(() -> {
            throw new WrappedException(new AWTException(""));
        });
    }

    @Test(expected = SQLException.class)
    public void runSQLException() throws IOException, AWTException, SQLException {
        expect(IOException.class, AWTException.class, SQLException.class).run(() -> {
            throw new WrappedException(new SQLException());
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void runRuntimeException() throws IOException, AWTException, SQLException {
        expect(IOException.class, AWTException.class, SQLException.class).run(() -> {
            throw new WrappedException(new IllegalArgumentException());
        });
    }

    @Test(expected = OutOfMemoryError.class)
    public void runError() throws IOException, AWTException, SQLException {
        expect(IOException.class, AWTException.class, SQLException.class).run(() -> {
            throw new WrappedException(new OutOfMemoryError());
        });
    }

    @Test(expected = WrappedException.class)
    public void runUnexpected() throws IOException, AWTException, SQLException {
        expect(IOException.class, AWTException.class, SQLException.class).run(() -> {
            throw new WrappedException(new SAXException());
        });
    }
}
