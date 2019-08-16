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
package ua.net.uid.utils.io;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InternPool<V> {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<V, WeakReference<V>> pool = new WeakHashMap<>();

    public V intern(V item) {
        if (item != null) {
            lock.readLock().lock();
            try {
                WeakReference<V> cached = pool.get(item);
                V result;
                if (cached != null && (result = cached.get()) != null)
                    return result;
            } finally {
                lock.readLock().unlock();
            }
            lock.writeLock().lock();
            try {
                WeakReference<V> cached = pool.get(item);
                V result;
                if (cached != null && (result = cached.get()) != null)
                    return result;
                pool.put(item, new WeakReference<>(item));
            } finally {
                lock.writeLock().unlock();
            }
        }
        return item;
    }

    public void remove(V item) {
        lock.writeLock().lock();
        try {
            pool.remove(item);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            pool.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int size() {
        lock.readLock().lock();
        try {
            return pool.size();
        } finally {
            lock.readLock().unlock();
        }
    }
}
