/*
 * Copyright 2019 nightfall.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
