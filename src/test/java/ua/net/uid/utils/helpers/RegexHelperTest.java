package ua.net.uid.utils.helpers;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;


class RegexHelperTest {
    @Test
    void testGetNamedGroups() {
        Pattern pattern = Pattern.compile("(.+)@(?<host>[^@]+(\\.)(?<zone>[^.@]+))");
        Set<String> result = RegexHelper.getNamedGroupsSet(pattern);
        assertTrue(result.contains("host"));
        assertTrue(result.contains("zone"));
    }

    /*
    @Test
    void testGetNamedGroups() {
        Pattern pattern = Pattern.compile("(.+)@(?<host>[^@]+(\\.)(?<zone>[^.@]+))");
        Map<String, Integer> result = RegexHelper.getNamedGroups(pattern);
        assertEquals((Integer) 2, result.get("host"));
        assertEquals((Integer) 4, result.get("zone"));
    }

    @Test
    void testGetGroupsByName() {
        Pattern pattern = Pattern.compile("^(.+)@(?<host>([^@]+\\.)?(?<zone>[^.@]+))$");
        Map<String, Integer> groupsMap = RegexHelper.getNamedGroups(pattern);
        Set<String> groups = groupsMap.keySet();
        Matcher matcher = pattern.matcher("nobody@test.mail.com");
        assertTrue(matcher.matches());
        Map<String, String> result = RegexHelper.getGroupsByName(matcher, groups);
        assertEquals("test.mail.com", matcher.group(groupsMap.get("host")));
        assertEquals("test.mail.com", result.get("host"));
        assertEquals("com", matcher.group(groupsMap.get("zone")));
        assertEquals("com", result.get("zone"));
    }
    */
}
