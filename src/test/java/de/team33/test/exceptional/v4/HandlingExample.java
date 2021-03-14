package de.team33.test.exceptional.v4;

import de.team33.libs.exceptional.v4.ExpectationException;
import de.team33.libs.exceptional.v4.Handling;
import de.team33.libs.exceptional.v4.WrappedException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HandlingExample {

    public static final Logger LOGGER = Logger.getLogger(HandlingExample.class.getCanonicalName());

    public void doSomething() throws IOException, SQLException, URISyntaxException {
        try {
            doSomethingThatMayThrowAWrappedException();
        } catch (final WrappedException caught) {
            // We want to unwrap the cause of the caught exception and rethrow
            // it as a certain type of exception that meets our expectations ...
            Handling.of(caught)
                    .reThrowCauseIf(IOException.class)
                    .reThrowCauseIf(SQLException.class)
                    .reThrowCauseIf(URISyntaxException.class);
            // In the event that our expectation does not apply ...
            LOGGER.log(Level.WARNING, "unexpected exception", caught.getCause());
        }
    }

    public void doSomethingElse() throws IOException, SQLException, URISyntaxException {
        try {
            doSomethingThatMayThrowAWrappedException();
        } catch (final WrappedException caught) {
            // We want to unwrap the cause of the caught exception and rethrow
            // it as a certain type of exception that meets our expectations ...
            throw Handling.of(caught)
                          .reThrowCauseIf(IOException.class)
                          .reThrowCauseIf(SQLException.class)
                          .reThrowCauseIf(URISyntaxException.class)
                          // Technically, it could happen that our expectations are not met.
                          // To be on the safe side, this should lead to a meaningful exception ...
                          .mappedCause(ExpectationException::new);
        }
    }

    private void doSomethingThatMayThrowAWrappedException() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    private Object getSomethingVolatile() throws WrappedException {
        // do something that may throw a WrappedException that may wrap several exceptions ...
        throw new UnsupportedOperationException("not yet implemented");
    }
}
