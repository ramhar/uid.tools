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

public abstract class AbstractTransformIterator<S, T> implements Iterator<T> {
    private final Iterator<? extends S> iterator;

    public AbstractTransformIterator(Iterator<? extends S> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return transform(iterator.next());
    }

    protected abstract T transform(S source);

    @Override
    public void remove() {
        iterator.remove();
    }
}
