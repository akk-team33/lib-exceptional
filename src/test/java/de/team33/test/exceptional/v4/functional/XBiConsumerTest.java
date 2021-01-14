package de.team33.test.exceptional.v4.functional;

import de.team33.libs.exceptional.v4.functional.XBiConsumer;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import static org.junit.Assert.*;

public class XBiConsumerTest {

    private static final BiConsumer<AtomicInteger, int[]> CONSUMER = (p1, p2) -> p2[0] = p1.incrementAndGet();

    @Test
    public final void treatBiConsumerAsXBiConsumer() {
        final AtomicInteger atomic = new AtomicInteger(0);
        final int[] array = {0};

        perform(CONSUMER::accept, atomic, array);
        assertEquals(1, atomic.get());
        assertEquals(1, array[0]);
    }

    private static <T, U, X extends Exception> void perform(final XBiConsumer<T, U, X> consumer,
                                                            final T t,
                                                            final U u) throws X {
        consumer.accept(t, u);
    }
}