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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class IOHelperTest {
    private static final byte[] dataEmpty = {};
    private static final byte[] dataShort = RandomHelper.randomString(1024, 1024).getBytes(StandardCharsets.UTF_8);
    private static final byte[] dataLong = RandomHelper.randomString(16 * 1024, 32 * 1024).getBytes(StandardCharsets.UTF_8);

    @Test
    void testCopyStreamWithBuffer() throws IOException {
        byte[] buffer = new byte[2048];
        try (InputStream input = new ByteArrayInputStream(dataEmpty)) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            assertEquals(dataEmpty.length, IOHelper.copy(input, output, buffer));
            assertArrayEquals(dataEmpty, output.toByteArray());
        }
        try (InputStream input = new ByteArrayInputStream(dataShort)) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            assertEquals(dataShort.length, IOHelper.copy(input, output, buffer));
            assertArrayEquals(dataShort, output.toByteArray());
        }
        try (InputStream input = new ByteArrayInputStream(dataLong)) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            assertEquals(dataLong.length, IOHelper.copy(input, output, buffer));
            assertArrayEquals(dataLong, output.toByteArray());
        }
    }

    @Test
    void testCopyStreamWithBufferSize() throws IOException {
        try (InputStream input = new ByteArrayInputStream(dataEmpty)) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            assertEquals(dataEmpty.length, IOHelper.copy(input, output, 4096));
            assertArrayEquals(dataEmpty, output.toByteArray());
        }
        try (InputStream input = new ByteArrayInputStream(dataShort)) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            assertEquals(dataShort.length, IOHelper.copy(input, output, 4096));
            assertArrayEquals(dataShort, output.toByteArray());
        }
        try (InputStream input = new ByteArrayInputStream(dataLong)) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            assertEquals(dataLong.length, IOHelper.copy(input, output, 4096));
            assertArrayEquals(dataLong, output.toByteArray());
        }
    }

    @Test
    void testCopyStreamWithDefault() throws IOException {
        try (InputStream input = new ByteArrayInputStream(dataEmpty)) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            assertEquals(dataEmpty.length, IOHelper.copy(input, output));
            assertArrayEquals(dataEmpty, output.toByteArray());
        }
        try (InputStream input = new ByteArrayInputStream(dataShort)) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            assertEquals(dataShort.length, IOHelper.copy(input, output));
            assertArrayEquals(dataShort, output.toByteArray());
        }
        try (InputStream input = new ByteArrayInputStream(dataLong)) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            assertEquals(dataLong.length, IOHelper.copy(input, output));
            assertArrayEquals(dataLong, output.toByteArray());
        }
    }

    @Test
    void testCopyReaderWithBuffer() throws IOException {
        char[] buffer = new char[2048];
        {
            String source = new String(dataEmpty, StandardCharsets.UTF_8);
            try (Reader reader = new StringReader(source)) {
                StringWriter writer = new StringWriter();
                assertEquals(source.length(), IOHelper.copy(reader, writer, buffer));
                assertEquals(source, writer.toString());
            }
        }
        {
            String source = new String(dataShort, StandardCharsets.UTF_8);
            try (Reader reader = new StringReader(source)) {
                StringWriter writer = new StringWriter();
                assertEquals(source.length(), IOHelper.copy(reader, writer, buffer));
                assertEquals(source, writer.toString());
            }
        }
        {
            String source = new String(dataLong, StandardCharsets.UTF_8);
            try (Reader reader = new StringReader(source)) {
                StringWriter writer = new StringWriter();
                assertEquals(source.length(), IOHelper.copy(reader, writer, buffer));
                assertEquals(source, writer.toString());
            }
        }
    }

    @Test
    void testCopyReaderWithBufferSize() throws IOException {
        {
            String source = new String(dataEmpty, StandardCharsets.UTF_8);
            try (Reader reader = new StringReader(source)) {
                StringWriter writer = new StringWriter();
                assertEquals(source.length(), IOHelper.copy(reader, writer, 4000));
                assertEquals(source, writer.toString());
            }
        }
        {
            String source = new String(dataShort, StandardCharsets.UTF_8);
            try (Reader reader = new StringReader(source)) {
                StringWriter writer = new StringWriter();
                assertEquals(source.length(), IOHelper.copy(reader, writer, 4000));
                assertEquals(source, writer.toString());
            }
        }
        {
            String source = new String(dataLong, StandardCharsets.UTF_8);
            try (Reader reader = new StringReader(source)) {
                StringWriter writer = new StringWriter();
                assertEquals(source.length(), IOHelper.copy(reader, writer, 4000));
                assertEquals(source, writer.toString());
            }
        }
    }

    @Test
    void testCopyReaderWithDefault() throws IOException {
        {
            String source = new String(dataEmpty, StandardCharsets.UTF_8);
            try (Reader reader = new StringReader(source)) {
                StringWriter writer = new StringWriter();
                assertEquals(source.length(), IOHelper.copy(reader, writer));
                assertEquals(source, writer.toString());
            }
        }
        {
            String source = new String(dataShort, StandardCharsets.UTF_8);
            try (Reader reader = new StringReader(source)) {
                StringWriter writer = new StringWriter();
                assertEquals(source.length(), IOHelper.copy(reader, writer));
                assertEquals(source, writer.toString());
            }
        }
        {
            String source = new String(dataLong, StandardCharsets.UTF_8);
            try (Reader reader = new StringReader(source)) {
                StringWriter writer = new StringWriter();
                assertEquals(source.length(), IOHelper.copy(reader, writer));
                assertEquals(source, writer.toString());
            }
        }
    }

    @Test
    void testToByteArrayWithBuffer() throws IOException {
        assertArrayEquals(dataLong, IOHelper.toByteArray(new ByteArrayInputStream(dataLong), new byte[2048]));
    }

    @Test
    void testToByteArrayWithBufferSize() throws IOException {
        assertArrayEquals(dataLong, IOHelper.toByteArray(new ByteArrayInputStream(dataLong), 2048));
    }

    @Test
    void testToByteArrayWithDefault() throws IOException {
        assertArrayEquals(dataLong, IOHelper.toByteArray(new ByteArrayInputStream(dataLong)));
    }

    @Test
    void testBufferedInput() {
        InputStream src = null;
        BufferedInputStream dst = null;

        assertNull(IOHelper.buffered(src));

        src = new ByteArrayInputStream(dataLong);
        dst = IOHelper.buffered(src);
        assertNotNull(dst);
        assertNotSame(src, dst);
        
        src = dst;
        dst = IOHelper.buffered(src);
        assertNotNull(dst);
        assertSame(src, dst);
    }

    @Test
    void testBufferedOutput() {
        OutputStream src = null;
        BufferedOutputStream dst = null;

        assertNull(IOHelper.buffered(src));

        src = new ByteArrayOutputStream();
        dst = IOHelper.buffered(src);
        assertNotNull(dst);
        assertNotSame(src, dst);
        
        src = dst;
        dst = IOHelper.buffered(src);
        assertNotNull(dst);
        assertSame(src, dst);
    }

    @Test
    void testBufferedReader() {
        Reader src = null;
        BufferedReader dst = null;

        assertThrows(NullPointerException.class, () -> IOHelper.buffered((Reader) null));

        src = new StringReader("");
        dst = IOHelper.buffered(src);
        assertNotSame(src, dst);
        
        src = dst;
        dst = IOHelper.buffered(src);
        assertSame(src, dst);
    }

    @Test
    void testBufferedWriter() {
        Writer src = null;
        BufferedWriter dst = null;

        assertThrows(NullPointerException.class, () -> IOHelper.buffered((Writer) null));

        src = new StringWriter();
        dst = IOHelper.buffered(src);
        assertNotSame(src, dst);
        
        src = dst;
        dst = IOHelper.buffered(src);
        assertSame(src, dst);
    }
}
