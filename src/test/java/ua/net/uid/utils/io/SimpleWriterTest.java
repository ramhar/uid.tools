package ua.net.uid.utils.io;

import org.junit.jupiter.api.Test;

import java.io.Writer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleWriterTest {

    @Test
    void testWriteChar() {
        try (SimpleWriter instance = new SimpleWriter()) {
            instance.write(32);
            assertEquals(" ", instance.toString());
        }
    }

    @Test
    void testWriteCharsFromOffsetWithLength() {
        char[] buf = "aaa0123456789bbb".toCharArray();
        try (SimpleWriter instance = new SimpleWriter()) {
            instance.write(buf, 3, 10);
            assertEquals("0123456789", instance.toString());
        }
    }

    @Test
    void testWriteString() {
        String str = "prototype";
        try (SimpleWriter instance = new SimpleWriter()) {
            instance.write(str);
            assertEquals(str, instance.toString());
        }
    }

    @Test
    void testWriteStringFromOffsetWithLength() {
        String buf = "aaa0123456789bbb";
        try (SimpleWriter instance = new SimpleWriter()) {
            instance.write(buf, 3, 10);
            assertEquals("0123456789", instance.toString());
        }
    }

    @Test
    void testAppendCharSequence() {
        try (SimpleWriter instance = new SimpleWriter()) {
            Writer result = instance.append("test");
            assertEquals(result, instance);
            assertEquals("test", result.toString());
        }
    }

    @Test
    void testAppendCharSequenceFromOffsetWithLength() {
        CharSequence data = "aaa0123456789bbb";
        try (SimpleWriter instance = new SimpleWriter()) {
            Writer result = instance.append(data, 3, 10);
            assertEquals(result, instance);
            assertEquals("0123456", result.toString());
        }
    }

    @Test
    void testAppendChar() {
        try (SimpleWriter instance = new SimpleWriter()) {
            Writer result = instance.append(' ');
            assertEquals(result, instance);
            assertEquals(" ", instance.toString());
        }
    }

    @Test
    void testFlush() {
        try (SimpleWriter instance = new SimpleWriter()) {
            instance.write("The test case is a prototype.");
            instance.flush();
            assertEquals("The test case is a prototype.", instance.toString());
        }
    }

    @Test
    void testClose() {
        try (SimpleWriter instance = new SimpleWriter()) {
            instance.write("The test case is a prototype.");
            instance.close();
            assertEquals("The test case is a prototype.", instance.toString());
        }
    }

    @Test
    void testLength() {
        String data = "aaa0123456789bbb";
        try (SimpleWriter instance = new SimpleWriter()) {
            instance.write(data);
            assertEquals(data.length(), instance.length());
        }
    }

    @Test
    void testCharAt() {
        String data = "aaa0123456789bbb";
        try (SimpleWriter instance = new SimpleWriter()) {
            instance.write(data);
            assertEquals(data.charAt(3), instance.charAt(3));
        }
    }

    @Test
    void testSubSequence() {
        String data = "aaa0123456789bbb";
        try (SimpleWriter instance = new SimpleWriter()) {
            instance.write(data);
            assertEquals("0123456", instance.subSequence(3, 10));
        }
    }

    @Test
    void testToString() {
        String data = "aaa0123456789bbb";
        try (SimpleWriter instance = new SimpleWriter()) {
            instance.write(data);
            assertEquals(data, instance.toString());
        }
    }

    @Test
    void testGetBuilder() {
        String data = "aaa0123456789bbb";
        try (SimpleWriter instance = new SimpleWriter()) {
            instance.write(data);
            StringBuilder result = instance.getBuilder();
            assertEquals(data, result.toString());
        }
    }

    @Test
    void testToCharArray() {
        char[] data = "aaa0123456789bbb".toCharArray();
        try (SimpleWriter instance = new SimpleWriter()) {
            instance.write(data);
            char[] result = instance.toCharArray();
            assertArrayEquals(data, result);
        }
    }

}
