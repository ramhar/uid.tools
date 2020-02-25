/*
 * Copyright 2020 nightfall.
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
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author nightfall
 */
public class PromiseTest {
    
    public PromiseTest() {
    }

    @Test
    public void testResolveState() {
        Promise<Integer> promise = Promise.resolve(123);
        assertSame(Promise.State.FULFILLED, promise.getState());
        
        assertEquals(Integer.valueOf(123), promise.getValue());
        assertNull(promise.getError());
    }
    
    @Test
    public void testRejectState() {
        final Throwable error = new UnsupportedOperationException("test exception #1");
        Promise<Integer> promise = Promise.reject(error);
        assertSame(Promise.State.REJECTED, promise.getState());
        
        assertNull(promise.getValue());
        assertEquals(error, promise.getError());
    }
    
    @Test
    public void testPendingState() {
        Exception error = new UnsupportedOperationException("test throw");
        
        Promise<Integer> promise1 = new Promise<>(() -> {
            Thread.sleep(1);
            return 213;
        });
        Promise<Integer> promise2 = new Promise<>(() -> {
            Thread.sleep(1);
            throw error;
        });
        
        assertSame(Promise.State.PENDING, promise1.getState());
        assertSame(Promise.State.PENDING, promise2.getState());
        
        assertEquals(Integer.valueOf(213), promise1.getValue());
        assertNull(promise1.getError());
        assertSame(Promise.State.FULFILLED, promise1.getState());
        
        assertNull(promise2.getValue());
        assertEquals(error, promise2.getError());
        assertSame(Promise.State.REJECTED, promise2.getState());
    }
    
    
    
    
    
    
    
    
    
}
