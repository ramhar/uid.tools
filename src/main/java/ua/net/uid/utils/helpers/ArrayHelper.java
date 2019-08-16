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

public class ArrayHelper {
    private ArrayHelper() {
    }

    public static <T> int indexOf(final T[] array, final T item) {
        return indexOf(array, item, 0);
    }

    public static <T> int indexOf(final T[] array, final T item, int from) {
        if (array == null) return -1;
        if (from < 0) from = 0;
        final int length = array.length;
        if (from >= length) return -1;
        if (item == null) {
            for (int i = from; i < length; ++i) {
                if (array[i] == null) return i;
            }
        } else {
            for (int i = from; i < length; ++i) {
                if (item.equals(array[i])) return i;
            }
        }
        return -1;
    }

    public static <T> int lastIndexOf(final T[] array, final T item) {
        return lastIndexOf(array, item, Integer.MAX_VALUE);
    }

    public static <T> int lastIndexOf(final T[] array, final T item, int from) {
        if (array == null || from < 0) return -1;
        if (from >= array.length) from = array.length - 1;
        if (item == null) {
            for (int i = from; i >= 0; --i) {
                if (array[i] == null) return i;
            }
        } else {
            for (int i = from; i >= 0; --i) {
                if (item.equals(array[i])) return i;
            }
        }
        return -1;
    }

    /*
    !!! not compiled in java 9
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<? extends T> collection) {
        return (T[]) collection.toArray();
    }

    public static <T> T[] toSortedArray(Collection<T> collection) {
        final T[] result = toArray(collection);
        Arrays.sort(result);
        return result;
    }

    public static <T> T[] toSortedArray(Collection<T> collection, Comparator<? super T> comparator) {
        final T[] result = toArray(collection);
        Arrays.sort(result, comparator);
        return result;
    }
    */
}
