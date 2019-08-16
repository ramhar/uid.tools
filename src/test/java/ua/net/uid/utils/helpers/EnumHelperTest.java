package ua.net.uid.utils.helpers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

public class EnumHelperTest {
    @Test
    void testStringToEnumWithDefault() {
        assertNull(EnumHelper.valueOf(Source.class, "zero", null));
        assertEquals(Source.four, EnumHelper.valueOf(Source.class, "zero", Source.four));
        assertEquals(Source.one, EnumHelper.valueOf(Source.class, "one", null));
        assertEquals(Source.four, EnumHelper.valueOf(Source.class, "four", null));
    }

    @Test
    void testStringToEnumDefaultNull() {
        assertNull(EnumHelper.valueOf(Source.class, "zero"));
        assertEquals(Source.two, EnumHelper.valueOf(Source.class, "two"));
        assertEquals(Source.three, EnumHelper.valueOf(Source.class, "three"));
    }

    @Test
    void toList() {
        assertEquals(
                Arrays.asList(Source.one, Source.two, Source.three, Source.four, Source.five),
                EnumHelper.toList(Source.class)
        );
    }

    @Test
    void toMap() {
        Map<String, Source> map = new HashMap<>();
        map.put(Source.one.toString(), Source.one);
        map.put(Source.two.toString(), Source.two);
        map.put(Source.three.toString(), Source.three);
        map.put(Source.four.toString(), Source.four);
        map.put(Source.five.toString(), Source.five);

        assertEquals(map, EnumHelper.toMap(Source.class));
    }

    enum Source {
        one, two, three, four, five
    }
}

