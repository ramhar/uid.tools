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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnumHelper {
    private EnumHelper() {
    }

    public static <T extends Enum<T>> T valueOf(Class<T> enumType, String value, T defValue) {
        if (value == null) return defValue;
        try {
            return Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException ex) {
            return defValue;
        }
    }

    public static <T extends Enum<T>> T valueOfIgnoreCase(Class<T> enumType, String value, T defValue) {
        if (value == null) return defValue;
        for (T item : enumType.getEnumConstants())
            if (item.name().equalsIgnoreCase(value))
                return item;
        return defValue;
    }

    public static <T extends Enum<T>> T valueOf(Class<T> enumType, String value) {
        return valueOf(enumType, value, null);
    }

    public static <T extends Enum<T>> T valueOfIgnoreCase(Class<T> enumType, String value) {
        return valueOfIgnoreCase(enumType, value, null);
    }

    public static <T extends Enum<T>> List<T> toList(final Class<T> enumType) {
        return Arrays.asList(enumType.getEnumConstants());
    }

    public static <T extends Enum<T>> Map<String, T> toMap(final Class<T> enumType) {
        Map<String, T> map = new LinkedHashMap<>();
        for (T e : enumType.getEnumConstants())
            map.put(e.name(), e);
        return map;
    }
}
