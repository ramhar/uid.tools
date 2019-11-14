package ua.net.uid.utils.helpers;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonHelperTest {

    CommonHelperTest() {
    }

    @Test
    void testClose() {
        AutoCloseableImpl closeable = new AutoCloseableImpl();
        CommonHelper.close(closeable);
        assertTrue(closeable.called);
    }

    @Test
    void testIsEmptyCharSequence() {
        StringBuilder src = null;
        assertTrue(CommonHelper.isEmpty((CharSequence) null));
        src = new StringBuilder();
        assertTrue(CommonHelper.isEmpty(src));
        src.append("test");
        assertFalse(CommonHelper.isEmpty(src));
    }

    @Test
    void testIsEmptyString() {
        String src = null;
        assertTrue(CommonHelper.isEmpty(src));
        src = "";
        assertTrue(CommonHelper.isEmpty(src));
        src = "test";
        assertFalse(CommonHelper.isEmpty(src));
    }

    @Test
    void testIsEmptyCollection() {
        Collection<Object> src = null;
        assertTrue(CommonHelper.isEmpty(src));
        src = new ArrayList<>();
        assertTrue(CommonHelper.isEmpty(src));
        src.add("1");
        assertFalse(CommonHelper.isEmpty(src));
    }

    @Test
    void testIsEmptyMap() {
        Map<Object, Object> src = null;
        assertTrue(CommonHelper.isEmpty(src));
        src = new HashMap<>();
        assertTrue(CommonHelper.isEmpty(src));
        src.put(1, "1");
        assertFalse(CommonHelper.isEmpty(src));
    }

    @Test
    void testIsEmptyGenericArray() {
        Integer[] src = null;
        assertTrue(CommonHelper.isEmpty(src));
        src = new Integer[0];
        assertTrue(CommonHelper.isEmpty(src));
        src = new Integer[]{1};
        assertFalse(CommonHelper.isEmpty(src));
    }

    @Test
    void testIsEmptyObject() {
        assertTrue(CommonHelper.isEmpty((Object) null));
        assertTrue(CommonHelper.isEmpty((Object) ""));
        assertFalse(CommonHelper.isEmpty((Object) "0"));
        assertTrue(CommonHelper.isEmpty((Object) new byte[] {}));
        assertFalse(CommonHelper.isEmpty((Object) new int[] {1,2,3}));
        assertTrue(CommonHelper.isEmpty((Object) Arrays.asList()));
        assertFalse(CommonHelper.isEmpty((Object) Arrays.asList(1,2,3)));
        assertTrue(CommonHelper.isEmpty((Object) Collections.emptyMap()));
        Map<String, Integer> map = new HashMap<>();
        assertTrue(CommonHelper.isEmpty((Object) map));
        map.put("q", 1);
        assertFalse(CommonHelper.isEmpty((Object) map));
    }

    @Test
    void testCoalesce() {
        assertNull(CommonHelper.coalesce());
        assertNull(CommonHelper.coalesce(null, null));
        assertEquals(1, CommonHelper.coalesce(1, null, null));
        assertEquals(2, CommonHelper.coalesce(null, 2, null));
        assertEquals(3, CommonHelper.coalesce(null, null, 3));
        assertEquals(1, CommonHelper.coalesce(1, null, 3));
    }

    @Test
    void testCompareNullable() {
        assertTrue(CommonHelper.compare(null, null, true) == 0);
        assertTrue(CommonHelper.compare(null, null, false) == 0);
        assertTrue(CommonHelper.compare(null, 1, false) < 0);
        assertTrue(CommonHelper.compare(null, 1, true) > 0);
        assertTrue(CommonHelper.compare(1, null, false) > 0);
        assertTrue(CommonHelper.compare(1, null, true) < 0);
        assertTrue(CommonHelper.compare(1, 1, false) == 0);
        assertTrue(CommonHelper.compare(1, 1, true) == 0);
        assertTrue(CommonHelper.compare(-1, 1, false) < 0);
        assertTrue(CommonHelper.compare(-1, 1, true) < 0);
        assertTrue(CommonHelper.compare(1, -1, false) > 0);
        assertTrue(CommonHelper.compare(1, -1, true) > 0);
    }

    @Test
    void testMinComparable() {
        assertNull(CommonHelper.min());
        assertNull(CommonHelper.min((Integer) null));
        assertEquals(0, CommonHelper.min(null, 3, 1, 0, 2, 4, null));
    }

    @Test
    void testMaxComparable() {
        assertNull(CommonHelper.max());
        assertNull(CommonHelper.max((Integer) null));
        assertEquals(4, CommonHelper.max(null, 3, 1, 0, 2, 4, null));
    }

    @Test
    void testMinByComparator() {
        Comparator<Integer> comparator = (a, b) -> CommonHelper.compare(a, b, true);
        assertNull(CommonHelper.minBy(comparator));
        assertNull(CommonHelper.minBy(comparator, (Integer) null));
        assertEquals(0, CommonHelper.minBy(comparator, null, 3, 1, 0, 2, 4, null));
    }

    @Test
    void testMaxByComparator() {
        Comparator<Integer> comparator = (a, b) -> CommonHelper.compare(a, b, false);
        assertNull(CommonHelper.maxBy(comparator));
        assertNull(CommonHelper.maxBy(comparator, (Integer) null));
        assertEquals(4, CommonHelper.maxBy(comparator, null, 3, 1, 0, 2, 4, null));
    }

    @Test
    void testSetOf() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(3,1,5,7,3));
        Set<Integer> actual = CommonHelper.setOf(3,1,1,5,7,3,5);
        assertEquals(expected, actual);
    }

    static class AutoCloseableImpl implements AutoCloseable {
        boolean called = false;

        @Override
        public void close() throws Exception {
            called = true; Map.of();
            throw new IOException("test exception");
        }
    }
}
