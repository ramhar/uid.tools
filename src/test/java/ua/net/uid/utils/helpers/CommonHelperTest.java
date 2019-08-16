package ua.net.uid.utils.helpers;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonHelperTest {

    CommonHelperTest() {
    }

    @Test
    void testClose() {
        AutoCloseableImpl closeable = new AutoCloseableImpl();
        CommonHelper.close(closeable);
        assertTrue(closeable.called);
    }

    @Test
    void testIsEmptyCharSequence() {
        StringBuilder src = null;
        assertTrue(CommonHelper.isEmpty((CharSequence) null));
        src = new StringBuilder();
        assertTrue(CommonHelper.isEmpty(src));
        src.append("test");
        assertFalse(CommonHelper.isEmpty(src));
    }

    @Test
    void testIsEmptyString() {
        String src = null;
        assertTrue(CommonHelper.isEmpty(src));
        src = "";
        assertTrue(CommonHelper.isEmpty(src));
        src = "test";
        assertFalse(CommonHelper.isEmpty(src));
    }

    @Test
    void testIsEmptyCollection() {
        Collection src = null;
        assertTrue(CommonHelper.isEmpty(src));
        src = new ArrayList();
        assertTrue(CommonHelper.isEmpty(src));
        src.add("1");
        assertFalse(CommonHelper.isEmpty(src));
    }

    @Test
    void testIsEmptyMap() {
        Map src = null;
        assertTrue(CommonHelper.isEmpty(src));
        src = new HashMap();
        assertTrue(CommonHelper.isEmpty(src));
        src.put(1, "1");
        assertFalse(CommonHelper.isEmpty(src));
    }

    @Test
    void testIsEmptyGenericArray() {
        Integer[] src = null;
        assertTrue(CommonHelper.isEmpty(src));
        src = new Integer[0];
        assertTrue(CommonHelper.isEmpty(src));
        src = new Integer[]{1};
        assertFalse(CommonHelper.isEmpty(src));
    }

    static class AutoCloseableImpl implements AutoCloseable {
        boolean called = false;

        @Override
        public void close() throws Exception {
            called = true;
            throw new IOException("test exception");
        }
    }
}
