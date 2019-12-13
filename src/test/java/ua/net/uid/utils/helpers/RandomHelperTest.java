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

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomHelperTest {

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
