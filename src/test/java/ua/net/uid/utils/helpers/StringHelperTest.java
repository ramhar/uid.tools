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

import java.io.IOException;
import java.util.Arrays;

@SuppressWarnings("SpellCheckingInspection")
class StringHelperTest {

    @Test
    void testByteArrayAsHexToCharArray() {
        byte[] src = new byte[256];
        for (int i = 0; i < 256; ++i) {
            src[i] = (byte) i;
        }
        char[] dst = "000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f202122232425262728292a2b2c2d2e2f303132333435363738393a3b3c3d3e3f404142434445464748494a4b4c4d4e4f505152535455565758595a5b5c5d5e5f606162636465666768696a6b6c6d6e6f707172737475767778797a7b7c7d7e7f808182838485868788898a8b8c8d8e8f909192939495969798999a9b9c9d9e9fa0a1a2a3a4a5a6a7a8a9aaabacadaeafb0b1b2b3b4b5b6b7b8b9babbbcbdbebfc0c1c2c3c4c5c6c7c8c9cacbcccdcecfd0d1d2d3d4d5d6d7d8d9dadbdcdddedfe0e1e2e3e4e5e6e7e8e9eaebecedeeeff0f1f2f3f4f5f6f7f8f9fafbfcfdfeff".toCharArray();
        char[] result = StringHelper.toHex(src);
        assertArrayEquals(dst, result);
    }

    @Test
    void testByteArrayAsHexToAppendable() throws Exception {
        byte[] src = new byte[256];
        for (int i = 0; i < 256; ++i) {
            src[i] = (byte) i;
        }
        String dst = "000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f202122232425262728292a2b2c2d2e2f303132333435363738393a3b3c3d3e3f404142434445464748494a4b4c4d4e4f505152535455565758595a5b5c5d5e5f606162636465666768696a6b6c6d6e6f707172737475767778797a7b7c7d7e7f808182838485868788898a8b8c8d8e8f909192939495969798999a9b9c9d9e9fa0a1a2a3a4a5a6a7a8a9aaabacadaeafb0b1b2b3b4b5b6b7b8b9babbbcbdbebfc0c1c2c3c4c5c6c7c8c9cacbcccdcecfd0d1d2d3d4d5d6d7d8d9dadbdcdddedfe0e1e2e3e4e5e6e7e8e9eaebecedeeeff0f1f2f3f4f5f6f7f8f9fafbfcfdfeff";
        Appendable builder = new StringBuilder();
        StringHelper.toHex(builder, src);
        assertEquals(dst, builder.toString());
    }

    @Test
    void testEscapeToAppendableWithRange() throws Exception {
        String src = "a\nsd`!@#$%^&*()\u0001\"'\n\r\\\t\b\fsdfцыувфыв\u0017";
        String dst = "d`!@#$%^&*()\\u0001\\\"'\\n\\r\\\\\\t\\b\\fsdfцыувф";
        Appendable builder = new StringBuilder();
        StringHelper.escape(builder, src, 3, src.length() - 3);
        assertEquals(dst, builder.toString());
    }

    @Test
    void testEscapeToAppendable() throws Exception {
        String src = "asd`!@#$%^&*()\u0001\"'\n\r\\\t\b\fsdfцыувфыв\u0017";
        String dst = "asd`!@#$%^&*()\\u0001\\\"'\\n\\r\\\\\\t\\b\\fsdfцыувфыв\\u0017";
        Appendable builder = new StringBuilder();
        StringHelper.escape(builder, src);
        assertEquals(dst, builder.toString());
    }

    @Test
    void testEscapeToString() {
        String src = "asd`!@#$%^&*()\u0001\"'\n\r\\\t\b\fsdfцыувфыв\u0017";
        String dst = "asd`!@#$%^&*()\\u0001\\\"'\\n\\r\\\\\\t\\b\\fsdfцыувфыв\\u0017";
        assertEquals(dst, StringHelper.escape(src));
    }

    @Test
    void testUnescapeToAppendable() throws Exception {
        String src = "asd`!@#$%^&*()\\u0001\\\"'\\n\\r\\\\\\t\\b\\fsdfцыувфыв\\u0017";
        String dst = "asd`!@#$%^&*()\u0001\"'\n\r\\\t\b\fsdfцыувфыв\u0017";
        Appendable builder = new StringBuilder();
        StringHelper.unescape(builder, src);
        assertEquals(dst, builder.toString());
    }

    @Test
    void testUnescapeToString() {
        String src = "asd`!@#$%^&*()\\u0001\\\"'\\n\\r\\\\\\t\\b\\fsdfцыувфыв\\u0017";
        String dst = "asd`!@#$%^&*()\u0001\"'\n\r\\\t\b\fsdfцыувфыв\u0017";
        assertEquals(dst, StringHelper.unescape(src));
    }

    @Test
    void testTrimStringNotThrowNPE() {
        assertNull(StringHelper.trim(null));
        assertEquals("abc", StringHelper.trim("\t abc\r\n"));
    }

    @Test
    void testLtrimStringNotThrowNPE() {
        assertNull(StringHelper.trim(null));
        assertEquals("abc\r\n", StringHelper.ltrim("\t abc\r\n"));
    }

    @Test
    void testRtrimStringNotThrowNPE() {
        assertNull(StringHelper.trim(null));
        assertEquals("\t abc", StringHelper.rtrim("\t abc\r\n"));
    }

    @Test
    void testSkipWhitespaceTest() {
        String src = "asd qwe\t\n123\f\f\fzzz   ";
        assertEquals(0, StringHelper.skipWhitespace(src, 0));
        assertEquals(4, StringHelper.skipWhitespace(src, 3));
        assertEquals(9, StringHelper.skipWhitespace(src, 7));
        assertEquals(15, StringHelper.skipWhitespace(src, 12));
        assertEquals(15, StringHelper.skipWhitespace(src, 15));
        assertEquals(src.length(), StringHelper.skipWhitespace(src, 18));
    }

    @Test
    void testJoinToBuilderWithEmptyAndNull() throws IOException {
        StringBuilder builder = new StringBuilder();
        Iterable<Object> src = Arrays.asList(1, 2, null, "q", "w", "");
        
        StringHelper.joinTo(builder, ",", src.iterator(), "E", "N");
        assertEquals("1,2,N,q,w,E", builder.toString()); builder.setLength(0);

        StringHelper.joinTo(builder, ",", src.iterator(), "E", "");
        assertEquals("1,2,,q,w,E", builder.toString()); builder.setLength(0);

        StringHelper.joinTo(builder, ",", src.iterator(), "E", null);
        assertEquals("1,2,q,w,E", builder.toString()); builder.setLength(0);

        StringHelper.joinTo(builder, ",", src.iterator(), "", "N");
        assertEquals("1,2,N,q,w,", builder.toString()); builder.setLength(0);

        StringHelper.joinTo(builder, ",", src.iterator(), null, "N");
        assertEquals("1,2,N,q,w", builder.toString()); builder.setLength(0);
    }

    @Test
    void testJoinToBuilderWithEmpty() throws IOException {
        StringBuilder builder = new StringBuilder();
        Iterable<Object> src = Arrays.asList(1, 2, null, "q", "w", "");

        StringHelper.joinTo(builder, ",", src.iterator(), "-");
        assertEquals("1,2,-,q,w,-", builder.toString()); builder.setLength(0);

        StringHelper.joinTo(builder, ",", src.iterator(), "");
        assertEquals("1,2,,q,w,", builder.toString()); builder.setLength(0);

        StringHelper.joinTo(builder, ",", src.iterator(), null);
        assertEquals("1,2,q,w", builder.toString()); builder.setLength(0);
    }

    @Test
    void testJoinToBuilder() throws IOException {
        StringBuilder builder = new StringBuilder();
        StringHelper.joinTo(builder, ",", Arrays.asList(1, 2, null, "q", "w", "").iterator());
        assertEquals("1,2,,q,w,", builder.toString()); builder.setLength(0);
    }

    @Test
    void testJoinItemsToBuilder() throws IOException {
        StringBuilder builder = new StringBuilder();
        StringHelper.joinItemsTo(builder, ",", 1, 2, null, "q", "w", "");
        assertEquals("1,2,,q,w,", builder.toString()); builder.setLength(0);
    }

    @Test
    void testJoinWithEmptyAndNull() {
        Iterable<Object> src = Arrays.asList(1, 2, null, "q", "w", "");
        assertEquals("1,2,N,q,w,E", StringHelper.join(",", src.iterator(), "E", "N").toString());
        assertEquals("1,2,,q,w,E", StringHelper.join(",", src.iterator(), "E", "").toString());
        assertEquals("1,2,q,w,E", StringHelper.join(",", src.iterator(), "E", null).toString());
        assertEquals("1,2,N,q,w,", StringHelper.join(",", src.iterator(), "", "N").toString());
        assertEquals("1,2,N,q,w", StringHelper.join(",", src.iterator(), null, "N").toString());
    }

    @Test
    void testJoinWithEmpty() throws IOException {
        Iterable<Object> src = Arrays.asList(1, 2, null, "q", "w", "");
        assertEquals("1,2,-,q,w,-", StringHelper.join(",", src.iterator(), "-").toString());
        assertEquals("1,2,,q,w,", StringHelper.join(",", src.iterator(), "").toString());
        assertEquals("1,2,q,w", StringHelper.join(",", src.iterator(), null).toString());
    }

    @Test
    void testJoin() throws IOException {
        Iterable<Object> src = Arrays.asList(1, 2, null, "q", "w", "");
        assertEquals("1,2,,q,w,", StringHelper.join(",", src.iterator()).toString());
    }

    @Test
    void testJoinItems() throws IOException {
        assertEquals("1,2,,q,w,", StringHelper.joinItems(",", 1, 2, null, "q", "w", "").toString());
    }

    @Test
    void testIsAscii() {
        for(int i = 0; i < 128; ++i) 
            assertTrue(StringHelper.isAscii(i));
        for(int i = 7; i < 32; ++i)
            assertFalse(StringHelper.isAscii(1 + (1 << i)));
        assertFalse(StringHelper.isAscii(0xffffffff));
    }

    @Test
    void testisAsciiUpper() {
        for(int i = 0; i < 'A'; ++i) 
            assertFalse(StringHelper.isAsciiUpper(i));
        for(int i = 'A'; i <= 'Z'; ++i) 
            assertTrue(StringHelper.isAsciiUpper(i));
        for(int i = 'Z' + 1; i <= 128; ++i) 
            assertFalse(StringHelper.isAsciiUpper(i));
        for(int i = 7; i < 32; ++i)
            assertFalse(StringHelper.isAsciiUpper(1 + (1 << i)));
        assertFalse(StringHelper.isAsciiUpper(0xffffffff));
    }

    @Test
    void testIsAsciiLower() {
        for(int i = 0; i < 'a'; ++i) 
            assertFalse(StringHelper.isAsciiLower(i));
        for(int i = 'a'; i <= 'z'; ++i) 
            assertTrue(StringHelper.isAsciiLower(i));
        for(int i = 'z' + 1; i <= 128; ++i) 
            assertFalse(StringHelper.isAsciiLower(i));
        for(int i = 7; i < 32; ++i)
            assertFalse(StringHelper.isAsciiLower(1 + (1 << i)));
        assertFalse(StringHelper.isAsciiLower(0xffffffff));
    }

    @Test
    void testIsAsciiDigit() {
        for(int i = 0; i < '0'; ++i) 
            assertFalse(StringHelper.isAsciiDigit(i));
        for(int i = '0'; i <= '9'; ++i) 
            assertTrue(StringHelper.isAsciiDigit(i));
        for(int i = '9' + 1; i <= 128; ++i) 
            assertFalse(StringHelper.isAsciiDigit(i));
        for(int i = 7; i < 32; ++i)
            assertFalse(StringHelper.isAsciiDigit(1 + (1 << i)));
        assertFalse(StringHelper.isAsciiDigit(0xffffffff));
    }

    @Test
    void testIsBlank() {
        assertTrue(StringHelper.isBlank(null));
        assertTrue(StringHelper.isBlank(""));
        assertTrue(StringHelper.isBlank(" \t\r\n\u005Ct"));
        assertFalse(StringHelper.isBlank("a"));
        assertFalse(StringHelper.isBlank(" \t\r\n\u005Ctq"));
        assertFalse(StringHelper.isBlank(" \t\r\n\0x2F81A"));
    }

    @Test
    void testLength() {
        assertEquals(0, StringHelper.length(null));
        assertEquals(0, StringHelper.length(""));
        assertEquals(3, StringHelper.length("123"));
    }

    @Test
    void testToCodePoints() {
        assertNull(StringHelper.toCodePoints(null));
        assertEquals(0, StringHelper.toCodePoints("").length);
        String src = "Пří視客øĥäΘい파ป็م♛";
        int[] codes = {1055, 345, 237, 35222, 23458, 248, 293, 228, 920, 12356, 54028, 3611, 3655, 1605, 9819};
        // String dst = new String(codes, 0, codes.length);
        assertArrayEquals(codes, StringHelper.toCodePoints(src));
    }
}
