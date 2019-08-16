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
