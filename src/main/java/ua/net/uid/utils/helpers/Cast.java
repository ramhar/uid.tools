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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cast {
    private static final Pattern REGEXP_BOOLEAN = Pattern.compile("^\\s*(false|f|no|off|n|0+|-)|(true|t|yes|on|y|\\d+|\\+)\\s*$", Pattern.CASE_INSENSITIVE);
    private static final Pattern REGEXP_EMAIL = Pattern.compile("^\\s*([-a-z0-9~!$%^&*_=+}{'?]+(\\.[-a-z0-9~!$%^&*_=+}{'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*))\\s*$", Pattern.CASE_INSENSITIVE);

    private Cast() {
    }
    
    public static String toString(final Object value, String defaults) {
        return CommonHelper.isEmpty(value) ? defaults : value.toString();
    }

    public static Boolean toBoolean(final String text, Boolean defaults) {
        if (text != null) {
            Matcher matcher = REGEXP_BOOLEAN.matcher(text);
            if (matcher.matches()) return matcher.group(2) != null;
        }
        return defaults;
    }

    public static Byte toByte(final String value, final Byte defaults) {
        if (value != null) {
            try {
                return Byte.decode(value);
            } catch (NumberFormatException ignored) {
            }
        }
        return defaults;
    }

    public static Short toShort(final String value, final Short defaults) {
        if (value != null) {
            try {
                return Short.decode(value);
            } catch (NumberFormatException ignored) {
            }
        }
        return defaults;
    }

    public static Integer toInteger(final String value, final Integer defaults) {
        if (value != null) {
            try {
                return Integer.decode(value);
            } catch (NumberFormatException ignored) {
            }
        }
        return defaults;
    }

    public static Long toLong(final String value, final Long defaults) {
        if (value != null) {
            try {
                return Long.decode(value);
            } catch (NumberFormatException ignored) {
            }
        }
        return defaults;
    }

    public static Float toFloat(final String value, final Float defaults) {
        if (value != null) {
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException ignored) {
            }
        }
        return defaults;
    }

    public static Double toDouble(final String value, final Double defaults) {
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException ignored) {
            }
        }
        return defaults;
    }

    public static BigInteger toBigInteger(String value, BigInteger defaults) {
        if (value != null) {
            try {
                return new BigInteger(value);
            } catch (NumberFormatException ignored) {
            }
        }
        return defaults;
    }

    public static BigDecimal toBigDecimal(String value, BigDecimal defaults) {
        if (value != null) {
            try {
                return new BigDecimal(value);
            } catch (NumberFormatException ignored) {
            }
        }
        return defaults;
    }

    public static String toEmail(String value, final String defaults) {
        if (value != null) {
            Matcher matcher = REGEXP_EMAIL.matcher(value);
            if (matcher.matches()) return matcher.group(1);
        }
        return defaults;
    }

    public static Date toDate(DateFormat format, String value, Date defValue) {
        if (value != null) {
            try {
                return format.parse(value);
            } catch (ParseException ignored) {
            }
        }
        return defValue;
    }

    public static <T extends Enum<T>> T toEnum(Class<T> enumType, String value, T defaults) {
        return EnumHelper.valueOf(enumType, value, defaults);
    }

    public static <T extends Enum<T>> T toEnumIgnoreCase(Class<T> enumType, String value, T defaults) {
        return EnumHelper.valueOfIgnoreCase(enumType, value, defaults);
    }
}
