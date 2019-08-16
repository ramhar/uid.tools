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

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomHelper {
    public static char[] randomChars(int minLength, int maxLength) {
        return randomChars(minLength, maxLength, ThreadLocalRandom.current());
    }

    public static char[] randomChars(int minLength, int maxLength, Random random) {
        assert minLength <= maxLength;
        int length = minLength == maxLength ? minLength : minLength + random.nextInt(maxLength - minLength);
        char[] text = new char[length];
        for (int i = 0; i < length; ++i) {
            text[i] = (char) (random.nextInt(126 - 32) + 32);
        }
        return text;
    }

    public static String randomString(int minLength, int maxLength) {
        return new String(randomChars(minLength, maxLength));
    }

    public static String randomString(int minLength, int maxLength, Random random) {
        return new String(randomChars(minLength, maxLength, random));
    }
}
