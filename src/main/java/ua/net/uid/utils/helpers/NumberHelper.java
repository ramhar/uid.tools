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
        if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) return Byte.valueOf((byte)value);
        if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) return Short.valueOf((short)value);
        if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) return Integer.valueOf((int)value);
        return Long.valueOf(value);
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
                    return smallest(value);
            }
        }
        position.setIndex(end);
        return Byte.valueOf((byte)0);
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
                return Double.valueOf((double)main * Math.pow(2, base));
            } else {
                return smallest(main);
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
                return smallest(value);
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
            return Double.valueOf((double)main * Math.pow(10, base));
        } else {
            return Byte.valueOf((byte)0);
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
            return Double.valueOf((double)main * Math.pow(10, (sig * pow) + base));
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
                return Double.valueOf((double)main * Math.pow(10, base));
            }
        }
        position.setIndex(end);
        return null;
    }

}
