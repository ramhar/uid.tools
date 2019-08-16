package ua.net.uid.utils.iterators;

import java.util.Iterator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayIteratorTest {
    @Test
    void simpleIterateTest() {
        Iterator<Integer> it = new ArrayIterator<>(new Integer[]{1, 2, 3});
        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(1), it.next());
        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(2), it.next());
        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(3), it.next());
        assertFalse(it.hasNext());
        try {
            it.next();
            assertFalse(true);
        } catch (Exception ex) {
            assertNotNull(ex);
        }
    }
}
