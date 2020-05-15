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

import java.text.ParsePosition;

public class NumberHelper {
    private NumberHelper() {
    }

    public static byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; --i) {
            result[i] = (byte) (l & 0xff);
            l >>= 8;
        }
        return result;
    }

    public static long bytesToLong(byte[] b) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }

    public static Number smallest(long value) {
        if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) return (byte)value;
        if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) return (short)value;
        if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) return (int)value;
        return value;
    }

    public static Number parse(CharSequence source, ParsePosition position) {
        int offset = StringHelper.skipWhitespace(source, position.getIndex());
        int length = source.length();
        if (offset < length) {
            char chr = source.charAt(offset);
            if (chr == '0') {
                return nextZeroDigit(source, position, offset + 1, length);
            } else if (chr > '0' && chr <= '9') {
                return nextDigit(chr, source, position, offset + 1, length);
            } else if (chr == '.'){
                return nextDot(source, position, offset + 1, length);
            }
    
        }
        position.setErrorIndex(offset);
        return null;
    }

    public static Number parse(CharSequence source) {
        return parse(source, new ParsePosition(0));
    }
    
    public static byte min(byte ... items) {
        if (items == null || items.length == 0)
            throw new IllegalArgumentException("the arguments array must not be null or empty");
        byte first = items[0];
        if (first > Byte.MIN_VALUE) {
            final int length = items.length;
            for(int i = 1; i < length; ++i) {
                final byte other = items[i];
                if (other == Byte.MIN_VALUE) return other;
                if (other < first) first = other;
            }
        }
        return first;
    }
    
    public static byte max(byte ... items) {
        if (items == null || items.length == 0)
            throw new IllegalArgumentException("the arguments array must not be null or empty");
        byte first = items[0];
        if (first < Byte.MAX_VALUE) {
            final int length = items.length;
            for(int i = 1; i < length; ++i) {
                final byte other = items[i];
                if (other == Byte.MAX_VALUE) return other;
                if (other > first) first = other;
            }
        }
        return first;
    }
    
    public static short min(short ... items) {
        if (items == null || items.length == 0)
            throw new IllegalArgumentException("the arguments array must not be null or empty");
        short first = items[0];
        if (first > Short.MIN_VALUE) {
            final int length = items.length;
            for(int i = 1; i < length; ++i) {
                final short other = items[i];
                if (other == Short.MIN_VALUE) return other;
                if (other < first) first = other;
            }
        }
        return first;
    }
        
    public static short max(short ... items) {
        if (items == null || items.length == 0)
            throw new IllegalArgumentException("the arguments array must not be null or empty");
        short first = items[0];
        if (first < Short.MAX_VALUE) {
            final int length = items.length;
            for(int i = 1; i < length; ++i) {
                final short other = items[i];
                if (other == Short.MAX_VALUE) return other;
                if (other > first) first = other;
            }
        }
        return first;
    }

    public static int min(int ... items) {
        if (items == null || items.length == 0)
            throw new IllegalArgumentException("the arguments array must not be null or empty");
        int first = items[0];
        if (first > Integer.MIN_VALUE) {
            final int length = items.length;
            for(int i = 1; i < length; ++i) {
                final int other = items[i];
                if (other == Integer.MIN_VALUE) return other;
                if (other < first) first = other;
            }
        }
        return first;
    }

    public static int max(int ... items) {
        if (items == null || items.length == 0)
            throw new IllegalArgumentException("the arguments array must not be null or empty");
        int first = items[0];
        if (first < Integer.MAX_VALUE) {
            final int length = items.length;
            for(int i = 1; i < length; ++i) {
                final int other = items[i];
                if (other == Integer.MAX_VALUE) return other;
                if (other > first) first = other;
            }
        }
        return first;
    }

    public static long min(long ... items) {
        if (items == null || items.length == 0)
            throw new IllegalArgumentException("the arguments array must not be null or empty");
        long first = items[0];
        if (first > Long.MIN_VALUE) {
            final int length = items.length;
            for(int i = 1; i < length; ++i) {
                final long other = items[i];
                if (other == Long.MIN_VALUE) return other;
                if (other < first) first = other;
            }
        }
        return first;
    }

    public static long max(long ... items) {
        if (items == null || items.length == 0)
            throw new IllegalArgumentException("the arguments array must not be null or empty");
        long first = items[0];
        if (first < Long.MAX_VALUE) {
            final int length = items.length;
            for(int i = 1; i < length; ++i) {
                final long other = items[i];
                if (other == Long.MAX_VALUE) return other;
                if (other > first) first = other;
            }
        }
        return first;
    }
    
    public static float min(float ... items) {
        if (items == null || items.length == 0)
            throw new IllegalArgumentException("the arguments array must not be null or empty");
        float first = items[0];
        if (first == first && Float.NEGATIVE_INFINITY != first) {
            final int length = items.length;
            for(int i = 1; i < length; ++i) {
                final float other = items[i];
                if (other != other || Float.NEGATIVE_INFINITY == other) return other;
                if (other < first) first = other;
            }
        }
        return first;
    }

    public static float max(float ... items) {
        if (items == null || items.length == 0)
            throw new IllegalArgumentException("the arguments array must not be null or empty");
        final int length = items.length;
        int i = 1;
        float first = items[0];
        while (first != first && i < length) first = items[++i]; // first not nan
        if (first != Float.POSITIVE_INFINITY) {
            for(; i < length; ++i) {
                final float other = items[i];
                if (other == Float.POSITIVE_INFINITY) return other;
                if (other == other && other > first) first = other;
            }
        }
        return first;
    }

    public static double min(double ... items) {
        if (items == null || items.length == 0)
            throw new IllegalArgumentException("the arguments array must not be null or empty");
        double first = items[0];
        if (first == first && Float.NEGATIVE_INFINITY != first) {
            final int length = items.length;
            for(int i = 1; i < length; ++i) {
                final double other = items[i];
                if (other != other || Double.NEGATIVE_INFINITY == other) return other;
                if (other < first) first = other;
            }
        }
        return first;
    }
    
    public static double max(double ... items) {
        if (items == null || items.length == 0)
            throw new IllegalArgumentException("the arguments array must not be null or empty");
        final int length = items.length;
        int i = 1;
        double first = items[0];
        while (first != first && i < length) first = items[++i];
        if (first != Double.POSITIVE_INFINITY) {
            for(; i < length; ++i) {
                final double other = items[i];
                if (other == Double.POSITIVE_INFINITY) return other;
                if (other == other && other > first) first = other;
            }
        }
        return first;
    }

    @SafeVarargs
    public static <T extends Number & Comparable<? super T>> T min(T ... items) {
        if (items == null || items.length == 0)
        throw new IllegalArgumentException("the arguments array must not be null or empty");
            final int length = items.length;
        T first = items[0];
        for(int i = 1; i < length; ++i) {
            final T other = items[i];
            if (other != null && other.compareTo(first) < 0)
                first = other;
        }
        return first;
    }
    
    @SafeVarargs
    public static <T extends Number & Comparable<? super T>> T max(T ... items) {
        if (items == null || items.length == 0)
        throw new IllegalArgumentException("the arguments array must not be null or empty");
            final int length = items.length;
        T first = items[0];
        for(int i = 1; i < length; ++i) {
            final T other = items[i];
            if (other != null && other.compareTo(first) > 0)
                first = other;
        }
        return first;
    }

    
    ////////////////////////////////////////////////////////////////////////////
    private static Number nextZeroDigit(CharSequence source, ParsePosition position, int end, int length) {
        if (end < source.length()) {
            char chr = source.charAt(end);
            switch (chr) {
                case 'x': case 'X':
                    return nextHexNumeric(source, position, end, length);
                case 'b': case 'B':
                    return nextBinNumeric(source, position, end, length);
                default:
                    long value = 0;
                    while (chr >= '0' && chr <= '7') {
                        value = (value << 3) | (chr - '0');
                        ++end;
                        if (end < length) {
                            chr = source.charAt(end);
                        } else {
                            break;
                        }
                    }
                    position.setIndex(end);
                    return value;
            }
        }
        position.setIndex(end);
        return 0L;
    }

    private static Number nextHexNumeric(CharSequence source, ParsePosition position, int end, int length) {
        boolean real = false;
        long main = 0;
        int base = 0;
        if (++end < length) {
            do {
                char chr = source.charAt(end);
                if (chr >= '0' && chr <= '9') {
                    main = (main << 4) | (chr - '0');
                } else if (chr >= 'A' && chr <= 'F') {
                    main = (main << 4) | (chr - 'A' + 10);
                } else if (chr >= 'a' && chr <= 'f') {
                    main = (main << 4) | (chr - 'a' + 10);
                } else if (!real && chr == '.') {
                    real = true;
                    continue;
                } else {
                    if (chr == 'p' || chr == 'P') {
                        if (++end == length) {
                            position.setErrorIndex(end);
                            return null;
                        }
                        int sig = 1;
                        chr = source.charAt(end);
                        switch (chr) {
                            case '-':
                                sig = -1;
                            case '+':
                                if (++end == length) {
                                    position.setErrorIndex(end);
                                    return null;
                                }
                                chr = source.charAt(end);
                        }
                        if (chr < '0' || chr > '9') {
                            position.setErrorIndex(end);
                            return null;
                        }
                        int pow = chr - '0';
                        while (++end < length) {
                            chr = source.charAt(end);
                            if (chr < '0' || chr > '9') break;
                            pow = (pow * 10) + chr - '0';
                        }
                        base += (sig * pow);
                        real = true;
                    }
                    break;
                }
                if (real) base -= 4;
            } while (++end < length);
            position.setIndex(end);
            if (real) {
                if (main > 0 && (main & -2L) == main) {
                    do {
                        main >>>= 1;
                        ++base;
                    } while ((main & -2L) == main);
                }
                return (double)main * Math.pow(2, base);
            } else {
                return main;
            }
        }
        position.setErrorIndex(end);
        return null;
    }
    
    private static Number nextBinNumeric(CharSequence source, ParsePosition position, int end, int length) {
        if (++end < length) {
            char chr = source.charAt(end++);
            if (chr == '0' || chr == '1') {
                long value = chr - '0';
                while (end < length) {
                    chr = source.charAt(end);
                    if (chr == '0' || chr == '1') {
                        value = (value << 1) | (chr - '0');
                    } else {
                        break;
                    }
                    ++end;
                }
                position.setIndex(end);
                return value;
            }
        }
        position.setErrorIndex(end);
        return null;
    }

    private static Number nextDigit(char chr, CharSequence source, ParsePosition position, int end, int length) {
        long main = chr - '0';
        boolean real = false;
        int base = 0;
        while (end < length) {
            chr = source.charAt(end);
            if (chr >= '0' && chr <= '9') {
                main = (main * 10) + (chr - '0');
                ++end;
                if (real) --base;
            } else if (!real && chr == '.') {
                real = true;
                ++end;
            } else if (chr == 'e' || chr == 'E') {
                return nextDecExp(main, base, source, position, end, length);
            } else {
                break;
            }
        }
        position.setIndex(end);
        if (real) {
            return (double)main * Math.pow(10, base);
        } else {
            return main;
        }
    }

    private static Number nextDecExp(long main, int base, CharSequence source, ParsePosition position, int end, int length) {
        int sig = 1;
        if (++end == length) {
            position.setErrorIndex(end);
            return null;
        }
        char chr = source.charAt(end);
        switch (chr) {
            case '-':
                sig = -1;
            case '+':
                if (++end == length) {
                    position.setErrorIndex(end);
                    return null;
                }
                chr = source.charAt(end);
        }
        if (chr >= '0' && chr <= '9') {
            int pow = chr - '0';
            while (++end < length) {
                chr = source.charAt(end);
                if (chr >= '0' && chr <= '9') {
                    pow = (pow * 10) + (chr - '0');
                } else {
                    break;
                }
            }
            position.setIndex(end);
            return (double)main * Math.pow(10, (sig * pow) + base);
        } else {
            ++end;
            position.setErrorIndex(end);
            return null;
        }
    }    

    private static Number nextDot(CharSequence source, ParsePosition position, int end, int length) {
        if (end < length) {
            char chr = source.charAt(end);
            if (chr >= '0' && chr <= '9') {
                long main = chr - '0';
                int base = -1;
                while (++end < length) {
                    chr = source.charAt(end);
                    if (chr >= '0' && chr <= '9') {
                        main = (main * 10) + (chr - '0');
                        --base;
                    } else if (chr == 'e' || chr == 'E') {
                        return nextDecExp(main, base, source, position, end, length);
                    } else {
                        break;
                    }
                }
                position.setIndex(end);
                return (double)main * Math.pow(10, base);
            }
        }
        position.setIndex(end);
        return null;
    }

}
