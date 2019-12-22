package de.team33.test.exceptional.v3;

import de.team33.libs.exceptional.v3.Disclosure;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.team33.libs.exceptional.v3.Disclosing.on;

public class DisclosureTest {

    private static final Logger LOGGER = Logger.getLogger(DisclosureTest.class.getCanonicalName());

    private final Disclosure<Envelope, IOException> disclosing = on(Envelope.class)
            .disclose(IOException.class, fallback -> LOGGER.log(Level.WARNING, "Undisclosed", fallback));

    private static <T extends Throwable> void fire(final Supplier<T> supplier) throws T {
        throw supplier.get();
    }

    @Test(expected = IOException.class)
    public final void run_Envelope_IOException() throws IOException {
        disclosing.run(() -> fire(() -> new Envelope(new IOException())));
    }

    @Test(expected = Envelope.class)
    public final void run_Envelope_IllegalArgumentException() throws IOException {
        disclosing.run(() -> fire(() -> new Envelope(new IllegalArgumentException())));
    }

    @Test(expected = Envelope.class)
    public final void run_Envelope_OutOfMemoryError() throws IOException {
        disclosing.run(() -> fire(() -> new Envelope(new OutOfMemoryError())));
    }

    @Test(expected = Envelope.class)
    public final void run_Envelope_SAXException() throws IOException {
        disclosing.run(() -> fire(() -> new Envelope(new SAXException())));
    }

    @Test(expected = Envelope.class)
    public final void run_Envelope_noCause() throws IOException {
        disclosing.run(() -> fire(Envelope::new));
    }

    @Test(expected = IllegalStateException.class)
    public final void run_noEnvelope() throws IOException {
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
