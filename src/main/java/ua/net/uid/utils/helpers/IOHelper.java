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
package ua.net.uid.utils.helpers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class IOHelper {
    public static final int DEFAULT_BUFFER_SIZE = 8 * 1024;

    public static long copy(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long result = 0;
        int readed;
        while ((readed = input.read(buffer)) != -1) {
            output.write(buffer, 0, readed);
            result += readed;
        }
        return result;
    }

    public static long copy(InputStream input, OutputStream output, int buffer) throws IOException {
        return copy(input, output, new byte[buffer]);
    }

    public static long copy(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, DEFAULT_BUFFER_SIZE);
    }

    public static long copy(Reader reader, Writer writer, char[] buffer) throws IOException {
        long result = 0;
        int readed;
        while ((readed = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, readed);
            result += readed;
        }
        return result;
    }

    public static long copy(Reader reader, Writer writer, int buffer) throws IOException {
        return copy(reader, writer, new char[buffer]);
    }

    public static long copy(Reader reader, Writer writer) throws IOException {
        return copy(reader, writer, DEFAULT_BUFFER_SIZE);
    }

    public static byte[] toByteArray(final InputStream input, byte[] buffer) throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            copy(input, output, buffer);
            return output.toByteArray();
        }
    }

    public static byte[] toByteArray(final InputStream input, int buffer) throws IOException {
        return toByteArray(input, new byte[buffer]);
    }

    public static byte[] toByteArray(final InputStream input) throws IOException {
        return toByteArray(input, DEFAULT_BUFFER_SIZE);
    }

    public static BufferedInputStream buffered(InputStream input) {
        if (input == null) return null;
        return input instanceof BufferedInputStream ? (BufferedInputStream) input : new BufferedInputStream(input);
    }

    public static BufferedOutputStream buffered(OutputStream output) {
        if (output == null) return null;
        return output instanceof BufferedOutputStream ? (BufferedOutputStream) output : new BufferedOutputStream(output);
    }

    public static BufferedReader buffered(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static BufferedWriter buffered(Writer writer) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer);
    }
    
    private IOHelper() {}
}