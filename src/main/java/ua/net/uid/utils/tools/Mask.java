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
package ua.net.uid.utils.tools;

import ua.net.uid.utils.Setter;
import ua.net.uid.utils.helpers.RegexHelper;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Mask {
    boolean check(String value);

    default boolean check(String value, Setter<String, Object> setter) {
        return check(value);
    }

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    class Any implements Mask {
        @Override
        public boolean check(String value) {
            return true;
        }

        @Override
        public boolean equals(Object another) {
            return another instanceof Any;
        }

        @Override
        public int hashCode() {
            return 5;
        }
    }

    class Match implements Mask {
        private final String pattern;

        public Match(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public boolean check(String value) {
            return pattern.equals(value);
        }

        @Override
        public boolean equals(Object another) {
            return another instanceof Match && pattern.equals(((Match) another).pattern);
        }

        @Override
        public int hashCode() {
            return 23 * 5 + this.pattern.hashCode();
        }
    }

    class IgnoreCase implements Mask {
        private final String pattern;

        public IgnoreCase(String pattern) {
            this.pattern = pattern.toLowerCase();
        }

        @Override
        public boolean check(String value) {
            return pattern.equalsIgnoreCase(value);
        }

        @Override
        public boolean equals(Object another) {
            return another instanceof IgnoreCase && pattern.equals(((IgnoreCase) another).pattern);
        }

        @Override
        public int hashCode() {
            return 37 * 3 + this.pattern.hashCode();
        }
    }

    class Prefix implements Mask {
        private final String prefix;

        public Prefix(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public boolean check(String value) {
            return value.startsWith(prefix);
        }

        @Override
        public boolean equals(Object another) {
            return another instanceof Prefix && prefix.equals(((Prefix) another).prefix);
        }

        @Override
        public int hashCode() {
            return 59 * 7 + prefix.hashCode();
        }
    }

    class Suffix implements Mask {
        private final String suffix;

        public Suffix(String suffix) {
            this.suffix = suffix;
        }

        @Override
        public boolean check(String value) {
            return value.endsWith(suffix);
        }

        @Override
        public boolean equals(Object another) {
            return another instanceof Suffix && suffix.equals(((Suffix) another).suffix);
        }

        @Override
        public int hashCode() {
            return 53 * 5 + suffix.hashCode();
        }
    }

    class Regexp implements Mask {
        private final Pattern pattern;
        private final Set<String> groups;

        public Regexp(Pattern pattern) {
            this.pattern = pattern;
            this.groups = RegexHelper.getNamedGroupsSet(pattern);
        }

        public Regexp(String pattern) {
            this(Pattern.compile(pattern));
        }

        public Regexp(String pattern, int flags) {
            this(Pattern.compile(pattern, flags));
        }

        @Override
        public boolean check(String value) {
            return check(value, null);
        }

        @Override
        public boolean check(String value, Setter<String, Object> setter) {
            final Matcher matcher = pattern.matcher(value);
            if (matcher.matches()) {
                if (setter != null && !groups.isEmpty()) {
                    for (String group : groups)
                        setter.set(group, matcher.group(group));
                }
                return true;
            }
            return false;
        }

        @Override
        public boolean equals(Object another) {
            return another instanceof Regexp && pattern.equals(((Regexp) another).pattern);
        }

        @Override
        public int hashCode() {
            return 97 * 7 + this.pattern.hashCode();
        }
    }

    class Or implements Mask {
        private final Mask[] items;

        public Or(Mask... items) {
            this.items = items;
        }

        @Override
        public boolean check(String value) {
            return check(value, null);
        }

        @Override
        public boolean check(String value, Setter<String, Object> setter) {
            for (Mask mask : items) {
                if (mask.check(value, setter)) return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return 61 * 7 + Arrays.deepHashCode(items);
        }

        @Override
        public boolean equals(Object another) {
            return another instanceof Or && Arrays.deepEquals(items, ((Or) another).items);
        }
    }

    class And implements Mask {
        private final Mask[] items;

        public And(Mask... items) {
            this.items = items;
        }

        @Override
        public boolean check(String value) {
            return check(value, null);
        }

        @Override
        public boolean check(String value, Setter<String, Object> setter) {
            for (Mask mask : items) {
                if (!mask.check(value, setter)) return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return 67 * 3 + Arrays.deepHashCode(items);
        }

        @Override
        public boolean equals(Object another) {
            return another instanceof And && Arrays.deepEquals(items, ((And) another).items);
        }
    }
}
