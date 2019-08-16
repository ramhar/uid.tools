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
package ua.net.uid.utils.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class AbstractFilteredIterator<T> implements Iterator<T> {
    private final Iterator<? extends T> iterator;
    private T next;
    private int flag = 0;

    public AbstractFilteredIterator(Iterator<? extends T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        if (flag != 1) {
            flag = 3;
            while (iterator.hasNext()) {
                T item = iterator.next();
                if (check(item)) {
                    next = item;
                    flag = 1;
                    break;
                }
            }
        }
        return flag != 3;
    }

    protected abstract boolean check(T item);

    @Override
    public T next() {
        if (!hasNext())
            throw new NoSuchElementException();
        flag = 2;
        return next;
    }

    @Override
    public void remove() {
        if (flag != 2)
            throw new IllegalStateException();
        iterator.remove();
    }
}
