package ua.net.uid.utils.helpers;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CastTest {
    @Test
    void testToBoolean() {
        assertNull(Cast.toBoolean(null, null));
        assertNull(Cast.toBoolean("", null));
        assertNull(Cast.toBoolean("qwe", null));
        assertTrue(Cast.toBoolean(null, true));
        assertTrue(Cast.toBoolean("", true));
        assertTrue(Cast.toBoolean("qwe", true));
        assertFalse(Cast.toBoolean(null, false));
        assertFalse(Cast.toBoolean("", false));
        assertFalse(Cast.toBoolean("qwe", false));
        assertTrue(Cast.toBoolean("true", false));
        assertTrue(Cast.toBoolean("t", false));
        assertTrue(Cast.toBoolean("yes", false));
        assertTrue(Cast.toBoolean("y", false));
        assertTrue(Cast.toBoolean("122", false));
        assertTrue(Cast.toBoolean("+", false));
        assertFalse(Cast.toBoolean("false", true));
        assertFalse(Cast.toBoolean("f", true));
        assertFalse(Cast.toBoolean("no", true));
        assertFalse(Cast.toBoolean("n", true));
        assertFalse(Cast.toBoolean("0000", true));
        assertFalse(Cast.toBoolean("-", true));
    }

    @Test
    void testToByte() {
        assertNull(Cast.toByte(null, null));
        assertNull(Cast.toByte("", null));
        assertNull(Cast.toByte("qwe", null));
        assertEquals((Object) (byte) 33, Cast.toByte(null, (byte) 33));
        assertEquals((Object) (byte) 34, Cast.toByte("", (byte) 34));
        assertEquals((Object) (byte) 35, Cast.toByte("qwe", (byte) 35));
        assertEquals((Object) (byte) 12, Cast.toByte("12", (byte) 36));
        assertEquals((Object) (byte) 0, Cast.toByte("0", (byte) 37));
    }

    @Test
    void testToShort() {
        assertNull(Cast.toShort(null, null));
        assertNull(Cast.toShort("", null));
        assertNull(Cast.toShort("qwe", null));
        assertEquals((Object) (short) 33, Cast.toShort(null, (short) 33));
        assertEquals((Object) (short) 34, Cast.toShort("", (short) 34));
        assertEquals((Object) (short) 35, Cast.toShort("qwe", (short) 35));
        assertEquals((Object) (short) 12, Cast.toShort("12", (short) 36));
        assertEquals((Object) (short) 0, Cast.toShort("0", (short) 37));
    }

    @Test
    void testToInteger() {
        assertNull(Cast.toInteger(null, null));
        assertNull(Cast.toInteger("", null));
        assertNull(Cast.toInteger("qwe", null));
        assertEquals((Object) (int) 33, Cast.toInteger(null, 33));
        assertEquals((Object) (int) 34, Cast.toInteger("", 34));
        assertEquals((Object) (int) 35, Cast.toInteger("qwe", 35));
        assertEquals((Object) (int) 12, Cast.toInteger("12", 36));
        assertEquals((Object) (int) 0, Cast.toInteger("0", 37));
    }

    @Test
    void testToLong() {
        assertNull(Cast.toLong(null, null));
        assertNull(Cast.toLong("", null));
        assertNull(Cast.toLong("qwe", null));
        assertEquals((Object) (long) 33, Cast.toLong(null, (long) 33));
        assertEquals((Object) (long) 34, Cast.toLong("", (long) 34));
        assertEquals((Object) (long) 35, Cast.toLong("qwe", (long) 35));
        assertEquals((Object) (long) 12, Cast.toLong("12", (long) 36));
        assertEquals((Object) (long) 0, Cast.toLong("0", (long) 37));
    }

    @Test
    void testToFloat() {
        assertNull(Cast.toFloat(null, null));
        assertNull(Cast.toFloat("", null));
        assertNull(Cast.toFloat("qwe", null));
        assertEquals((Object) (float) 33.1, Cast.toFloat(null, (float) 33.1));
        assertEquals((Object) (float) 34.1, Cast.toFloat("", (float) 34.1));
        assertEquals((Object) (float) 35.1, Cast.toFloat("qwe", (float) 35.1));
        assertEquals((Object) (float) 12.2, Cast.toFloat("12.2", (float) 36.1));
        assertEquals((Object) (float) 0.2, Cast.toFloat("0.2", (float) 37.1));
        assertEquals((Object) (float) 0.0, Cast.toFloat("0.0", (float) 38.1));
    }

    @Test
    void testToDouble() {
        assertNull(Cast.toDouble(null, null));
        assertNull(Cast.toDouble("", null));
        assertNull(Cast.toDouble("qwe", null));
        assertEquals((Object) (double) 33.1, Cast.toDouble(null, 33.1));
        assertEquals((Object) (double) 34.1, Cast.toDouble("", 34.1));
        assertEquals((Object) (double) 35.1, Cast.toDouble("qwe", 35.1));
        assertEquals((Object) (double) 12.2, Cast.toDouble("12.2", 36.1));
        assertEquals((Object) (double) 0.2, Cast.toDouble("0.2", 37.1));
        assertEquals((Object) (double) 0.0, Cast.toDouble("0.0", 38.1));
    }

    @Test
    void testToBigInteger() {
        assertNull(Cast.toBigInteger(null, null));
        assertNull(Cast.toBigInteger("", null));
        assertNull(Cast.toBigInteger("qwe", null));
        assertEquals(BigInteger.ZERO, Cast.toBigInteger(null, BigInteger.ZERO));
        assertEquals(BigInteger.ONE, Cast.toBigInteger("", BigInteger.ONE));
        assertEquals(BigInteger.TEN, Cast.toBigInteger("qwe", BigInteger.TEN));
        assertEquals(BigInteger.valueOf(12), Cast.toBigInteger("12", BigInteger.ZERO));
        assertEquals(BigInteger.ZERO, Cast.toBigInteger("0", BigInteger.TEN));
    }

    @Test
    void testToBigDecimal() {
        assertNull(Cast.toBigDecimal(null, null));
        assertNull(Cast.toBigDecimal("", null));
        assertNull(Cast.toBigDecimal("qwe", null));
        assertEquals(BigDecimal.valueOf(33.1), Cast.toBigDecimal(null, BigDecimal.valueOf(33.1)));
        assertEquals(BigDecimal.valueOf(34.1), Cast.toBigDecimal("", BigDecimal.valueOf(34.1)));
        assertEquals(BigDecimal.valueOf(35.1), Cast.toBigDecimal("qwe", BigDecimal.valueOf(35.1)));
        assertEquals(BigDecimal.valueOf(12.2), Cast.toBigDecimal("12.2", BigDecimal.valueOf(36.1)));
        assertEquals(BigDecimal.valueOf(0.2), Cast.toBigDecimal("0.2", BigDecimal.valueOf(37.1)));
        assertEquals(BigDecimal.valueOf(0.0), Cast.toBigDecimal("0.0", BigDecimal.valueOf(38.1)));
    }

    @Test
    void testToEmail() {
        assertNull(Cast.toEmail(null, null));
        assertNull(Cast.toEmail("", null));
        assertNull(Cast.toEmail("qwe", null));
        assertEquals("default", Cast.toEmail(null, "default"));
        assertEquals("default", Cast.toEmail("", "default"));
        assertEquals("default", Cast.toEmail("qwe", "default"));
        assertEquals("mail@test.net", Cast.toEmail(" mail@test.net ", "default"));
    }

    @Test
    void testToDate() {
        DateFormat format = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z", Locale.US);
        assertNull(Cast.toDate(format, "Wed, Jul 4, '01", null));
        assertEquals(994273736000L, Cast.toDate(format, "2001.07.04 AD at 12:08:56 PDT", null).getTime());
    }

    @Test
    void testToEnum() {
        assertNull(Cast.toEnum(TestEnum.class, null, null));
        assertNull(Cast.toEnum(TestEnum.class, "", null));
        assertNull(Cast.toEnum(TestEnum.class, "qwe", null));
        assertEquals(TestEnum.three, Cast.toEnum(TestEnum.class, null, TestEnum.three));
        assertEquals(TestEnum.two, Cast.toEnum(TestEnum.class, "", TestEnum.two));
        assertEquals(TestEnum.one, Cast.toEnum(TestEnum.class, "qwe", TestEnum.one));
        assertEquals(TestEnum.zero, Cast.toEnum(TestEnum.class, "zero", TestEnum.one));
    }

    @Test
    void testToEnumIgnoreCase() {
        assertNull(Cast.toEnumIgnoreCase(TestEnum.class, null, null));
        assertNull(Cast.toEnumIgnoreCase(TestEnum.class, "", null));
        assertNull(Cast.toEnumIgnoreCase(TestEnum.class, "qwe", null));
        assertEquals(TestEnum.three, Cast.toEnumIgnoreCase(TestEnum.class, null, TestEnum.three));
        assertEquals(TestEnum.two, Cast.toEnumIgnoreCase(TestEnum.class, "", TestEnum.two));
        assertEquals(TestEnum.one, Cast.toEnumIgnoreCase(TestEnum.class, "qwe", TestEnum.one));
        assertEquals(TestEnum.zero, Cast.toEnumIgnoreCase(TestEnum.class, "zero", TestEnum.one));
        assertEquals(TestEnum.one, Cast.toEnumIgnoreCase(TestEnum.class, "One", TestEnum.zero));
        assertEquals(TestEnum.two, Cast.toEnumIgnoreCase(TestEnum.class, "tWo", TestEnum.zero));
        assertEquals(TestEnum.three, Cast.toEnumIgnoreCase(TestEnum.class, "THREE", TestEnum.zero));
    }

    enum TestEnum {zero, one, two, three}
}
