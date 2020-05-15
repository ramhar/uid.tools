/*
 * Copyright 2020 nightfall.
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
package ua.net.uid.utils.concurrent;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import ua.net.uid.utils.Function;

/**
 * !WIP! not for use
 * 
 * @author nightfall
 * @param <V>
 */
public class Promise<V> {
    ////////////////////////////////////////////////////////////////////////////
    public enum State { PENDING, FULFILLED, REJECTED }
    ////////////////////////////////////////////////////////////////////////////
    public interface Resolver<V> {
        void accept(V value);
        void reject(Throwable error);
    }
    public interface Performer<V> {
        void apply(Resolver<V> resolver) throws Throwable;
    }
    ////////////////////////////////////////////////////////////////////////////
    public static <V> Promise<V> resolve(V value) {
        return new Promise<>(value, 0);
    }
    public static <V> Promise<V> reject(Throwable error) {
        return new Promise<>(error, false);
    }
    ////////////////////////////////////////////////////////////////////////////
    /*
    public static <E> Promise<List<E>> all(Promise<? extends E> ... promises) {
        return all(Arrays.asList(promises));
    }
    public static <E> Promise<List<E>> all(Iterable<? extends Promise<? extends E>> promises) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static <E> Promise<List<E>> allSettled(Promise<? extends E> ... promises) {
        return allSettled(Arrays.asList(promises));
    }
    public static <E> Promise<List<E>> allSettled(Iterable<? extends Promise<? extends E>> promises) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static <E> Promise<List<E>> any(Promise<? extends E> ... promises) {
        return any(Arrays.asList(promises));
    }
    public static <E> Promise<List<E>> any(Iterable<? extends Promise<? extends E>> promises) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static <E> Promise<List<E>> race(Promise<? extends E> ... promises) {
        return race(Arrays.asList(promises));
    }
    public static <E> Promise<List<E>> race(Iterable<? extends Promise<? extends E>> promises) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    */
    ////////////////////////////////////////////////////////////////////////////
    private volatile Internal<V> internal;
    ////////////////////////////////////////////////////////////////////////////
    private Promise(V value, int any) { 
        internal = new Fulfilled(value);
    }
    private Promise(Throwable error, boolean any) { 
        internal = new Rejected(error);
    }
    
    public Promise(Callable<? extends V> task) {
        this((resolver) -> {
            try {
                resolver.accept(task.call());
            } catch (Throwable ex) {
                resolver.reject(ex);
            }
        });
    }
    public Promise(Performer<V> performer) {
        internal = new Pending(performer);
        internal.run();
    }
    ////////////////////////////////////////////////////////////////////////////
    public State getState() {
        return internal.getState();
    }
    public V getValue() {
        internal.sync();
        return internal.getValue();
    }
    public Throwable getError() {
        internal.sync();
        return internal.getError();
    }
    public Promise<V> sync() {
        internal.sync();
        return this;
    }
    public <U> Promise<U> then(Function<V, U> callback) {
        return internal.then(callback);
    }
    public <U> Promise<U> then(U value) {
        return internal.then(value);
    }
    public <U> Promise<U> except(Function<Throwable, U> callback) {
        return internal.except(callback);
    }
    public <U> Promise<U> except(U value) {
        return internal.except(value);
    }
    public <U> Promise<U> anyway(Function<Object, U> callback) {
        return internal.anyway(callback);
    }
    public <U> Promise<U> anyway(U value) {
        return internal.anyway(value);
    }
    private void run() {
        internal.run();
    }
    ////////////////////////////////////////////////////////////////////////////
    private interface Internal<V> {
        State getState();
        V getValue();
        Throwable getError();
        void run();
        void sync();
        <U> Promise<U> then(Function<V, U> callback);
        <U> Promise<U> then(U value);
        <U> Promise<U> except(Function<Throwable, U> callback);
        <U> Promise<U> except(U value);
        <U> Promise<U> anyway(Function<Object, U> callback);
        <U> Promise<U> anyway(U value);
    }
    ////////////////////////////////////////////////////////////////////////////
    private final class Fulfilled implements Internal<V> {
        private final V value;
        public Fulfilled(V value) { this.value = value; }
        @Override
        public State getState() { return State.FULFILLED; }
        @Override
        public V getValue() { return value; }
        @Override
        public Throwable getError() { return null; }
        @Override
        public void run() {}
        @Override
        public void sync() {}

        @Override
        public <U> Promise<U> then(Function<V, U> callback) {
            return new Promise<>((resolver) -> {
                try {
                    resolver.accept(callback.call(value));
                } catch (Throwable ex) {
                    resolver.reject(ex);
                }
            });
        }
        @Override
        public <U> Promise<U> then(U value) {
            return Promise.resolve(value);
        }
        @Override
        public <U> Promise<U> except(Function<Throwable, U> callback) {
            return (Promise<U>) Promise.this;
        }
        @Override
        public <U> Promise<U> except(U value) {
            return (Promise<U>) Promise.this;
        }
        @Override
        public <U> Promise<U> anyway(Function<Object, U> callback) {
            return new Promise<>((resolver) -> {
                try {
                    resolver.accept(callback.call(value));
                } catch (Throwable ex) {
                    resolver.reject(ex);
                }
            });
        }
        @Override
        public <U> Promise<U> anyway(U value) {
            return Promise.resolve(value);
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    private final class Rejected implements Internal<V> {
        private final Throwable error;
        public Rejected(Throwable error) { this.error = error; }
        @Override
        public State getState() { return State.REJECTED; }
        @Override
        public V getValue() { return null; }
        @Override
        public Throwable getError() { return error; }
        @Override
        public void run() {}
        @Override
        public void sync() {}

        @Override
        public <U> Promise<U> then(Function<V, U> callback) {
            return (Promise<U>) Promise.this;
        }
        @Override
        public <U> Promise<U> then(U value) {
            return (Promise<U>) Promise.this;
        }
        @Override
        public <U> Promise<U> except(Function<Throwable, U> callback) {
            return new Promise<>((resolver) -> {
                try {
                    resolver.accept(callback.call(error));
                } catch (Throwable ex) {
                    resolver.reject(ex);
                }
            });
        }
        @Override
        public <U> Promise<U> except(U value) {
            return Promise.resolve(value);
        }
        @Override
        public <U> Promise<U> anyway(Function<Object, U> callback) {
            return new Promise<>((resolver) -> {
                try {
                    resolver.accept(callback.call(error));
                } catch (Throwable ex) {
                    resolver.reject(ex);
                }
            });
        }
        @Override
        public <U> Promise<U> anyway(U value) {
            return Promise.resolve(value);
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    private final class Pending implements Internal<V>, Resolver<V> {
        private final ForkJoinTask<?> task;
        private final Queue<Promise> onFulfill = new ConcurrentLinkedQueue<>();
        private final Queue<Promise> onReject = new ConcurrentLinkedQueue<>();
        private Object value = null;
        public Pending(Performer<V> performer) {
            task = ForkJoinTask.adapt(() -> {
                try {
                    performer.apply(Pending.this);
                } catch (Throwable error) {
                    Pending.this.reject(error);
                }
            });
        }
        @Override
        public State getState() { return State.PENDING; }
        @Override
        public V getValue() { throw new UnsupportedOperationException(); }
        @Override
        public Throwable getError() { throw new UnsupportedOperationException(); }
        @Override
        public void run() {
            ForkJoinPool.commonPool().submit(task);
        }
        @Override
        public void sync() { 
            try {
                task.get();
            } catch (InterruptedException | ExecutionException ex) {
                reject(ex);
            }
        }
        @Override
        public <U> Promise<U> then(Function<V, U> callback) {
            Promise<U> promise = new Promise<>((resolver) -> {
                try {
                    resolver.accept(callback.call((V) Pending.this.value));
                } catch (Throwable ex) {
                    resolver.reject(ex);
                }
            });
            onFulfill.add(promise);
            return promise;
        }
        @Override
        public <U> Promise<U> then(U value) {
            Promise<U> promise = Promise.resolve(value);
            onFulfill.add(promise);
            return promise;
        }
        @Override
        public <U> Promise<U> except(Function<Throwable, U> callback) {
            Promise<U> promise = new Promise<>((resolver) -> {
                try {
                    resolver.accept(callback.call((Throwable) Pending.this.value));
                } catch (Throwable ex) {
                    resolver.reject(ex);
                }
            });
            onReject.add(promise);
            return promise;
        }
        @Override
        public <U> Promise<U> except(U value) {
            Promise<U> promise = Promise.resolve(value);
            onReject.add(promise);
            return promise;
        }
        @Override
        public <U> Promise<U> anyway(Function<Object, U> callback) {
            Promise<U> promise = new Promise<>((resolver) -> {
                try {
                    resolver.accept(callback.call(Pending.this.value));
                } catch (Throwable ex) {
                    resolver.reject(ex);
                }
            });
            onFulfill.add(promise);
            onReject.add(promise);
            return promise;
        }
        @Override
        public <U> Promise<U> anyway(U value) {
            Promise<U> promise = Promise.resolve(value);
            onFulfill.add(promise);
            onReject.add(promise);
            return promise;
        }
        @Override
        public void accept(V value) {
            internal = new Fulfilled(value);
            this.value = value;
            if (!onFulfill.isEmpty()) {
                ForkJoinPool.commonPool().execute(() -> {
                    do {
                        onFulfill.peek().run();
                    } while (!onFulfill.isEmpty());
                });
            }
        }
        @Override
        public void reject(Throwable error) {
            internal = new Rejected(error);
            this.value = error;
            if (!onReject.isEmpty()) {
                ForkJoinPool.commonPool().execute(() -> {
                    do {
                        onReject.peek().run();
                    } while (!onReject.isEmpty());
                });
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////
}
