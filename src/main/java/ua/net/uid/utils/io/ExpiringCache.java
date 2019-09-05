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

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

public class ExpiringCache<K, V> {
    ////////////////////////////////////////////////////////////////////////////
    public static final int MAX_TABLE_SIZE = Integer.MAX_VALUE >>> 3;
    public static final int INIT_SIZE = 64;
    public static final float LOAD_FACTOR = 7F/3F;
    ////////////////////////////////////////////////////////////////////////////
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final AtomicInteger count = new AtomicInteger(0);
    private final AtomicInteger modified = new AtomicInteger(0);
    private final float loadFactor;
    private int nextResize;
    private Chain<K, V>[] table;

    
    public ExpiringCache() {
        this(INIT_SIZE, LOAD_FACTOR);
    }
    public ExpiringCache(int initSize) {
        this(initSize, LOAD_FACTOR);
    }
    public ExpiringCache(float loadFactor) {
        this(INIT_SIZE, loadFactor);
    }
    public ExpiringCache(int initSize, float loadFactor) {
        this.loadFactor = loadFactor;
        this.nextResize = (int)(loadFactor * initSize);
        this.table = create(initSize);
    }

    public int getSize() { return count.get(); }
    
    public V get(K key) {
        return get(key, now());
    }
    
    public V get(K key, long now) {
        return chain(key.hashCode()).get(key, now);
    }

    public V get(K key, Function<K, V> callback, long ttl) {
        return get(key, callback, ttl, now());
    }
    
    public V get(K key, Function<K, V> callback, Date expiry) {
        return get(key, callback, expiry, now());
    }

    public V get(K key, Function<K, V> callback, long ttl, long now) {
        return chain(key.hashCode()).get(key, callback, now + ttl, now);
    }
    
    public V get(K key, Function<K, V> callback, Date expiry, long now) {
        return chain(key.hashCode()).get(key, callback, expiry.getTime(), now);
    }
    
    public void set(K key, V value, long ttl) {
        set(key, value, ttl, now());
    }
    
    public void set(K key, V value, Date expiry) {
        set(key, value, expiry, now());
    }

    public void set(K key, V value, long ttl, long now) {
        chain(key.hashCode()).set(key, value, ttl + now, now);
    }
    
    public void set(K key, V value, Date expiry, long now) {
        chain(key.hashCode()).set(key, value, expiry.getTime(), now);
    }
    
    public void remove(K key) {
        remove(key, now());
    }
    
    public void remove(K key, long now) {
        chain(key.hashCode()).remove(key, now);
    }
    
    public V extract(K key) {
        return extract(key, now());
    }
    
    public V extract(K key, long now) {
        return chain(key.hashCode()).extract(key, now);
    }
    
    public void gc() {
        lock.readLock().lock();
        try {
            long now = now();
            for(Chain<K, V> chain: table)
                chain.gc(now);
            modified.set(0);
        } finally {
            lock.readLock().unlock();
        }
    }
    /*
    //TODO public void pack() 
    public void pack() {
        lock.writeLock().lock();
        try {
            throw new UnsupportedOperationException("TODO: Not supported yet.");
        } finally {
            lock.writeLock().unlock();
        }
    }
    */
    public void empty() {
        lock.readLock().lock();
        try {
            for(Chain<K, V> chain: table)
                chain.next = null;
            modified.set(0);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void clear(int initSize) {
        lock.writeLock().lock();
        try {
            table = create(initSize);
            modified.set(0);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void clear() {
        clear(INIT_SIZE);
    }
    ////////////////////////////////////////////////////////////////////////////
    private static long now() { return System.currentTimeMillis(); }

    private Chain<K, V>[] create(int size) {
        Chain<K, V>[] result = (Chain<K, V>[]) new Chain[size];
        for(int i = 0; i < size; ++i)
            result[i] = new Chain<>(this);
        return result;
    }

    private static int index(int hash, int length) {
        return (hash & MAX_TABLE_SIZE) % length;
    }

    private Chain<K, V> chain(int hash) {
        lock.readLock().lock();
        try {
            return table[index(hash, table.length)];
        } finally {
            lock.readLock().unlock();
        }
    }
    
    private void resize(int delta) {
        int newSize = Math.min(MAX_TABLE_SIZE, count.addAndGet(delta));
        if (newSize > nextResize) {
            lock.writeLock().lock();
            try {
                Chain<K, V>[] result = create(newSize);
                int newCount = 0;
                long now = now();
                for(Chain<K, V> src : table) {
                    Entry<K, V> old = src.next;
                    while (old != null && old.expiry > now) {
                        result[index(old.key.hashCode(), newSize)].add(old.key, old.value, old.expiry);
                        ++newCount;
                        old = old.next;
                    }
                    src.next = null;
                }
                table = result;
                count.set(newCount);
                modified.set(0);
                nextResize = Math.min(MAX_TABLE_SIZE, (int)(loadFactor * newSize));
            } finally {
                lock.writeLock().unlock();
            }
        } else {
            if (modified.incrementAndGet() > nextResize)
                gc();
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    private static abstract class Item<K, V> {
        Entry<K, V> next;
        Item(Entry<K, V> next) { this.next = next; }
    }
    ////////////////////////////////////////////////////////////////////////////
    private static final class Entry<K, V> extends Item<K, V> {
        final K key;
        final V value;
        final long expiry;
        Entry(K key, V value, long expiry, Entry<K, V> next) {
            super(next);
            this.expiry = expiry;
            this.key = key;
            this.value = value;
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    private static final class Chain<K, V> extends Item<K, V> {
        final ExpiringCache<K, V> cache;
        int count = 0;
        
        Chain(ExpiringCache<K, V> cache) {
            super(null);
            this.cache = cache;
        }
        
        private void count(int value) {
            if (count != value) {
                int old = count;
                count = value;
                cache.resize(value - old);
            }
        }

        private boolean checkNext(Item<K, V> current, long now) {
            if (current.next != null) {
                if (current.next.expiry <= now) {
                    current.next = null;
                } else {
                    return true;
                }
            }
            return false;
        }

        void add(K key, V value, long expiry) {
            Item<K, V> item = this;
            while (item.next != null && item.next.expiry > expiry) {
                item = item.next;
            }
            item.next = new Entry<>(key, value, expiry, item.next);
            ++count;
        }

        synchronized V get(K key, long now) {
            Item<K, V> item = this;
            int cnt = 0;
            V result = null;
            while (checkNext(item, now)) {
                if (Objects.equals(key, item.next.key))
                    result = item.next.value;
                ++cnt;
                item = item.next;
            }
            count(cnt);
            return result;
        }

        synchronized V get(K key, Function<K, V> callback, long expiry, long now) {
            Item<K, V> item = this, last = this;
            int cnt = 0;
            while (checkNext(item, now)) {
                ++cnt;
                if (Objects.equals(key, item.next.key)) {
                    V value = item.next.value;
                    while (checkNext(item, now)) {
                        ++cnt;
                        item = item.next;
                    }
                    count(cnt);
                    return value;
                }
                if (item.next.expiry > expiry)
                    last = item.next;
                item = item.next;
            }
            V value = callback.apply(key);
            last.next = new Entry<>(key, value, expiry, last.next);
            count(cnt + 1);
            return value;
        }

        synchronized void set(K key, V value, long expiry, long now) {
            Item<K, V> item = this, last = this;
            int cnt = 0;
            while (checkNext(item, now)) {
                if (Objects.equals(key, item.next.key)) {
                    item.next = item.next.next;
                    while (checkNext(item, now)) {
                        ++cnt;
                        if (item.next.expiry > expiry)
                            last = item.next;
                        item = item.next;
                    }
                    break;
                }
                ++cnt;
                if (item.next.expiry > expiry)
                    last = item.next;
                item = item.next;
            }
            last.next = new Entry<>(key, value, expiry, last.next);
            count(cnt + 1);
        }

        synchronized void remove(K key, long now) {
            Item<K, V> item = this;
            int cnt = 0;
            while (checkNext(item, now)) {
                if (Objects.equals(key, item.next.key)) {
                    item.next = item.next.next;
                } else {
                    ++cnt;
                    item = item.next;
                }
            }
            count(cnt);
        }
        
        synchronized V extract(K key, long now) {
            Item<K, V> item = this;
            int cnt = 0;
            V result = null;
            while (checkNext(item, now)) {
                if (Objects.equals(key, item.next.key)) {
                    result = item.next.value;
                    item.next = item.next.next;
                } else {
                    ++cnt;
                    item = item.next;
                }
            }
            count(cnt);
            return result;
        }

        synchronized void gc(long now) {
            Item<K, V> item = this;
            int cnt = 0;
            while (checkNext(item, now)) {
                ++cnt;
                item = item.next;
            }
            count(cnt);
        }
    }
    ////////////////////////////////////////////////////////////////////////////
}
