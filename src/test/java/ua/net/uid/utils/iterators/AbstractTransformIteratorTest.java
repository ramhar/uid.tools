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
package ua.net.uid.utils.iterators;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbstractTransformIteratorTest {
    private final List<Integer> source = Arrays.asList(1, 2, 3);

    @Test
    void simpleIterateTest() {
        Iterator<String> it = new IntToStringIterator(source.iterator());
        assertTrue(it.hasNext());
        assertEquals("1", it.next());
        assertTrue(it.hasNext());
        assertEquals("2", it.next());
        assertTrue(it.hasNext());
        assertEquals("3", it.next());
        assertFalse(it.hasNext());
        try {
            it.next();
            assertFalse(true);
        } catch (Exception ex) {
            assertNotNull(ex);
        }
    }

    @Test
    void removeItemTest() {
        List<Integer> list = new ArrayList<>(source);
        Iterator<String> it = new IntToStringIterator(list.iterator());
        assertTrue(it.hasNext());
        assertEquals("1", it.next());
        it.remove();
        assertEquals(2, list.size());
        assertTrue(it.hasNext());
        assertEquals("2", it.next());
        it.remove();
        assertEquals(1, list.size());
        assertTrue(it.hasNext());
        assertEquals("3", it.next());
        it.remove();
        assertTrue(list.isEmpty());
        assertFalse(it.hasNext());
    }

    private static final class IntToStringIterator extends AbstractTransformIterator<Integer, String> {
        IntToStringIterator(Iterator<? extends Integer> iterator) {
            super(iterator);
        }

        @Override
        protected String transform(Integer source) {
            return String.valueOf(source);
        }
    }
}