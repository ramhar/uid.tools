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
