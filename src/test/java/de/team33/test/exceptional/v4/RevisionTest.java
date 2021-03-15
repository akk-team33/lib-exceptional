package de.team33.test.exceptional.v4;

import de.team33.libs.exceptional.v4.Revision;
import de.team33.libs.exceptional.v4.WrappedException;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

@SuppressWarnings("NestedTryStatement")
public class RevisionTest {

    private static <X extends Exception> void doThrow(final Supplier<X> supplier) throws X {
        throw supplier.get();
    }

    private static <T extends Throwable> Predicate<T> equalsMessage(final String message) {
        return subject -> subject.getMessage().endsWith(message);
    }

    @Test
    public final void throwMapped() {
        final Supplier<RuntimeException> newSubExceptionA = () -> new RuntimeException("SubExceptionA");
        final Supplier<RuntimeException> newSubExceptionB = () -> new RuntimeException("SubExceptionB");
        final Supplier<RuntimeException> newSubExceptionC = () -> new RuntimeException("SubExceptionC");
        final Supplier<RuntimeException> newIOException = () -> new RuntimeException("IOException");
        final List<Supplier<RuntimeException>> newExceptionList = Arrays.asList(
                newSubExceptionA, newSubExceptionB, newSubExceptionC, newIOException);
        for (final Supplier<RuntimeException> newException : newExceptionList) {
            try {
                try {
                    doThrow(newException);
                    fail("unexpected: no exception thrown");
                } catch (final RuntimeException caught) {
                    Revision.of(caught)
                            .throwMapped(subject -> Optional.of(subject)
                                                            .filter(equalsMessage("SubExceptionA"))
                                                            .map(SubExceptionA::new)
                                                            .orElse(null))
                            .throwMapped(subject -> Optional.of(subject)
                                                            .filter(equalsMessage("SubExceptionB"))
                                                            .map(SubExceptionB::new)
                                                            .orElse(null))
                            .throwMapped(subject -> Optional.of(subject)
                                                            .filter(equalsMessage("SubExceptionC"))
                                                            .map(SubExceptionC::new)
                                                            .orElse(null));
                    assertSame(newIOException, newException);
                }
            } catch (final SubExceptionA caught) {
                assertSame(newSubExceptionA, newException);
            } catch (final SubExceptionB caught) {
                assertSame(newSubExceptionB, newException);
            } catch (final SubExceptionC caught) {
                assertSame(newSubExceptionC, newException);
            }
        }
    }

    @Test
    public final void throwMappedCause() {
        final Supplier<RuntimeException> a = () -> new RuntimeException(new ClassCastException("IOX"));
        final Supplier<RuntimeException> b = () -> new RuntimeException(new IOException("SQX"));
        final Supplier<RuntimeException> c = () -> new RuntimeException(new SQLException("USX"));
        final Supplier<RuntimeException> d = () -> new RuntimeException(new URISyntaxException("CCX",""));
        for (final Supplier<RuntimeException> newException : Arrays.asList(a, b, c, d)) {
            try {
                try {
                    doThrow(newException);
                    fail("unexpected: no exception thrown");
                } catch (final RuntimeException caught) {
                    Revision.of(caught)
                            .throwMappedCause(cause -> Optional.of(cause)
                                                               .filter(equalsMessage("IOX"))
                                                               .map(IOException::new)
                                                               .orElse(null))
                            .throwMappedCause(cause -> Optional.of(cause)
                                                               .filter(equalsMessage("SQX"))
                                                               .map(SQLException::new)
                                                               .orElse(null))
                            .throwMappedCause(cause -> Optional.of(cause)
                                                               .filter(equalsMessage("USX"))
                                                               .map(cause1 -> new URISyntaxException(cause1.getClass().getCanonicalName(), ""))
                                                               .orElse(null))
                            .throwMappedCause(cause -> Optional.of(cause)
                                                               .filter(equalsMessage("CCX"))
                                                               .map(cause2 -> new ClassCastException(cause2.getClass().getCanonicalName()))
                                                               .orElse(null));
                    fail("unexpected: no exception thrown");
                }
            } catch (final IOException caught) {
                assertSame(a, newException);
            } catch (final SQLException caught) {
                assertSame(b, newException);
            } catch (final URISyntaxException caught) {
                assertSame(c, newException);
            } catch (final ClassCastException caught) {
                assertSame(d, newException);
            }
        }
    }

    @SuppressWarnings("StandardVariableNames")
    @Test
    public final void throwMappedCause_() {
        final Supplier<RuntimeException> a = () -> new RuntimeException(new ClassCastException("IOX"));
        final Supplier<RuntimeException> b = () -> new RuntimeException(new IOException("SQX"));
        final Supplier<RuntimeException> c = () -> new RuntimeException(new SQLException("USX"));
        final Supplier<RuntimeException> d = () -> new RuntimeException(new URISyntaxException("CCX",""));
        for (final Supplier<RuntimeException> newException : Arrays.asList(a, b, c, d)) {
            try {
                try {
                    doThrow(newException);
                    fail("unexpected: no exception thrown");
                } catch (final RuntimeException caught) {
                    Revision.of(caught)
                            .when(equalsMessage("IOX"))
                            .thenThrow(IOException::new)
                            .when(equalsMessage("SQX"))
                            .thenThrow(SQLException::new)
                            .when(equalsMessage("USX"))
                            .thenThrow(cause1 -> new URISyntaxException(cause1.getClass().getCanonicalName(), ""))
                            .when(equalsMessage("CCX"))
                            .thenThrow(cause2 -> new ClassCastException(cause2.getClass().getCanonicalName()));
                    fail("unexpected: no exception thrown");
                }
            } catch (final IOException caught) {
                assertSame(a, newException);
            } catch (final SQLException caught) {
                assertSame(b, newException);
            } catch (final URISyntaxException caught) {
                assertSame(c, newException);
            } catch (final ClassCastException caught) {
                assertSame(d, newException);
            }
        }
    }

    @Test
    public final void reThrowWhen() {
        final List<Exception> exceptionList = Arrays.asList(
                new IOException(),
                new SQLException(),
                new URISyntaxException("", ""),
                new ClassCastException());
        for (final Exception exception : exceptionList) {
            try {
                try {
                    doThrow(() -> new WrappedException(exception));
                    fail("unexpected: no exception thrown");
                } catch (final WrappedException caught) {
                    Revision.of(caught)
                            .reThrowWhen(IOException.class)
                            .reThrowWhen(SQLException.class)
                            .reThrowWhen(URISyntaxException.class)
                            .reThrowWhen(ClassCastException.class);
                    fail("unexpected: no exception thrown");
                }
            } catch (final IOException caught) {
                assertSame(exceptionList.get(0), caught);
            } catch (final SQLException caught) {
                assertSame(exceptionList.get(1), caught);
            } catch (final URISyntaxException caught) {
                assertSame(exceptionList.get(2), caught);
            } catch (final ClassCastException caught) {
                assertSame(exceptionList.get(3), caught);
            }
        }
    }

    static class SuperException extends Exception {
        SuperException() {
        }

        SuperException(final Throwable cause) {
            super(cause);
        }
    }

    static class SubExceptionA extends SuperException {
        SubExceptionA() {
        }

        SubExceptionA(final Throwable cause) {
            super(cause);
        }
    }

    static class SubExceptionB extends SuperException {
        SubExceptionB() {
        }

        SubExceptionB(final Throwable cause) {
            super(cause);
        }
    }

    static class SubExceptionC extends SuperException {
        SubExceptionC() {
        }

        SubExceptionC(final Throwable cause) {
            super(cause);
        }
    }
}
