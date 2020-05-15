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

import static org.junit.jupiter.api.Assertions.assertEquals;

class XmlHelperTest {
    @SuppressWarnings("SpellCheckingInspection")
    private static final String src = " &< &-amp; <-lt; >-gt; \"-quot; '-#39; >& ";

    @Test
    void testEscapeXmlEntitiesByRangeToAppendable() throws Exception {
        StringBuilder builder = new StringBuilder();
        XmlHelper.escape(builder, src, 4, src.length() - 4);
        assertEquals(
                "&amp;-amp; &lt;-lt; &gt;-gt; &quot;-quot; &#39;-#39;",
                builder.toString()
        );
    }

    @Test
    void testEscapeXmlEntitiesToAppendable() throws Exception {
        StringBuilder builder = new StringBuilder();
        XmlHelper.escape(builder, src);
        assertEquals(
                " &amp;&lt; &amp;-amp; &lt;-lt; &gt;-gt; &quot;-quot; &#39;-#39; &gt;&amp; ",
                builder.toString()
        );
    }

    @Test
    void testEscapeXmlEntitiesByRangeToString() {
        assertEquals(
                "&amp;-amp; &lt;-lt; &gt;-gt; &quot;-quot; &#39;-#39;",
                XmlHelper.escape(src, 4, src.length() - 4)
        );
    }

    @Test
    void testEscapeXmlEntitiesToString() {
        assertEquals(
                " &amp;&lt; &amp;-amp; &lt;-lt; &gt;-gt; &quot;-quot; &#39;-#39; &gt;&amp; ",
                XmlHelper.escape(src)
        );
    }
}
