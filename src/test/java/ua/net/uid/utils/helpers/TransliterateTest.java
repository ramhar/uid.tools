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

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransliterateTest {
    @Test
    void testTransliterateFromCharSequenceToStringBuilder() {
        StringBuilder builder = new StringBuilder();
        String test = "Сжатие SSL/TLS было запрещено по умолчанию ©";
        Transliterate.transliterate(builder, test);
        //noinspection SpellCheckingInspection
        assertEquals("Szhatie SSL/TLS bylo zapresheno po umolchaniyu (c)", builder.toString());
    }

    @Test
    void testTransliterateFromCharSequence() {
        StringBuilder builder = new StringBuilder();
        String test = "Сжатие SSL/TLS было запрещено по умолчанию";
        Transliterate.transliterate(builder, test);
        //noinspection SpellCheckingInspection
        assertEquals("Szhatie SSL/TLS bylo zapresheno po umolchaniyu", Transliterate.transliterate(test).toString());
    }
}
