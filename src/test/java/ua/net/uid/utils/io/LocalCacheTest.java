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
package ua.net.uid.utils.io;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

//@SuppressWarnings("unchecked")
class LocalCacheTest {

    @Test
    void testFuture_GenericType() throws InterruptedException, ExecutionException {
        LocalCache<String, Object> instance = new LocalCache<>();
        instance.set("test", 123);
        Future<Object> result = instance.future("test");
        assertNotNull(result);
        assertEquals(123, result.get());
    }

    @Test
    void testFuture_GenericType_Callable() throws InterruptedException, ExecutionException {
        LocalCache<String, Object> instance = new LocalCache<>();
        Future<Object> result = instance.future("test", () -> 456);
        assertNotNull(result);
        assertEquals(456, result.get());
    }

    @Test
    void testGet_GenericType() {
        LocalCache<String, Object> instance = new LocalCache<>();
        instance.set("test", 789);
        Object result = instance.get("test");
        assertNotNull(result);
        assertEquals(789, result);
    }

    @Test
    void testGet_GenericType_Callable() {
        LocalCache<String, Object> instance = new LocalCache<>();
        Object result = instance.get("test", () -> 65535);
        assertNotNull(result);
        assertEquals(65535, result);
    }

    @Test
    void testSet_GenericType_GenericType() throws InterruptedException, ExecutionException {
        LocalCache<String, Object> instance = new LocalCache<>();
        instance.set("test", 2345);
        Future<Object> result = instance.future("test");
        assertNotNull(result);
        assertEquals(2345, result.get());
    }

    @Test
    void testSet_GenericType_Callable() throws InterruptedException, ExecutionException {
        LocalCache<String, Object> instance = new LocalCache<>();
        instance.put("testSet_GenericType_Callable", () -> "testSet_GenericType_Callable");
        Future<Object> result = instance.future("testSet_GenericType_Callable");
        assertNotNull(result);
        assertEquals("testSet_GenericType_Callable", result.get());
    }

    @Test
    void testExtractFuture() throws InterruptedException, ExecutionException {
        LocalCache<String, Object> instance = new LocalCache<>();
        instance.put("testExtractFuture", () -> "testExtractFuture");
        Future<Object> result = instance.extractFuture("testExtractFuture");
        assertNotNull(result);
        assertNull(instance.future("testExtractFuture"));
        assertEquals("testExtractFuture", result.get());
    }

    @Test
    void testExtract() {
        LocalCache<String, Object> instance = new LocalCache<>();
        instance.put("testExtract", () -> "testExtract");
        Object result = instance.extract("testExtract");
        assertNotNull(result);
        assertNull(instance.future("testExtract"));
        assertEquals("testExtract", result);
    }

    @Test
    void testRemove() {
        LocalCache<String, Object> instance = new LocalCache<>();
        instance.put("testRemove", () -> "testRemove");
        instance.remove("testExtract");
        assertNull(instance.future("testExtract"));
    }

    @Test
    void testClear() {
        LocalCache<String, Object> instance = new LocalCache<>();
        instance.put("testClear", () -> "testClear");
        instance.clear();
        assertNull(instance.future("testClear"));
    }

    @Test
    void testCount() {
        LocalCache<Integer, Object> instance = new LocalCache<>();
        instance.set(0, 0);
        instance.set(1, 1);
        instance.set(2, 2);
        assertEquals(3, instance.count());
    }
}
