package de.team33.test.exceptional.v4;

import de.team33.libs.exceptional.v4.Handling;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

@SuppressWarnings({"MultipleTopLevelClassesInFile", "NestedTryStatement"})
public class HandlingTest {

    private static <X extends Exception> void doThrow(final Supplier<X> supplier) throws X {
        throw supplier.get();
    }

    private static <T extends Throwable> Predicate<T> equalsMessage(final String message) {
        return subject -> message.equals(subject.getMessage());
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
                    Handling.of(caught)
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
                    // No handling took place. This is expected only if ...
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
    public final void reThrowIf() {
        final Supplier<Exception> newSubExceptionA = SubExceptionA::new;
        final Supplier<Exception> newSubExceptionB = SubExceptionB::new;
        final Supplier<Exception> newSubExceptionC = SubExceptionC::new;
        final Supplier<Exception> newIOException = IOException::new;
        final List<Supplier<Exception>> newExceptionList = Arrays.asList(
                newSubExceptionA, newSubExceptionB, newSubExceptionC, newIOException);
        for (final Supplier<Exception> newException : newExceptionList) {
            try {
                try {
                    doThrow(newException);
                    fail("unexpected: no exception thrown");
                } catch (final Exception caught) {
                    Handling.of(caught)
                            .reThrowIf(SubExceptionA.class)
                            .reThrowIf(SubExceptionB.class)
                            .reThrowIf(SubExceptionC.class);
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
        final Supplier<RuntimeException> subExceptionA = () -> new RuntimeException(new IOException("SubExceptionA"));
        final Supplier<RuntimeException> subExceptionB = () -> new RuntimeException(new IOException("SubExceptionB"));
        final Supplier<RuntimeException> subExceptionC = () -> new RuntimeException(new IOException("SubExceptionC"));
        final Supplier<RuntimeException> ioException = () -> new RuntimeException(new IOException());
        final List<Supplier<RuntimeException>> newExceptionList = Arrays.asList(
                subExceptionA, subExceptionB, subExceptionC, ioException);
        for (final Supplier<RuntimeException> newException : newExceptionList) {
            try {
                try {
                    doThrow(newException);
                    fail("unexpected: no exception thrown");
                } catch (final RuntimeException caught) {
                    Handling.of(caught)
                            .throwMappedCause(cause -> Optional.of(cause)
                                                               .filter(equalsMessage("SubExceptionA"))
                                                               .map(SubExceptionA::new)
                                                               .orElse(null))
                            .throwMappedCause(cause -> Optional.of(cause)
                                                               .filter(equalsMessage("SubExceptionB"))
                                                               .map(SubExceptionB::new)
                                                               .orElse(null))
                            .throwMappedCause(cause -> Optional.of(cause)
                                                               .filter(equalsMessage("SubExceptionC"))
                                                               .map(SubExceptionC::new)
                                                               .orElse(null));
                    assertSame(ioException, newException);
                }
            } catch (final SubExceptionA caught) {
                assertSame(subExceptionA, newException);
            } catch (final SubExceptionB caught) {
                assertSame(subExceptionB, newException);
            } catch (final SubExceptionC caught) {
                assertSame(subExceptionC, newException);
            }
        }
    }

    @Test
    public final void reThrowCauseIf() {
        final Supplier<RuntimeException> newSubExceptionA = () -> new RuntimeException(new SubExceptionA());
        final Supplier<RuntimeException> newSubExceptionB = () -> new RuntimeException(new SubExceptionB());
        final Supplier<RuntimeException> newSubExceptionC = () -> new RuntimeException(new SubExceptionC());
        final Supplier<RuntimeException> newIOException = () -> new RuntimeException(new IOException());
        final List<Supplier<RuntimeException>> newExceptionList = Arrays.asList(
                newSubExceptionA, newSubExceptionB, newSubExceptionC, newIOException);
        for (final Supplier<RuntimeException> newException : newExceptionList) {
            try {
                try {
                    doThrow(newException);
                    fail("unexpected: no exception thrown");
                } catch (final RuntimeException caught) {
                    Handling.of(caught)
                            .reThrowCauseIf(SubExceptionA.class)
                            .reThrowCauseIf(SubExceptionB.class)
                            .reThrowCauseIf(SubExceptionC.class);
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
