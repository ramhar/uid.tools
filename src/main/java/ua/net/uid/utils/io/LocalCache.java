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

import java.util.Map;
import java.util.concurrent.*;

public class LocalCache<K, V> {
    private final ExecutorService executor;
    private final Map<K, Future<V>> items = new ConcurrentHashMap<>();

    public LocalCache() {
        this(Executors.newCachedThreadPool());
    }

    public LocalCache(ExecutorService executor) {
        this.executor = executor;
    }

    public Future<V> future(K key) {
        return items.get(key);
    }

    public Future<V> future(K key, Callable<V> callable) {
        return items.computeIfAbsent(key, (K k) -> executor.submit(callable));
    }

    public V get(K key) {
        Future<V> future = future(key);
        return future == null ? null : value(future);
    }

    public V get(K key, Callable<V> callable) {
        return value(future(key, callable));
    }

    public void set(K key, V value) {
        items.put(key, new ReadyFuture<>(value));
    }

    public void put(K key, Callable<V> callable) {
        items.put(key, executor.submit(callable));
    }

    public Future<V> extractFuture(K key) {
        return items.remove(key);
    }

    public V extract(K key) {
        Future<V> future = extractFuture(key);
        return future == null ? null : value(future);
    }

    public void remove(K key) {
        items.remove(key);
    }

    public void clear() {
        items.clear();
    }

    public int count() {
        return items.size();
    }

    private V value(Future<V> future) {
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
