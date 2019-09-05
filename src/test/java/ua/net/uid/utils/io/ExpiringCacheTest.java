package ua.net.uid.utils.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//TODO ExpiringCacheTest
class ExpiringCacheTest {
    
    @Test
    public void testSavingOfItems() throws Exception {
        ExpiringCache<Integer, Integer> instance = new ExpiringCache<>(2, 4f/3f);
        
        assertEquals(Integer.valueOf(1), instance.get(1, (key) -> key * key, 1500));
        assertEquals(Integer.valueOf(4), instance.get(2, (key) -> key * key, 2000));
        assertEquals(2, instance.getSize());
        assertEquals(2, instance.getTableSize());
        assertEquals(2, instance.getModifiedCount());
        
        assertEquals(Integer.valueOf(4), instance.get(2, (key) -> key, 2000));
        assertEquals(2, instance.getSize());
        assertEquals(2, instance.getTableSize());
        assertEquals(2, instance.getModifiedCount());
        
        assertEquals(Integer.valueOf(9), instance.get(3, (key) -> key * key, 2000));
        assertEquals(3, instance.getSize());
        assertEquals(3, instance.getTableSize());
        assertEquals(0, instance.getModifiedCount());
        
        assertEquals(Integer.valueOf(16), instance.get(4, (key) -> key * key, 2000));
        assertEquals(4, instance.getSize());
        assertEquals(3, instance.getTableSize());
        assertEquals(1, instance.getModifiedCount());

        assertEquals(Integer.valueOf(25), instance.get(-5, (key) -> key * key, 1000));
        assertEquals(5, instance.getSize());
        assertEquals(5, instance.getTableSize());
        assertEquals(0, instance.getModifiedCount());

        assertEquals(Integer.valueOf(25), instance.get(-5));
        assertEquals(5, instance.getSize());
        assertEquals(5, instance.getTableSize());
        assertEquals(0, instance.getModifiedCount());
        
        assertNull(instance.get(-5, System.currentTimeMillis() + 1000));
        assertEquals(4, instance.getSize());
        assertEquals(5, instance.getTableSize());
        assertEquals(1, instance.getModifiedCount());
        
        assertEquals(Integer.valueOf(25), instance.get(5, (key) -> key * key, 1));
        assertEquals(5, instance.getSize());
        assertEquals(5, instance.getTableSize());
        assertEquals(2, instance.getModifiedCount());
        
        assertEquals(Integer.valueOf(2), instance.get(1, (key) -> key + 1, 1, System.currentTimeMillis() + 1500));
        assertEquals(5, instance.getSize());
        assertEquals(5, instance.getTableSize());
        assertEquals(2, instance.getModifiedCount());
        
        assertEquals(Integer.valueOf(9), instance.get(3));
        assertEquals(Integer.valueOf(4), instance.get(2));
        instance.remove(3);
        assertNull(instance.get(3));
        assertEquals(Integer.valueOf(4), instance.get(2));
        
        assertEquals(4, instance.getSize());
        assertEquals(5, instance.getTableSize());
        assertEquals(3, instance.getModifiedCount());
        
        assertEquals(Integer.valueOf(3), instance.get(3, (key) -> key, 2000));
        assertEquals(5, instance.getSize());
        assertEquals(5, instance.getTableSize());
        assertEquals(4, instance.getModifiedCount());
        
        
    }

}