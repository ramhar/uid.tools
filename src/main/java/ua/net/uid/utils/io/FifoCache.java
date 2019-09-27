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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Function;

/**
 *
 * @author nightfall
 */
public class FifoCache<K, V> {
    private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();
    private final ConcurrentLinkedDeque<K> fifo = new ConcurrentLinkedDeque<>();
    private final int limit;

    public FifoCache(int limit) {
        this.limit = limit;
    }
    
    public V get(K key, Function<K, V> callback) {
        return cache.computeIfAbsent(key, (k) -> {
            try {
                fifo.addLast(k);
                return callback.apply(k);
            } finally {
                if (fifo.size() > limit)
                    cache.remove(fifo.removeFirst());
            }
        });
    }
}
