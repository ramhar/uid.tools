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
package ua.net.uid.utils.comparators;

import java.util.Comparator;

class NullComparator<T> implements Comparator<T>  {
    private final Comparator<? super T> notNullComparator;
    private final boolean nullFirst;

    public NullComparator(Comparator<? super T> notNullComparator, boolean nullFirst) {
        this.notNullComparator = notNullComparator;
        this.nullFirst = nullFirst;
    }
    public NullComparator(Comparator<T> notNullComparator) { this(notNullComparator, true); }

    @Override
    public int compare(T o1, T o2) {
        if (o1 == o2) return 0;
        if (o1 == null) return nullFirst ? -1 : 1;
        if (o2 == null) return nullFirst ? 1 : -1;
        return notNullComparator.compare(o1, o2);
    }
}