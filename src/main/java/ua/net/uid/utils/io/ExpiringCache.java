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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class ExpiringCache<K, V> {
    ////////////////////////////////////////////////////////////////////////////
    private final Item<K, V> EMPTY = new Item<>(null, null, 0);
    private final ConcurrentHashMap<K, Item<K, V>> cache;
    private final DelayQueue<Item<K, V>> queue = new DelayQueue<>();
    ////////////////////////////////////////////////////////////////////////////
    public ExpiringCache() {
        cache = new ConcurrentHashMap<>();
    }
    
    public ExpiringCache(int initSize) {
        cache = new ConcurrentHashMap<>(initSize);
    }
    ////////////////////////////////////////////////////////////////////////////
    public int getSize() { return cache.size(); }
    
    public V get(K key) {
        return cache.getOrDefault(key, EMPTY).value;
    }

    public V get(K key, Function<K, V> callback, long ttl) {
        return cache.computeIfAbsent(key, (k) -> {
            Item<K, V> item = new Item<>(key, callback.apply(key), now() + ttl);
            queue.put(item);
            return item;
        }).value;
    }
    
    public V get(K key, Function<K, V> callback, Date expiry) {
        return cache.computeIfAbsent(key, (k) -> {
            Item<K, V> item = new Item<>(key, callback.apply(key), expiry.getTime());
            queue.put(item);
            return item;
        }).value;
    }

    public void set(K key, V value, long ttl) {
        Item<K, V> item = new Item<>(key, value, now() + ttl);
        queue.put(item);
        cache.put(item.key, item);
    }

    public void set(K key, V value, Date expiry) {
        Item<K, V> item = new Item<>(key, value, expiry.getTime());
        queue.put(item);
        cache.put(item.key, item);
    }

    public void remove(K key) {
        queue.remove(cache.remove(key));
    }

    public void gc() {
        Item<K, V> item;
        while((item = queue.poll()) != null)
            cache.remove(item.key, item);           
    }
    
    public void runGC() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Item<K, V> item = queue.take();
                cache.remove(item.key, item);                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void clear() {
        cache.clear();
    }
    ////////////////////////////////////////////////////////////////////////////
    private static long now() { return System.currentTimeMillis(); }
    private static int compare(long lv, long rv) { return lv < rv ? -1 : (lv > rv ? 1 : 0); }
    ////////////////////////////////////////////////////////////////////////////
    private static final class Item<K, V> implements Delayed {
        final K key;
        final V value;
        final long expiry;

        public Item(K key, V value, long expiry) {
            this.key = key;
            this.value = value;
            this.expiry = expiry;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(expiry - now(), TimeUnit.MILLISECONDS);
        }

        @Override
        @SuppressWarnings("unchecked")
        public int compareTo(Delayed other) {
            return compare(expiry, ((Item<K, V>)other).expiry);
        }

        @Override
        public int hashCode() {
            return 23 * (23 * (23 * 7 + Objects.hashCode(key)) + Objects.hashCode(value)) + (int) (expiry ^ (expiry >>> 32));
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Item && equals((Item<?, ?>) other);
        }
        
        public boolean equals(Item<?, ?> other) {
            return other != null 
                    && Objects.equals(key, other.key) 
                    && expiry == other.expiry
                    && Objects.equals(value, other.value);
        }
    }
    ////////////////////////////////////////////////////////////////////////////
}
