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
