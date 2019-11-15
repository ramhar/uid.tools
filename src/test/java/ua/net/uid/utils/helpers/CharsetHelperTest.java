package ua.net.uid.utils.helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

class CharsetHelperTest {
    @Test
    void testIsSupported() {
        assertFalse(CharsetHelper.isSupported(null));
        assertFalse(CharsetHelper.isSupported(""));
        assertFalse(CharsetHelper.isSupported("Unknown"));
        assertTrue(CharsetHelper.isSupported("UTF-8"));
        assertTrue(CharsetHelper.isSupported("UTF-16"));
    }

    @Test
    void testToCharset() {
        assertEquals(Charset.defaultCharset(), CharsetHelper.toCharset(null));
        assertEquals(StandardCharsets.UTF_8, CharsetHelper.toCharset("UTF-8"));
        assertThrows(UnsupportedCharsetException.class, () -> CharsetHelper.toCharset("Unknown"));
    }

    @Test
    void testToCharsetName() {
        assertEquals(Charset.defaultCharset().name(), CharsetHelper.toCharsetName(null));
        assertEquals(StandardCharsets.UTF_8.name(), CharsetHelper.toCharsetName("UTF-8"));
        assertThrows(UnsupportedCharsetException.class, () -> CharsetHelper.toCharsetName("Unknown"));
    }
}
