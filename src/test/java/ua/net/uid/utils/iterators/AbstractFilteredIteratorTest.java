package ua.net.uid.utils.iterators;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbstractFilteredIteratorTest {
    private final List<Object> source = Arrays.asList(1, "q", 3.14, null, "w", true, "r", 4);

    @Test
    void simpleIterateTest() {
        Iterator<Object> it = new StringFilteredIterator(source.iterator());
        assertTrue(it.hasNext());
        assertEquals("q", it.next());
        assertTrue(it.hasNext());
        assertEquals("w", it.next());
        assertTrue(it.hasNext());
        assertEquals("r", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void removeElementTest() {
        List<Object> items = new ArrayList<>(source);

        {
            Iterator<Object> it = new StringFilteredIterator(items.iterator());
            assertTrue(it.hasNext());
            assertEquals("q", it.next());
            assertTrue(it.hasNext());
            assertEquals("w", it.next());
            assertTrue(it.hasNext());
            assertEquals("r", it.next());
            it.remove();
            assertFalse(it.hasNext());
        }

        assertArrayEquals(new Object[]{1, "q", 3.14, null, "w", true, 4}, items.toArray());

        {
            Iterator<Object> it = new StringFilteredIterator(items.iterator());
            assertTrue(it.hasNext());
            try {
                it.remove();
                assertFalse(true);
            } catch (IllegalStateException ex) {
                assertNotNull(ex);
            }
            assertTrue(it.hasNext());
            assertEquals("q", it.next());
            it.remove();
            assertTrue(it.hasNext());
            assertEquals("w", it.next());
            it.remove();
            assertFalse(it.hasNext());
        }
        assertArrayEquals(new Object[]{1, 3.14, null, true, 4}, items.toArray());

    }

    private final static class StringFilteredIterator extends AbstractFilteredIterator<Object> {
        StringFilteredIterator(Iterator<?> iterator) {
            super(iterator);
        }

        @Override
        protected boolean check(Object item) {
            return item instanceof String;
        }
    }
}