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

import java.io.IOException;
import java.util.Iterator;

import ua.net.uid.utils.iterators.ArrayIterator;

public class StringHelper {
    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private StringHelper() {
    }

    public static char[] toHex(byte[] bytes) {
        final char[] buffer = new char[bytes.length * 2];
        for (int i = 0, n = 0; i < bytes.length; ++i) {
            final int val = bytes[i] & 255;
            buffer[n++] = HEX_CHARS[val >>> 4];
            buffer[n++] = HEX_CHARS[val & 15];
        }
        return buffer;
    }

    public static void toHex(final Appendable builder, byte[] bytes) throws IOException {
        for (byte b : bytes) {
            final int val = b & 255;
            builder.append(HEX_CHARS[val >>> 4]);
            builder.append(HEX_CHARS[val & 15]);
        }
    }

    public static void escape(final Appendable builder, final CharSequence string, int start, int end) throws IOException {
        for (int e = start; e < end; ++e) {
            char chr = string.charAt(e);
            switch (chr) {
                case '"': case '\\': break;
                case '\t': chr = 't'; break;
                case '\b': chr = 'b'; break;
                case '\n': chr = 'n'; break;
                case '\r': chr = 'r'; break;
                case '\f': chr = 'f'; break;
                default:
                    if (chr < 32) {
                        if (start < e) builder.append(string, start, e);
                        builder.append("\\u");
                        for (int i = 12; i >= 0; i -= 4)
                            builder.append(HEX_CHARS[(chr >> i) & 15]);
                        start = e + 1;
                    }
                    continue;
            }
            if (start < e) builder.append(string, start, e);
            start = e + 1;
            builder.append('\\').append(chr);
        }
        if (start < end) builder.append(string, start, end);
    }

    public static void escape(final Appendable builder, final CharSequence string) throws IOException {
        escape(builder, string, 0, string.length());
    }

    public static String escape(final CharSequence string) {
        final StringBuilder builder = new StringBuilder(string.length() * 2);
        try {
            escape(builder, string);
        } catch (IOException ignored) {
        }
        return builder.toString();
    }

    public static void unescape(final Appendable builder, final CharSequence string) throws IOException {
        final int length = string.length();
        int start = 0;
        for (int end = 0; end < length; ++end) {
            char chr = string.charAt(end);
            if (chr == '\\') {
                if (start < end) builder.append(string, start, end);
                chr = string.charAt(++end);
                switch (chr) {
                    case '"': case '\\': break;
                    case 't': chr = '\t'; break;
                    case 'b': chr = '\b'; break;
                    case 'n': chr = '\n'; break;
                    case 'r': chr = '\r'; break;
                    case 'f': chr = '\f'; break;
                    case 'u':
                        chr = 0;
                        for (int i = 1; i <= 4; ++i) {
                            final char tmp = string.charAt(end + i);
                            chr <<= 4;
                            if (tmp >= '0' && tmp <= '9') {
                                chr += tmp - '0';
                            } else if (tmp >= 'A' && tmp <= 'F') {
                                chr += tmp - ('A' - 10);
                            } else if (tmp >= 'a' && tmp <= 'f') {
                                chr += tmp - ('a' - 10);
                            } else {
                                throw new NumberFormatException(string.subSequence(end - 1, end + 5).toString());
                            }
                        }
                        end += 4;
                        break;
                    default:
                        continue;
                }
                builder.append(chr);
                start = end + 1;
            }
        }
        if (start < length) builder.append(string, start, length);
    }

    public static String unescape(final CharSequence string) {
        final StringBuilder builder = new StringBuilder(string.length());
        try {
            unescape(builder, string);
        } catch (IOException ignored) {
        }
        return builder.toString();
    }

    public static String trim(String string) {
        return string == null ? null : string.trim();
    }

    public static String ltrim(String string) {
        if (string == null) return null;
        int i = 0, length = string.length();
        while (i < length && Character.isWhitespace(string.charAt(i)))
            ++i;
        return i == 0 ? string : (i < length ? string.substring(i) : "");
    }

    public static String rtrim(String string) {
        if (string == null) return null;
        int i = string.length();
        while (i >= 0 && Character.isWhitespace(string.charAt(i - 1)))
            --i;
        return i <= 0 ? "" : string.substring(0, i);
    }

    public static int skipWhitespace(CharSequence source, int offset) {
        while (offset < source.length() && Character.isWhitespace(source.charAt(offset)))
            ++offset;
        return offset;
    }

    public static void joinTo(Appendable builder, CharSequence separator, Iterator<?> iterator, CharSequence emptyAs, CharSequence nullAs) throws IOException {
        boolean first = true;
        while (iterator.hasNext()) {
            Object item = iterator.next();
            if (item == null) {
                if (nullAs != null) {
                    if (first) first = false; else builder.append(separator);
                    builder.append(nullAs);
                }
            } else if (CommonHelper.isEmpty(item)) {
                if (emptyAs != null) {
                    if (first) first = false; else builder.append(separator);
                    builder.append(emptyAs);
                }
            } else {
                if (first) first = false; else builder.append(separator);
                builder.append(item.toString());
            }
        }
    }

    public static void joinTo(Appendable builder, CharSequence separator, Iterator<?> iterator, CharSequence emptyAs) throws IOException {
        joinTo(builder, separator, iterator, emptyAs, emptyAs);
    }

    public static void joinTo(Appendable builder, CharSequence separator, Iterator<?> iterator) throws IOException {
        joinTo(builder, separator, iterator, "", "");
    }

    public static void joinItemsTo(Appendable builder, CharSequence separator, Object ... items) throws IOException {
        joinTo(builder, separator, new ArrayIterator<>(items), "", "");
    }

    public static CharSequence join(CharSequence separator, Iterator<?> iterator, CharSequence emptyAs, CharSequence nullAs) {
        StringBuilder builder = new StringBuilder();
        try { joinTo(builder, separator, iterator, emptyAs, nullAs); } catch (IOException ignore) {}
        return builder;
    }

    public static CharSequence join(CharSequence separator, Iterator<?> iterator, CharSequence emptyAs) {
        return join(separator, iterator, emptyAs, emptyAs);
    }

    public static CharSequence join(CharSequence separator, Iterator<?> iterator) {
        return join(separator, iterator, "", "");
    }

    public static CharSequence joinItems(CharSequence separator, Object ... items) {
        return join(separator, new ArrayIterator<>(items), "", "");
    }

    public static boolean isAscii(int chr) {
        return ((chr & 0xFFFFFF80) == 0);
    }

    public static boolean isAsciiUpper(int chr) {
        return chr >= 'A' && chr <= 'Z';
    }

    public static boolean isAsciiLower(int chr) {
        return chr >= 'a' && chr <= 'z';
    }

    public static boolean isAsciiDigit(int chr) {
        return chr >= '0' && chr <= '9';
    }

    public static boolean isBlank(CharSequence str) {
        if (!CommonHelper.isEmpty(str)) {
            final int len = str.length();
            for (int i = 0; i < len; ++i) {
                char chr = str.charAt(i);
                if ((Character.isHighSurrogate(chr) && !Character.isWhitespace(Character.toCodePoint(chr, str.charAt(++i)))) || !Character.isWhitespace(chr))
                    return false;
            }
        }
        return true;
    }

    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    public static int[] toCodePoints(String str) {
        if (str == null) return null;
        int length = str.codePointCount(0, str.length());
        int[] result = new int[length];
        for (int i = 0, j = 0; i < length; ++i) {
            int chr = str.codePointAt(j);
            j += Character.charCount(chr);
            result[i] = chr;
        }
        return result;
    }
}
