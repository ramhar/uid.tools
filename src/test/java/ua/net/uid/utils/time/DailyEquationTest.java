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
package ua.net.uid.utils.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author nightfall
 */
public class DailyEquationTest {
    private static final SimpleDateFormat DATE_FORMAT_DAY = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat DATE_FORMAT_MINUTES = new SimpleDateFormat("yyyyMMdd HH:mm");
    
    @Test
    public void testCalculateEvent() {
        //https://edwilliams.org/sunrise_sunset_example.htm
        assertEquals(9.441, DailyEquation.calculateEvent(40.9, -74.3 / 15, Zenith.OFFICIAL.getDegrees(), 176, true), 0.001);
    }    
    
    @Test
    public void testGetSunEvents() throws ParseException {
        //https://edwilliams.org/sunrise_sunset_example.htm
        assertEvents("America/New_York", "19900625", 40.9, -74.3, "05:26", "20:33"); // June 25, 1990 / Wayne, NJ
        
        //https://github.com/caarmen/SunriseSunset/blob/master/library/src/test/java/ca/rmen/sunrisesunset/test/SunriseSunsetTest.java
        assertEvents("PST", "20130120", 34.0522, -118.2437, "06:56", "17:11");
        assertEvents("CET", "20130120", 48.8567, 2.351, "08:35", "17:28");
        assertEvents("Australia/Sydney", "20121225", -33.86, 151.2111, "05:43", "20:07");
        assertEvents("Japan", "20130501", 35.6938, 139.7036, "04:50", "18:27");
        assertEvents("Europe/Dublin", "20130605", 53.3441, -6.2675, "05:00", "21:46");
        assertEvents("CST", "20130622", 41.8781, -87.6298, "05:15", "20:29");
        assertEvents("Pacific/Honolulu", "20150827", 21.3069, -157.8583, "06:13", "18:51");
        assertEvents("America/Argentina/Buenos_Aires", "20130501", -34.6092, -58.3732, "07:29", "18:11");
        assertEvents("America/Argentina/Buenos_Aires", "20131019", -34.6092, -58.3732, "06:06", "19:10");

        assertEvents("America/Argentina/Buenos_Aires", "20130126", -34.6092, -58.3732, "06:07", "20:04");
        assertEvents("America/Argentina/Buenos_Aires", "20131020", -34.6092, -58.3732, "06:05", "19:11");
        assertEvents("America/Argentina/Buenos_Aires", "20131031", -34.6092, -58.3732, "05:53", "19:21");

        assertEvents("Antarctica/McMurdo", "20150419", -77.8456, 166.6693, "10:34", "15:04"); // "10:37", "15:08"
        assertEvents("Antarctica/McMurdo", "20150621", -77.8456, 166.6693, Zenith.OFFICIAL, DailyEventType.POLAR_NIGHT, null, null);
        assertEvents("Antarctica/McMurdo", "20150921", -77.8456, 166.6693, "06:49", "18:47");
        assertEvents("Antarctica/McMurdo", "20151221", -77.8456, 166.6693, Zenith.OFFICIAL, DailyEventType.POLAR_DAY, null, null);
        
        //https://github.com/caarmen/SunriseSunset/blob/master/library/src/test/java/ca/rmen/sunrisesunset/test/SunriseSunsetSpecificLocationsTest.java
        assertEvents("EST", "20160228", 82.50178, -62.34809, "10:13", "12:47"); // "10:27", "12:19
        assertEvents("EST", "20160228", 82.5018, -62.3481, "10:13", "12:47"); // "10:27", "12:19"
        assertEvents("EST", "20160228", 82.50177764892578, -62.34809112548828, "10:13", "12:47"); // "10:17", "12:31"
        assertEvents("EST", "20160228", 82.501667, -62.348056, "10:13", "12:47"); // "10:15", "12:32"
        assertEvents("EST", "20160228", 82.5, -62.35, "10:13", "12:48"); // "10:14", "12:33"
        assertEvents("EST", "20160228", 82.50177764892578, -62.34809112548828, "10:13", "12:47"); // "10:14", "12:33"
        
        assertEvents("Europe/Kiev", "20190708", 50.401984, 30.531065, "04:55", "21:09" );
        assertEvents("Asia/Kamchatka", "20190706", 53.047252, 158.628714, "05:07", "21:52");
        
        assertEvents("America/Moscow", "20181201", 69.000779, 33.116392, "09:14", "09:44"); // Мурманск - https://ru.wikipedia.org/wiki/%D0%9F%D0%BE%D0%BB%D1%8F%D1%80%D0%BD%D0%B0%D1%8F_%D0%BD%D0%BE%D1%87%D1%8C
        assertEvents("America/Moscow", "20181202", 69.000779, 33.116392, Zenith.OFFICIAL, DailyEventType.POLAR_NIGHT, null, null);
        assertEvents("America/Moscow", "20190111", 69.000779, 33.116392, Zenith.OFFICIAL, DailyEventType.POLAR_NIGHT, null, null);
        assertEvents("America/Moscow", "20190112", 69.000779, 33.116392, "09:29", "10:29");
        
        assertEvents("Atlantic/Reykjavik", "20190708", 65.67, -18.1, "02:26", "00:06"); // Акюрейри, Регион Нордюрланд-Эйстра, Исландия
        
    }

    private void assertEvents(String timezone, String date, double lat, double lon, String sr, String ss) throws ParseException {
        assertEvents(timezone, date, lat, lon, Zenith.OFFICIAL, DailyEventType.NORMAL_DAY, sr, ss);
    }
    private void assertEvents(String timezone, String date, double lat, double lon, Zenith zenith, DailyEventType type, String sr, String ss) throws ParseException {
        TimeZone tz = TimeZone.getTimeZone(timezone);
        
        SimpleDateFormat parse = (SimpleDateFormat) DATE_FORMAT_DAY.clone();
        parse.setTimeZone(tz);
        
        Calendar calendar = Calendar.getInstance(tz);
        calendar.setTime(parse.parse(date));
        
        DailyEvents events = DailyEquation.getSunEvents(lat, lon, calendar, zenith);
        
        assertSame(type, events.getType());
        if (type == DailyEventType.NORMAL_DAY) {
            SimpleDateFormat format = (SimpleDateFormat) DATE_FORMAT_MINUTES.clone();
            format.setTimeZone(tz);
            assertEquals(date + ' ' + sr, format.format(events.getSunrise()));
            assertEquals(date + ' ' + ss, format.format(events.getSunset()));
        }
    }
}
