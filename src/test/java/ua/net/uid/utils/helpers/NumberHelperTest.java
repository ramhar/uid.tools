package ua.net.uid.utils.helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParsePosition;

class NumberHelperTest {
    private final long LONG_VALUE = 0x12345678a1b2c3d4L;
    private final byte[] BYTE_ARRAY = {(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0xa1, (byte) 0xb2, (byte) 0xc3, (byte) 0xd4};

    NumberHelperTest() {
    }

    @Test
    void testLongToBytes() {
        assertArrayEquals(BYTE_ARRAY, NumberHelper.longToBytes(LONG_VALUE));
    }

    @Test
    void testBytesToLong() {
        assertEquals(LONG_VALUE, NumberHelper.bytesToLong(BYTE_ARRAY));
    }

    @Test
    void testSmallest() {
        Number val = NumberHelper.smallest(0);
        assertEquals(Byte.class, val.getClass());
        assertEquals(Byte.valueOf((byte)0), val);

        val = NumberHelper.smallest(Byte.MAX_VALUE);
        assertEquals(Byte.class, val.getClass());
        assertEquals(Byte.valueOf(Byte.MAX_VALUE), val);
        val = NumberHelper.smallest(Byte.MIN_VALUE);
        assertEquals(Byte.class, val.getClass());
        assertEquals(Byte.valueOf(Byte.MIN_VALUE), val);

        val = NumberHelper.smallest(Byte.MAX_VALUE + 1);
        assertEquals(Short.class, val.getClass());
        assertEquals(Short.valueOf((short) (Byte.MAX_VALUE + 1)), val);
        val = NumberHelper.smallest(Byte.MIN_VALUE - 1);
        assertEquals(Short.class, val.getClass());
        assertEquals(Short.valueOf((short) (Byte.MIN_VALUE - 1)), val);

        val = NumberHelper.smallest(Short.MAX_VALUE);
        assertEquals(Short.class, val.getClass());
        assertEquals(Short.valueOf(Short.MAX_VALUE), val);
        val = NumberHelper.smallest(Short.MIN_VALUE);
        assertEquals(Short.class, val.getClass());
        assertEquals(Short.valueOf(Short.MIN_VALUE), val);

        val = NumberHelper.smallest(Short.MAX_VALUE + 1);
        assertEquals(Integer.class, val.getClass());
        assertEquals(Integer.valueOf((int) (Short.MAX_VALUE + 1)), val);
        val = NumberHelper.smallest(Short.MIN_VALUE - 1);
        assertEquals(Integer.class, val.getClass());
        assertEquals(Integer.valueOf((int) (Short.MIN_VALUE - 1)), val);

        val = NumberHelper.smallest(Integer.MAX_VALUE);
        assertEquals(Integer.class, val.getClass());
        assertEquals(Integer.valueOf(Integer.MAX_VALUE), val);
        val = NumberHelper.smallest(Integer.MIN_VALUE);
        assertEquals(Integer.class, val.getClass());
        assertEquals(Integer.valueOf(Integer.MIN_VALUE), val);

        val = NumberHelper.smallest((long)Integer.MAX_VALUE + 1);
        assertEquals(Long.class, val.getClass());
        assertEquals(Long.valueOf((long) Integer.MAX_VALUE + 1), val);
        val = NumberHelper.smallest((long)Integer.MIN_VALUE - 1);
        assertEquals(Long.class, val.getClass());
        assertEquals(Long.valueOf((long) Integer.MIN_VALUE - 1), val);
        
        val = NumberHelper.smallest(Long.MAX_VALUE);
        assertEquals(Long.class, val.getClass());
        assertEquals(Long.valueOf(Long.MAX_VALUE), val);
        val = NumberHelper.smallest(Long.MIN_VALUE);
        assertEquals(Long.class, val.getClass());
        assertEquals(Long.valueOf(Long.MIN_VALUE), val);
    }

    @Test
    void testParseHexDigit() {
        ParsePosition position = new ParsePosition(0);
        String source =
            "0 0x0 0x7a 0x1f9c 0x00000ffffff 0x10000ffffff 0x0. 0x7.a 0x1f.9c 0x00000.ffffff 0x10000.ffffff "+
            "0x0p0 0x1000P+256 0x1000P-256 0x1.fffffffffffffp1023 0x0.0000000000001P-1022 0x1.0P-1074 0x1.8p+1";
        
        assertEquals((byte)0, NumberHelper.parse(source, position));
        assertEquals(1, position.getIndex());

        assertEquals((byte)0, NumberHelper.parse(source, position));
        assertEquals(5, position.getIndex());

        assertEquals((byte)0x7a, NumberHelper.parse(source, position));
        assertEquals(10, position.getIndex());

        assertEquals((short)0x1f9c, NumberHelper.parse(source, position));
        assertEquals(17, position.getIndex());

        assertEquals(0xffffff, NumberHelper.parse(source, position));
        assertEquals(31, position.getIndex());

        assertEquals(0x10000ffffffL, NumberHelper.parse(source, position));
        assertEquals(45, position.getIndex());

        assertEquals(0., NumberHelper.parse(source, position));
        assertEquals(50, position.getIndex());

        assertEquals(0x7.ap0, NumberHelper.parse(source, position));
        assertEquals(56, position.getIndex());

        assertEquals(0x1f.9cp0, NumberHelper.parse(source, position));
        assertEquals(64, position.getIndex());

        assertEquals(0x0.ffffffp0, NumberHelper.parse(source, position));
        assertEquals(79, position.getIndex());
        
        assertEquals(0x10000.ffffffp0, NumberHelper.parse(source, position));
        assertEquals(94, position.getIndex());

        assertEquals(0x0p0, NumberHelper.parse(source, position));
        assertEquals(100, position.getIndex());

        assertEquals(0x1000p256, NumberHelper.parse(source, position));
        assertEquals(112, position.getIndex());

        assertEquals(0x1000p-256, NumberHelper.parse(source, position));
        assertEquals(124, position.getIndex());

        assertEquals(0x1.fffffffffffffp1023, NumberHelper.parse(source, position));
        assertEquals(147, position.getIndex());

        assertEquals(0x0.0000000000001P-1022, NumberHelper.parse(source, position));
        assertEquals(171, position.getIndex());

        assertEquals(0x1P-1074, NumberHelper.parse(source, position));
        assertEquals(183, position.getIndex());

        assertEquals(3., NumberHelper.parse(source, position));
        assertEquals(192, position.getIndex());

        assertNull(NumberHelper.parse(source, position));
        assertEquals(192, position.getIndex());
        assertEquals(192, position.getErrorIndex());
    }

    @Test
    public void testParseOctDigits() {
        ParsePosition position = new ParsePosition(0);
        String source = "0 01 077 06666 01234567 0123456701234567";

        assertEquals((byte)0, NumberHelper.parse(source, position));
        assertEquals(1, position.getIndex());

        assertEquals((byte)01, NumberHelper.parse(source, position));
        assertEquals(4, position.getIndex());
        
        assertEquals((byte)077, NumberHelper.parse(source, position));
        assertEquals(8, position.getIndex());

        assertEquals((short)0_66_66, NumberHelper.parse(source, position));
        assertEquals(14, position.getIndex());

        assertEquals(01234567, NumberHelper.parse(source, position));
        assertEquals(23, position.getIndex());

        assertEquals(0123456701234567L, NumberHelper.parse(source, position));
        assertEquals(40, position.getIndex());

        assertNull(NumberHelper.parse(source, position));
        assertEquals(40, position.getIndex());
        assertEquals(40, position.getErrorIndex());
    }
    @Test
    public void testParseBinDigits() {
        ParsePosition position = new ParsePosition(0);
        String source = "0b0 0b1 0b11111111 0b1111111111111111111111111111111111111111111111111111111111111111 0ba";
        
        assertEquals((byte)0, NumberHelper.parse(source, position));
        assertEquals(3, position.getIndex());

        assertEquals((byte)1, NumberHelper.parse(source, position));
        assertEquals(7, position.getIndex());

        assertEquals((short)0b11111111, NumberHelper.parse(source, position));
        assertEquals(18, position.getIndex());

        assertEquals((byte)-1, NumberHelper.parse(source, position));
        assertEquals(85, position.getIndex());

        assertNull(NumberHelper.parse(source, position));
        assertEquals(85, position.getIndex());
        assertEquals(89, position.getErrorIndex());
    }


    @Test
    public void testParseDigits() {
        ParsePosition position = new ParsePosition(0);
        String source = "1 333 70000 465729124123444 .01 77. 3.333 .1e-3 222.e76 123.456e-78 888e+8 ";

        assertEquals((byte)1, NumberHelper.parse(source, position));
        assertEquals(1, position.getIndex());

        assertEquals((short)333, NumberHelper.parse(source, position));
        assertEquals(5, position.getIndex());

        assertEquals(70000, NumberHelper.parse(source, position));
        assertEquals(11, position.getIndex());

        assertEquals(465729124123444l, NumberHelper.parse(source, position));
        assertEquals(27, position.getIndex());

        assertEquals(.01, NumberHelper.parse(source, position));
        assertEquals(31, position.getIndex());

        assertEquals(77., NumberHelper.parse(source, position));
        assertEquals(35, position.getIndex());

        assertEquals(3.333, NumberHelper.parse(source, position));
        assertEquals(41, position.getIndex());

        assertEquals(.1e-3, NumberHelper.parse(source, position));
        assertEquals(47, position.getIndex());

        assertEquals(222.e76, NumberHelper.parse(source, position));
        assertEquals(55, position.getIndex());

        assertEquals(123.456e-78, NumberHelper.parse(source, position));
        assertEquals(67, position.getIndex());

        assertEquals(888e+8, NumberHelper.parse(source, position));
        assertEquals(74, position.getIndex());

        assertNull(NumberHelper.parse(source, position));
        assertEquals(74, position.getIndex());
        assertEquals(75, position.getErrorIndex());
    }

    @Test
    void testMinOfBytes() {
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.min((byte[])null));
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.min(new byte[0]));
        assertEquals((byte)-55, NumberHelper.min(new byte[] {1,85,-4,6,3,-55}) );
    }

    @Test
    void testMaxOfBytes() {
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.max((byte[])null));
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.max(new byte[0]));
        assertEquals((byte)85, NumberHelper.max(new byte[] {1,85,-4,6,3,-55}) );
    }

    @Test
    void testMinOfShorts() {
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.min((short[])null));
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.min(new short[0]));
        assertEquals((short)-55, NumberHelper.min(new short[] {1,85,-4,6,3,-55}) );
    }

    @Test
    void testMaxOfShorts() {
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.max((short[])null));
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.max(new short[0]));
        assertEquals((short)85, NumberHelper.max(new short[] {1,85,-4,6,3,-55}) );
    }

    @Test
    void testMinOfInts() {
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.min((int[])null));
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.min(new int[0]));
        assertEquals((int)-55, NumberHelper.min(new int[] {1,85,-4,6,3,-55}) );
    }

    @Test
    void testMaxOfInts() {
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.max((int[])null));
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.max(new int[0]));
        assertEquals((int)85, NumberHelper.max(new int[] {1,85,-4,6,3,-55}) );
    }

    @Test
    void testMinOfLongs() {
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.min((long[])null));
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.min(new long[0]));
        assertEquals((long)-55, NumberHelper.min(new long[] {1,85,-4,6,3,-55}) );
    }

    @Test
    void testMaxOfLongs() {
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.max((long[])null));
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.max(new long[0]));
        assertEquals((long)85, NumberHelper.max(new long[] {1,85,-4,6,3,-55}) );
    }

    @Test
    void testMinOfFloats() {
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.min((float[])null));
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.min(new float[0]));
        assertEquals(-55.1f, NumberHelper.min(new float[] {1.1f,85.2f,-4.3f,6.3f,3.2f,-55.1f}) );
    }

    @Test
    void testMaxOfFloats() {
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.max((float[])null));
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.max(new float[0]));
        assertEquals(85.2f, NumberHelper.max(new float[] {1.1f,85.2f,-4.3f,6.3f,3.2f,-55.1f}) );
    }

    @Test
    void testMinOfDoubles() {
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.min((double[])null));
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.min(new double[0]));
        assertEquals(-55.1, NumberHelper.min(new double[] {1.1,85.2,-4.3,6.3,3.2,-55.1}) );
    }

    @Test
    void testMaxOfDoubles() {
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.max((double[])null));
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.max(new double[0]));
        assertEquals(85.2, NumberHelper.max(new double[] {1.1,85.2,-4.3,6.3,3.2,-55.1}) );
    }

    @Test
    void testMinOfNumbers() {
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.min((Double[])null));
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.min(new Double[0]));
        assertEquals(-55.1, NumberHelper.min(new Double[] {1.1,85.2,-4.3,6.3,3.2,-55.1}) );
    }

    @Test
    void testMaxOfNumbers() {
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.max((Double[])null));
        assertThrows(IllegalArgumentException.class, () -> NumberHelper.max(new Double[0]));
        assertEquals(85.2, NumberHelper.max(new Double[] {1.1,85.2,-4.3,6.3,3.2,-55.1}) );
    }
}
