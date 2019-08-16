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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHelper {
    private static final Pattern NAMED_GROUP = Pattern.compile("(?:[^\\\\]+|(?:\\\\.)+)?\\(\\?<([a-z][a-z0-9]*)>");

    private RegexHelper() {
    }


    public static Set<String> getNamedGroupsSet(Pattern pattern) {
        HashSet<String> result = new HashSet<>();
        Matcher matcher = NAMED_GROUP.matcher(pattern.pattern());
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }

    public static Map<String, Integer> getNamedGroups(Pattern pattern) {
        /*
        !!! does not work in java 9
        try {
            Method method = Pattern.class.getDeclaredMethod("namedGroups");
            method.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, Integer> groups = (Map<String, Integer>) method.invoke(pattern);
            return Collections.unmodifiableMap(groups);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
        */
        throw new UnsupportedOperationException("does not work in java 9");
    }

    public static Map<String, String> getGroupsByName(Matcher matcher, Set<String> groups) {
        if (groups != null) {
            HashMap<String, String> map = new HashMap<>();
            for (String group : groups)
                map.put(group, matcher.group(group));
            return map;
        }
        return null;
    }
}
