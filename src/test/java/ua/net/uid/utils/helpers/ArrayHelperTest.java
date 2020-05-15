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
package ua.net.uid.utils.helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayHelperTest {
    final String[] source = new String[]{"v2", "v5", "v4", "v1", null, "v3", "v2", "v5", "v4", "v1", null, "v3"};

    @Test
    void indexOf() {
        assertEquals(3, ArrayHelper.indexOf(source, "v1"));
        assertEquals(0, ArrayHelper.indexOf(source, "v2"));
        assertEquals(-1, ArrayHelper.indexOf(source, "s1"));
        assertEquals(4, ArrayHelper.indexOf(source, null));
    }

    @Test
    void indexOfFrom() {
        assertEquals(-1, ArrayHelper.indexOf(source, "s1", 2));
        assertEquals(3, ArrayHelper.indexOf(source, "v1", 3));
        assertEquals(6, ArrayHelper.indexOf(source, "v2", 3));
        assertEquals(4, ArrayHelper.indexOf(source, null, 4));
        assertEquals(10, ArrayHelper.indexOf(source, null, 5));
        assertEquals(-1, ArrayHelper.indexOf(source, null, 11));
    }

    @Test
    void lastIndexOf() {
        assertEquals(9, ArrayHelper.lastIndexOf(source, "v1"));
        assertEquals(6, ArrayHelper.lastIndexOf(source, "v2"));
        assertEquals(-1, ArrayHelper.lastIndexOf(source, "s1"));
        assertEquals(10, ArrayHelper.lastIndexOf(source, null));
    }

    @Test
    void lastIndexOfFrom() {
        assertEquals(-1, ArrayHelper.lastIndexOf(source, "s1", 2));
        assertEquals(3, ArrayHelper.lastIndexOf(source, "v1", 3));
        assertEquals(0, ArrayHelper.lastIndexOf(source, "v2", 3));
        assertEquals(4, ArrayHelper.lastIndexOf(source, null, 4));
        assertEquals(-1, ArrayHelper.lastIndexOf(source, null, 3));
        assertEquals(10, ArrayHelper.lastIndexOf(source, null, 11));
    }

    /*
    @Test
    void testCollectionToArray() {
        List<String> source = Arrays.asList("v2", "v5", "v4", "v1", "v3");
        String[] target = ArrayHelper.toArray(source);
        assertArrayEquals(new String[]{"v2", "v5", "v4", "v1", "v3"}, target);
    }

    @Test
    void testCollectionToSortedArray() {
        List<String> source = Arrays.asList("v2", "v5", "v4", "v1", "v3");
        String[] target = ArrayHelper.toSortedArray(source);
        assertArrayEquals(new String[]{"v1", "v2", "v3", "v4", "v5"}, target);
    }

    @Test
    void testCollectionToSortedArrayWithComparator() {
        List<String> source = Arrays.asList("v2", "v5", "v4", "v1", "v3");
        @SuppressWarnings("ComparatorCombinators") String[] target = ArrayHelper.toSortedArray(source, (String o1, String o2) -> o2.compareTo(o1));
        assertArrayEquals(new String[]{"v5", "v4", "v3", "v2", "v1"}, target);
    }
    */
}
