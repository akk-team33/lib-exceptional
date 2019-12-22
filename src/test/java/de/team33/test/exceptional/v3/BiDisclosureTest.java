package de.team33.test.exceptional.v3;

import de.team33.libs.exceptional.v3.BiDisclosure;
import de.team33.libs.exceptional.v3.Disclosing;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BiDisclosureTest {

    private static final Logger LOGGER = Logger.getLogger(BiDisclosureTest.class.getCanonicalName());

    private final BiDisclosure<Envelope, IOException, SQLException> disclosing = Disclosing
            .on(Envelope.class)
            .disclose(
                    IOException.class, SQLException.class,
                    fallback -> LOGGER.log(Level.WARNING, "Undisclosed", fallback));

    private static <T extends Throwable> void fire(final Supplier<T> supplier) throws T {
        throw supplier.get();
    }

    @Test(expected = IOException.class)
    public final void run_Envelope_IOException() throws IOException, SQLException {
        disclosing.run(() -> fire(() -> new Envelope(new IOException())));
    }

    @Test(expected = SQLException.class)
    public final void run_Envelope_SQLException() throws IOException, SQLException {
        disclosing.run(() -> fire(() -> new Envelope(new SQLException())));
    }

    @Test(expected = Envelope.class)
    public final void run_Envelope_IllegalArgumentException() throws IOException, SQLException {
        disclosing.run(() -> fire(() -> new Envelope(new IllegalArgumentException())));
    }

    @Test(expected = Envelope.class)
    public final void run_Envelope_OutOfMemoryError() throws IOException, SQLException {
        disclosing.run(() -> fire(() -> new Envelope(new OutOfMemoryError())));
    }

    @Test(expected = Envelope.class)
    public final void run_Envelope_SAXException() throws IOException, SQLException {
        disclosing.run(() -> fire(() -> new Envelope(new SAXException())));
    }

    @Test(expected = Envelope.class)
    public final void run_Envelope_noCause() throws IOException, SQLException {
        disclosing.run(() -> fire(Envelope::new));
    }

    @Test(expected = IllegalStateException.class)
    public final void run_noEnvelope() throws IOException, SQLException {
        disclosing.run(() -> fire(IllegalStateException::new));
    }

    private static class Envelope extends RuntimeException {
        private Envelope() {
        }

        private Envelope(final Throwable cause) {
            super(cause);
        }
    }
}
