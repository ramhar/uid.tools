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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommonHelper {
    private CommonHelper() {
    }

    public static void close(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final Exception ignored) {
            }
        }
    }

    public static boolean isEmpty(CharSequence chars) {
        return chars == null || chars.length() == 0;
    }

    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Object object) {
        if (object == null) return true;
        if (object instanceof CharSequence) return ((CharSequence) object).length() == 0;
        if (object.getClass().isArray()) return Array.getLength(object) == 0;
        if (object instanceof Collection<?>) return ((Collection<?>) object).isEmpty();
        if (object instanceof Map<?, ?>) return ((Map<?, ?>) object).isEmpty();
        return false;
    }

    @SafeVarargs
    public static <T> T coalesce(T ... items) {
        if (!isEmpty(items)) {
            for (T item : items)
                if (item != null)
                    return item;
        }
        return null;
    }

    public static <T extends Comparable<? super T>> int compare(T v1, T v2, boolean nullGreater) {
        if (v1 == v2) return 0;
        if (v1 == null) return nullGreater ? 1 : -1;
        if (v2 == null) return nullGreater ? -1 : 1;
        return v1.compareTo(v2);
    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> T min(T ... items) {
        T result = null;
        if (!isEmpty(items)) {
            for (T item : items)
                if (compare(item, result, true) < 0)
                    result = item;
        }
        return result;
    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> T max(T ... items) {
        T result = null;
        if (!isEmpty(items)) {
            for (T item : items)
                if (compare(item, result, false) > 0)
                    result = item;
        }
        return result;
    }

    public <T> T minBy(Comparator<? super T> comparator, T ... items) {
        T result = null;
        if (!isEmpty(items)) {
            for (T item : items)
                if (comparator.compare(result, item) < 0)
                    result = item;
        }
        return result;
    }

    public <T> T maxBy(Comparator<? super T> comparator, T ... items) {
        T result = null;
        if (!isEmpty(items)) {
            for (T item : items)
                if (comparator.compare(result, item) > 0)
                    result = item;
        }
        return result;
    }

    @SafeVarargs
    public static <T> Set<T> setOf(T ... items) {
        if (!isEmpty(items)) {
            Set<T> result = new HashSet<>(items.length);
            Collections.addAll(result, items);
            return result;
        }
        return new HashSet<>();
    }
}
