package ua.net.uid.utils.helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
