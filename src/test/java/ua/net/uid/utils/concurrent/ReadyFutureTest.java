package ua.net.uid.utils.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ReadyFutureTest {

    @Test
    void testCancel() {
        ReadyFuture<?> instance = new ReadyFuture<>(null);
        assertFalse(instance.cancel(true));
        assertFalse(instance.cancel(false));
    }

    @Test
    void testIsCancelled() {
        ReadyFuture<?> instance = new ReadyFuture<>(null);
        assertFalse(instance.isCancelled());
    }

    @Test
    void testIsDone() {
        ReadyFuture<?> instance = new ReadyFuture<>(null);
        assertTrue(instance.isDone());
    }

    @Test
    void testGet() {
        ReadyFuture<?> instance = new ReadyFuture<>(3);
        assertEquals(3, instance.get());
    }

    @Test
    void testGet_long_TimeUnit() {
        ReadyFuture<?> instance = new ReadyFuture<>(4.4);
        assertEquals(4.4, instance.get(1, TimeUnit.NANOSECONDS));
    }
}
