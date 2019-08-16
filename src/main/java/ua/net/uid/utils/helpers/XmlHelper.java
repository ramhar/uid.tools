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

import java.io.IOException;

public class XmlHelper {
    private XmlHelper() {
    }

    public static void escape(Appendable builder, CharSequence string, int start, int end) throws IOException {
        for (int i = start; i < end; ++i) {
            char chr = string.charAt(i);
            String entity;
            switch (chr) {
                case '&':
                    entity = "&amp;";
                    break;
                case '<':
                    entity = "&lt;";
                    break;
                case '>':
                    entity = "&gt;";
                    break;
                case '"':
                    entity = "&quot;";
                    break;
                case '\'':
                    entity = "&#39;";
                    break;
                default:
                    continue;
            }
            if (start < i) builder.append(string, start, i);
            start = i + 1;
            builder.append(entity);
        }
        if (start < end) builder.append(string, start, end);
    }

    public static void escape(Appendable builder, CharSequence string) throws IOException {
        escape(builder, string, 0, string.length());
    }

    public static String escape(CharSequence string, int start, int end) {
        StringBuilder builder = new StringBuilder(string.length());
        try {
            escape(builder, string, start, end);
        } catch (IOException ignored) {
        }
        return builder.toString();
    }

    public static String escape(CharSequence string) {
        return escape(string, 0, string.length());
    }
}
