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

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

public class CharsetHelper {
    private CharsetHelper() {}

    public static boolean isSupported(String charset) {
        if (charset != null) {
            try {
                return Charset.isSupported(charset);
            } catch (IllegalCharsetNameException ex) {}
        }
        return false;
    }

    public static Charset toCharset(Charset charset) {
        return charset == null ? Charset.defaultCharset() : charset;
    }

    public static Charset toCharset(String charsetName) {
        return charsetName == null ? Charset.defaultCharset() : Charset.forName(charsetName);
    }

    public static String toCharsetName(String charsetName) {
        return toCharset(charsetName).name();
    }
}