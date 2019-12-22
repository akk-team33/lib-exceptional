package de.team33.test.exceptional.v3;

import de.team33.libs.exceptional.v3.TriDisclosure;
import de.team33.libs.exceptional.v3.Disclosing;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TriDisclosureTest {

    private static final Logger LOGGER = Logger.getLogger(TriDisclosureTest.class.getCanonicalName());

    private final TriDisclosure<Envelope, IOException, SQLException, InterruptedException> disclosing = Disclosing
            .on(Envelope.class)
            .disclose(
                    IOException.class, SQLException.class, InterruptedException.class,
                    fallback -> LOGGER.log(Level.WARNING, "Undisclosed", fallback));

    private static <T extends Throwable> void fire(final Supplier<T> supplier) throws T {
        throw supplier.get();
    }

    @Test(expected = IOException.class)
    public final void run_Envelope_IOException() throws Exception {
        disclosing.run(() -> fire(() -> new Envelope(new IOException())));
    }

    @Test(expected = SQLException.class)
    public final void run_Envelope_SQLException() throws Exception {
        disclosing.run(() -> fire(() -> new Envelope(new SQLException())));
    }

    @Test(expected = InterruptedException.class)
    public final void run_Envelope_InterruptedException() throws Exception {
        disclosing.run(() -> fire(() -> new Envelope(new InterruptedException())));
    }

    @Test(expected = Envelope.class)
    public final void run_Envelope_IllegalArgumentException() throws Exception {
        disclosing.run(() -> fire(() -> new Envelope(new IllegalArgumentException())));
    }

    @Test(expected = Envelope.class)
    public final void run_Envelope_OutOfMemoryError() throws Exception {
        disclosing.run(() -> fire(() -> new Envelope(new OutOfMemoryError())));
    }

    @Test(expected = Envelope.class)
    public final void run_Envelope_SAXException() throws Exception {
        disclosing.run(() -> fire(() -> new Envelope(new SAXException())));
    }

    @Test(expected = Envelope.class)
    public final void run_Envelope_noCause() throws Exception {
        disclosing.run(() -> fire(Envelope::new));
    }

    @Test(expected = IllegalStateException.class)
    public final void run_noEnvelope() throws Exception {
        disclosing.run(() -> fire(IllegalStateException::new));
    }

    private static final class Envelope extends RuntimeException {
        private Envelope() {
        }

        private Envelope(final Throwable cause) {
            super(cause);
        }
    }
}
