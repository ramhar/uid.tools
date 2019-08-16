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

import ua.net.uid.utils.concurrent.ReadyFuture;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ExpiringCache<K, V> {
    static final int NCPU = Runtime.getRuntime().availableProcessors();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final AtomicInteger count = new AtomicInteger();
    private final ExecutorService executor;
    private Chain<K, V>[] table;

    public ExpiringCache() {
        this(Executors.newCachedThreadPool());
    }

    public ExpiringCache(ExecutorService executor) {
        this.executor = executor;
        table = new Chain[NCPU * 2];
    }

    public Future<V> future(K key) {
        return chain(key).get(System.currentTimeMillis(), key);
    }

    public Future<V> future(K key, Callable<V> callable) {
        return chain(key).future(System.currentTimeMillis(), key, callable, Long.MAX_VALUE);
    }

    public Future<V> future(K key, Callable<V> callable, long ttl) {
        long now;
        return chain(key).future(now = System.currentTimeMillis(), key, callable, now + ttl);
    }

    public Future<V> future(K key, Callable<V> callable, Date expiry) {
        return chain(key).future(System.currentTimeMillis(), key, callable, expiry.getTime());
    }

    public V get(K key) {
        Future<V> future = future(key);
        return future == null ? null : value(future);
    }

    public V get(K key, Callable<V> callable) {
        return value(future(key, callable));
    }

    public V get(K key, Callable<V> callable, long ttl) {
        return value(future(key, callable, ttl));
    }

    public V get(K key, Callable<V> callable, Date expiry) {
        return value(future(key, callable, expiry));
    }

    public void set(K key, V value) {
        chain(key).set(System.currentTimeMillis(), key, value, Long.MAX_VALUE);
    }

    public void set(K key, V value, long ttl) {
        long now;
        chain(key).set(now = System.currentTimeMillis(), key, value, now + ttl);
    }

    public void set(K key, V value, Date expiry) {
        chain(key).set(System.currentTimeMillis(), key, value, expiry.getTime());
    }

    public void put(K key, Callable<V> callable) {
        chain(key).put(System.currentTimeMillis(), key, callable, Long.MAX_VALUE);
    }

    public void put(K key, Callable<V> callable, long ttl) {
        long now;
        chain(key).put(now = System.currentTimeMillis(), key, callable, now + ttl);
    }

    public void put(K key, Callable<V> callable, Date expiry) {
        chain(key).put(System.currentTimeMillis(), key, callable, expiry.getTime());
    }

    public Future<V> extractFuture(K key) {
        return chain(key).extract(System.currentTimeMillis(), key);
    }

    public V extract(K key) {
        Future<V> future = extractFuture(key);
        return future == null ? null : value(future);
    }

    public void remove(K key) {
        chain(key).remove(System.currentTimeMillis(), key);
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            for (Chain<K, V> chain : table) {
                chain.clear();
            }
            //count.set(0);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int count() {
        return count.get();
    }

    public void gc() {
        lock.readLock().lock();
        try {
            long now = System.currentTimeMillis();
            for (Chain<K, V> chain : table) {
                chain.gc(now);
            }
        } finally {
            lock.readLock().unlock();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private Chain<K, V> chain(K key) {
        int hash = key.hashCode();
        lock.readLock().lock();
        try {
            return table[hash % table.length];
        } finally {
            lock.readLock().unlock();
        }
    }

    private V value(Future<V> future) {
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static abstract class Item<K, V> {
        Entry<K, V> next;
        Item(Entry<K, V> next) { this.next = next; }
    }

    private final static class Entry<K, V> extends Item<K, V> {
        final K key;
        Future<V> future;
        long expiry;

        private Entry(K key, Future<V> future, long expiry, Entry<K, V> next) {
            super(next);
            this.key = key;
            this.future = future;
            this.expiry = expiry;
        }
    }

    private final static class Chain<K, V> extends Item<K, V> {
        private final ExpiringCache<K, V> cache;
        private int count = 0;

        Chain(ExpiringCache<K, V> cache, Entry<K, V> next) {
            super(next);
            this.cache = cache;
        }

        synchronized Future<V> get(long now, K key) {
            Item<K, V> item = this;
            int cnt = 0;
            while (next(now, item)) {
                if (Objects.equals(key, item.next.key)) {
                    return item.next.future;
                }
                ++cnt;
                item = item.next;
            }
            count(cnt);
            return null;
        }

        synchronized Future<V> future(long now, K key, Callable<V> callable, long expiry) {
            Item<K, V> item = this, last = this;
            int cnt = 0;
            while (next(now, item)) {
                if (Objects.equals(key, item.next.key)) {
                    return item.next.future;
                }
                ++cnt;
                if (item.next.expiry > expiry) {
                    last = item.next;
                }
                item = item.next;
            }
            last.next = new Entry<>(key, cache.executor.submit(callable), expiry, last.next);
            count(cnt + 1);
            return last.next.future;
        }

        synchronized void set(long now, K key, V value, long expiry) {
            Item<K, V> item = this, last = this;
            int cnt = 0;
            while (next(now, item)) {
                if (Objects.equals(key, item.next.key)) {
                    item.next = item.next.next;
                    continue;
                }
                ++cnt;
                if (item.next.expiry > expiry) {
                    last = item.next;
                }
                item = item.next;
            }
            last.next = new Entry<>(key, new ReadyFuture<>(value), expiry, last.next);
            count(cnt + 1);
        }

        synchronized void put(long now, K key, Callable<V> callable, long expiry) {
            Item<K, V> item = this, last = this;
            int cnt = 0;
            while (next(now, item)) {
                if (Objects.equals(key, item.next.key)) {
                    item.next = item.next.next;
                    continue;
                }
                ++cnt;
                if (item.next.expiry > expiry) {
                    last = item.next;
                }
                item = item.next;
            }
            last.next = new Entry<>(key, cache.executor.submit(callable), expiry, last.next);
            count(cnt + 1);
        }

        synchronized Future<V> extract(long now, K key) {
            Item<K, V> item = this;
            int cnt = 0;
            while (next(now, item)) {
                if (Objects.equals(key, item.next.key)) {
                    try {
                        return item.next.future;
                    } finally {
                        item.next = item.next.next;
                        --count;
                        cache.count.decrementAndGet();
                    }
                }
                ++cnt;
                item = item.next;
            }
            count(cnt);
            return null;
        }

        synchronized void remove(long now, K key) {
            Item<K, V> item = this;
            int cnt = 0;
            while (next(now, item)) {
                if (Objects.equals(key, item.next.key)) {
                    item.next = item.next.next;
                    --count;
                    cache.count.decrementAndGet();
                    return;
                }
                ++cnt;
                item = item.next;
            }
            count(cnt);
        }

        void gc(long now) {
            Item<K, V> item = this;
            int cnt = 0;
            while (next(now, item)) {
                ++cnt;
                item = item.next;
            }
            count(cnt);
        }

        void  clear() {
            cache.count.addAndGet(-count);
            count = 0;
            next = null;
        }

        private boolean next(long now, Item<K, V> current) {
            if (current.next != null) {
                if (current.next.expiry <= now) {
                    current.next = null;
                } else {
                    return true;
                }
            }
            return false;
        }

        private void count(int value) {
            if (count != value) {
                cache.count.addAndGet(value - count);
                count = value;
            }
        }
    }
}
