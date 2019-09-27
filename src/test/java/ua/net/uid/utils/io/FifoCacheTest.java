package ua.net.uid.utils.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author nightfall
 */
public class FifoCacheTest {
    @Test
    public void testGet() {
        FifoCache<Integer, Integer> cache = new FifoCache<>(5);
        for (int i = 0; i < 5; ++i) { // new values
            assertEquals(Integer.valueOf(i * i + 1), cache.get(i, (t) -> t * t + 1));
        }
        for (int i = 0; i < 5; ++i) { // exists values
            assertEquals(Integer.valueOf(i * i + 1), cache.get(i, (t) -> 0));
        }
        assertEquals(Integer.valueOf(6 * 6), cache.get(6, (t) -> t * t));
        assertEquals(Integer.valueOf(0), cache.get(0, (t) -> 0));
    }
    /*
    @Test
    public void testTmp() {
        TimeZone zone = TimeZone.getTimeZone("Europe/Kiev");
        Calendar calendar = Calendar.getInstance(zone);
        calendar.setTimeInMillis(1566223311001L);
        
        assertEquals(19, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(17, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(1, calendar.get(Calendar.MINUTE));
        assertEquals(51, calendar.get(Calendar.SECOND));
        assertEquals(1, calendar.get(Calendar.MILLISECOND));
        
        calendar.set(Calendar.MILLISECOND, 0);
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
        assertEquals(1566223311000L, calendar.getTimeInMillis());

        calendar.set(Calendar.SECOND, 0);
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(1566223260000L, calendar.getTimeInMillis());

        calendar.set(Calendar.MINUTE, 0);
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(1566223200000L, calendar.getTimeInMillis());
    }
    */
}
