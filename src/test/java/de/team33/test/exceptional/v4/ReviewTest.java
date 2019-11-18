package de.team33.test.exceptional.v4;

import de.team33.libs.exceptional.v3.XRunnable;
import de.team33.libs.exceptional.v4.Review;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.Assert.*;

public class ReviewTest {

    private static <X extends Exception> void getAndThrow(final Supplier<X> supplier) throws X {
        throw supplier.get();
    }

    @Test
    public void reThrowIf() {
        final Supplier<Exception> newSubExceptionA = SubExceptionA::new;
        final Supplier<Exception> newSubExceptionB = SubExceptionB::new;
        final Supplier<Exception> newSubExceptionC = SubExceptionC::new;
        final Supplier<Exception> newIOException = IOException::new;
        final Supplier<Exception> newIllegalArgumentException = IllegalArgumentException::new;
        final List<Supplier<Exception>> newExceptionList = Arrays.asList(
                newSubExceptionA, newSubExceptionB, newSubExceptionC, newIOException, newIllegalArgumentException);
        for (final Supplier<Exception> newException : newExceptionList) {
            try {
                try {
                    getAndThrow(newException);
                    fail("should not happen");
                } catch (RuntimeException caught) {
                    assertSame(newIllegalArgumentException, newException);
                } catch (Exception caught) {
                    Review.of(caught)
                          .reThrowIf(SubExceptionA.class)
                          .reThrowIf(SubExceptionB.class)
                          .reThrowIf(SubExceptionC.class);
                    assertSame(newIOException, newException);
                }
            } catch (SubExceptionA caught) {
                assertSame(newSubExceptionA, newException);
            } catch (SubExceptionB caught) {
                assertSame(newSubExceptionB, newException);
            } catch (SubExceptionC caught) {
                assertSame(newSubExceptionC, newException);
            }
        }
    }

    /**
     * Test for {@link Review#reThrowCauseIf(Class)}.
     *
     * Die Methode dient dazu,
     */
    @Test
    public void reThrowCauseIf() {
        final Supplier<RuntimeException> newSubExceptionA = () -> new RuntimeException(new SubExceptionA());
        final Supplier<RuntimeException> newSubExceptionB = () -> new RuntimeException(new SubExceptionB());
        final List<Supplier<? extends Exception>> newExceptionList = Arrays.asList(
                newSubExceptionA, newSubExceptionB);
        for (final Supplier<? extends Exception> newException : newExceptionList) {
            final XRunnable<SuperException> inner = () -> {
                try {
                    getAndThrow(newException);
                    fail("should not happen");
                } catch (RuntimeException e) {
                    Review.of(e)
                          .reThrowCauseIf(SubExceptionA.class)
                          .reThrowCauseIf(SubExceptionB.class);
                    fail("should not happen");
                } catch (Exception e) {
                    fail("should not happen");
                }
            };
            try {
                inner.run();
            } catch (SubExceptionA subExceptionA) {
                assertSame(newSubExceptionA, newException);
            } catch (final SubExceptionB caught) {
                assertSame(newSubExceptionB, newException);
            } catch (SuperException e) {
                fail("should not happen");
            }
        }
    }

    @Test
    public void reThrowCauseIf0() {
        final Supplier<Exception> newSubExceptionA = () -> new IOException(new SubExceptionA());
        final Supplier<Exception> newSubExceptionB = () -> new IOException(new SubExceptionB());
        final Supplier<Exception> newSubExceptionC = () -> new IOException(new SubExceptionC());
        final Supplier<Exception> newIOException = () -> new IOException(new IOException());
        final Supplier<Exception> newIllegalArgumentException = () -> new IllegalArgumentException(new SubExceptionA());
        final List<Supplier<Exception>> newExceptionList = Arrays.asList(
                newSubExceptionA, newSubExceptionB, newSubExceptionC, newIOException, newIllegalArgumentException);
        for (final Supplier<Exception> newException : newExceptionList) {
            try {
                try {
                    getAndThrow(newException);
                    fail("should not happen");
                } catch (RuntimeException caught) {
                    assertSame(newIllegalArgumentException, newException);
                } catch (Exception caught) {
                    Review.of(caught)
                          .reThrowCauseIf(SubExceptionA.class)
                          .reThrowCauseIf(SubExceptionB.class)
                          .reThrowCauseIf(SubExceptionC.class);
                    assertSame(newIOException, newException);
                }
            } catch (SubExceptionA caught) {
                assertSame(newSubExceptionA, newException);
            } catch (SubExceptionB caught) {
                assertSame(newSubExceptionB, newException);
            } catch (SubExceptionC caught) {
                assertSame(newSubExceptionC, newException);
            }
        }
    }
}

class SuperException extends Exception {}

class SubExceptionA extends SuperException {}

class SubExceptionB extends SuperException {}

class SubExceptionC extends SuperException {}
