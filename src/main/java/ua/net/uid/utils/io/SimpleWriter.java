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

import java.io.Writer;

public class SimpleWriter extends Writer implements CharSequence {
    private final StringBuilder builder;

    public SimpleWriter() {
        this(new StringBuilder());
    }

    public SimpleWriter(int initCapacity) {
        this(new StringBuilder(initCapacity));
    }

    public SimpleWriter(StringBuilder builder) {
        this.builder = builder;
        this.lock = this.builder;
    }

    @Override
    public void write(int c) {
        builder.append((char) c);
    }

    @Override
    public void write(char[] buf) {
        builder.append(buf);
    }

    @Override
    public void write(char[] buf, int off, int len) {
        builder.append(buf, off, len);
    }

    @Override
    public void write(String str) {
        builder.append(str);
    }

    @Override
    public void write(String str, int off, int len) {
        builder.append(str, off, off + len);
    }

    @Override
    public SimpleWriter append(CharSequence csq) {
        builder.append(csq);
        return this;
    }

    @Override
    public SimpleWriter append(CharSequence csq, int start, int end) {
        builder.append(csq, start, end);
        return this;
    }

    @Override
    public SimpleWriter append(char c) {
        builder.append(c);
        return this;
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }

    @Override
    public int length() {
        return builder.length();
    }

    @Override
    public char charAt(int index) {
        return builder.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return builder.subSequence(start, end);
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    public StringBuilder getBuilder() {
        return builder;
    }

    public char[] toCharArray() {
        return toString().toCharArray();
    }
}
