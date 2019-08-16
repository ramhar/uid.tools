package ua.net.uid.utils.helpers;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomHelperTest { //TODO RandomHelperTest

    RandomHelperTest() {
    }

    @Test
    void testRandomCharsWithLimitLength() {
        int minLength = 20;
        int maxLength = 50;
        char[] result = RandomHelper.randomChars(minLength, maxLength);
        assertNotNull(result);
        assertTrue(result.length >= minLength);
        assertTrue(result.length <= maxLength);
    }

    @Test
    void testRandomCharsWithLimitLengthAndCustomRandom() {
        int minLength = 20;
        int maxLength = 50;
        Random random = new Random();
        char[] result = RandomHelper.randomChars(minLength, maxLength, random);
        assertNotNull(result);
        assertTrue(result.length >= minLength);
        assertTrue(result.length <= maxLength);
    }

    @Test
    void testRandomStringWithLimitLength() {
        int minLength = 2;
        int maxLength = 5;
        String result = RandomHelper.randomString(minLength, maxLength);
        assertNotNull(result);
        assertTrue(result.length() >= minLength);
        assertTrue(result.length() <= maxLength);
    }

    @Test
    void testRandomStringWithLimitLengthAndCustomRandom() {
        int minLength = 2;
        int maxLength = 5;
        Random random = new Random();
        String result = RandomHelper.randomString(minLength, maxLength, random);
        assertTrue(result.length() >= minLength);
        assertTrue(result.length() <= maxLength);
    }
}
