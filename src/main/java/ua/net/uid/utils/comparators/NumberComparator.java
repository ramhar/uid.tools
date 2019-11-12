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
package ua.net.uid.utils.comparators;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author nightfall
 */
public class NumberComparator implements Comparator<Number> {
    public static NumberComparator getInstance() {
        return Holder.INSTANCE;
    }
    
    private NumberComparator() {}
    
    @Override
    public int compare(Number o1, Number o2) {
        if (o1 == o2) return 0;
        if (o1 == null) return -1;
        if (o2 == null) return 1;
        if (o1.getClass() == o2.getClass() && o1 instanceof Comparable) 
            return ((Comparable) o1).compareTo((Comparable) o2);
        
        if (o1 instanceof BigDecimal) return compareBig((BigDecimal)o1, o2);
        if (o2 instanceof BigDecimal) return -compareBig((BigDecimal) o2, o1);
        if (o1 instanceof BigInteger) return compareBig((BigInteger) o1, o2);
        if (o2 instanceof BigInteger) return -compareBig((BigInteger) o2, o1);

        return isInteger(o1) && isInteger(o2)
            ? Long.compare(o1.longValue(), o1.longValue())
            : Double.compare(o1.floatValue(), o2.floatValue());
    }

    private static boolean isInteger(Number val) {
        return val instanceof Byte || val instanceof Short 
            || val instanceof Integer || val instanceof Long
            || val instanceof AtomicInteger || val instanceof AtomicLong;
    }
    
    private static int compareBig(BigDecimal o1, Number o2) {
        if (o2 instanceof BigInteger)
            return o1.compareTo(new BigDecimal((BigInteger) o2));
        if (o2 instanceof Double || o2 instanceof Float)
            return o1.compareTo(BigDecimal.valueOf(o2.doubleValue()));
        return o1.compareTo(BigDecimal.valueOf(o2.longValue()));
    }
    
    private static int compareBig(BigInteger o1, Number o2) {
        return o2 instanceof Double || o2 instanceof Float
            ? Double.compare(o1.doubleValue(), o2.doubleValue())
            : o1.compareTo(BigInteger.valueOf(o2.longValue()));
    }
    
    private static final class Holder {
        private static final NumberComparator INSTANCE = new NumberComparator();
    }
}
