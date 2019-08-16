package ua.net.uid.utils.helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}
