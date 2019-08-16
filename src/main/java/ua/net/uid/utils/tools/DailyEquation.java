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

import java.util.Calendar;

/**
 * 
 * 
 * https://web.archive.org/web/20161202180207/http://williams.best.vwh.net/sunrise_sunset_algorithm.htm
 * http://edwilliams.org/sunrise_sunset_algorithm.htm
 * https://edwilliams.org/sunrise_sunset_example.htm
 * 
 * https://github.com/KlausBrunner/solarpositioning
 * https://github.com/mikereedell/sunrisesunsetlib-java
 * https://github.com/shred/commons-suncalc
 * https://gist.github.com/Tafkas/4742250
 * 
 * https://github.com/caarmen/SunriseSunset
 * http://users.electromagnetic.net/bu/astro/sunrise-set.php
 * 
 * 
 * https://www.aa.quae.nl/en/reken/zonpositie.html
 * 
 * @author nightfall
 */
public class DailyEquation {
    ////////////////////////////////////////////////////////////////////////////
    public enum Zenith {
        ASTRONOMICAL(108.),     // Astronomical sunrise/set is when the sun is 18 degrees below the horizon.
        NAUTICAL(102.),         // Nautical sunrise/set is when the sun is 12 degrees below the horizon.
        CIVIL(96.),             // Civil sunrise/set (dawn/dusk) is when the sun is 6 degrees below the horizon.
        OFFICIAL(90.8333);      // Official sunrise/set is when the sun is 50' below the horizon.
        
        private final double degrees;
        Zenith(double degrees) { this.degrees = degrees; }
        public double getDegrees() { return degrees; }
    }
    ////////////////////////////////////////////////////////////////////////////
    public enum Type {
        NORMAL_DAY("normal"),
        POLAR_NIGHT("night"),
        POLAR_DAY("day");
        private final String title;
        Type(String title) { this.title = title; }
        public String getTitle() { return title; }
    }
    ////////////////////////////////////////////////////////////////////////////
    public static class Events {
        private final Long sunrise;
        private final Long sunset;
        private final Type type;
        Events(Long sunrise, Long sunset, Type type) {
            this.sunrise = sunrise;
            this.sunset = sunset;
            this.type = type;
        }
        public Long getSunrise() { return sunrise; }
        public Long getSunset() { return sunset; }
        public Type getType() { return type; }
    }
    ////////////////////////////////////////////////////////////////////////////
    public static Events getSunEvents(double latitude, double longitude, Calendar calendar, Zenith zenith) {
        return getSunEvents(latitude, longitude, calendar, zenith.getDegrees());
    }
    public static Events getSunEvents(double latitude, double longitude, Calendar calendar, double zenith) {
        longitude /= 15;
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        double sunrizeTime = calculateEvent(latitude, longitude, zenith, dayOfYear, true);
        double sunsetTime = calculateEvent(latitude, longitude, zenith, dayOfYear, false);

        if (sunrizeTime == Double.NEGATIVE_INFINITY || sunsetTime == Double.NEGATIVE_INFINITY) { // the sun never rises on this location (on the specified date)
            return new Events(null, null, Type.POLAR_NIGHT);
        } else if (sunrizeTime == Double.POSITIVE_INFINITY || sunsetTime == Double.POSITIVE_INFINITY) { // the sun never sets on this location (on the specified date)
            return new Events(null, null, Type.POLAR_DAY);
        } else {
            return new Events(convertDate(calendar, sunrizeTime), convertDate(calendar, sunsetTime), Type.NORMAL_DAY);
        }
    }
    
    protected static double calculateEvent(double latitude, double baseLongitudeHour, double zenith, int dayOfYear, boolean rising) {
        //t = ((rising ? 6 : 18) - lngHour) / 24 + dayOfYear;
        double longitudeHour = ((rising ? 6 : 18) - baseLongitudeHour) / 24 + dayOfYear;
        // M = 0.9856 * t - 3.289;
        double meanAnomaly = 0.9856 * longitudeHour - 3.289;
        double meanAnomalyRadians = Math.toRadians(meanAnomaly);
        // L = M + (1.916 * sin(M)) + (0.020 * sin(2 * M)) + 282.634
        double sunTrueLong = meanAnomaly + (1.916 * Math.sin(meanAnomalyRadians)) + (0.020 * Math.sin(2 * meanAnomalyRadians)) + 282.634; // L
        if (sunTrueLong > 360) sunTrueLong -= 360;
        
        // sinDec = 0.39782 * sin(L);
        double sinSunDeclination =  0.39782 * Math.sin(Math.toRadians(sunTrueLong));
        // cosDec = cos(asin(sinDec));
        double cosineSunDeclination = Math.cos(Math.asin(sinSunDeclination));
        // cosH = (cos(zenith) - (sinDec *  sin(latitude))) / (cosDec *  cos(latitude));
        double latitudeRadians = Math.toRadians(latitude);
        double cosineSunLocalHour = (Math.cos(Math.toRadians(zenith)) - sinSunDeclination * Math.sin(latitudeRadians))
                / (cosineSunDeclination * Math.cos(latitudeRadians));
        
        if (cosineSunLocalHour > 1) return Double.NEGATIVE_INFINITY;  // the sun never rises on this location (on the specified date)
	if (cosineSunLocalHour < -1) return Double.POSITIVE_INFINITY; // the sun never sets on this location (on the specified date)
        
        // H = (rising ? 360 - acos(cosH) : acos(cosH)) / 15;
        double sunLocalHour = Math.toDegrees(Math.acos(cosineSunLocalHour));
        sunLocalHour = (rising ? 360 - sunLocalHour : sunLocalHour) / 15;
        
        // RA = atan(0.91764 * tan(L)); // RA
        //double rightAscension = Math.atan(Math.toRadians(0.91764 * Math.toDegrees( Math.tan(Math.toRadians(sunTrueLong)) )));
        double rightAscension = Math.toDegrees(Math.atan(Math.toRadians(0.91764 * Math.toDegrees( Math.tan(Math.toRadians(sunTrueLong)) ))));
        
        //--- //RA potentially needs to be adjusted into the range [0,360) by adding/subtracting 360
        //--- if (rightAscension < 0) { rightAscension += 360; }  else if (rightAscension > 360) { rightAscension -= 360; }
        // RA = (RA + (floor(L/90) - floor(RA/90)) * 90) / 15;
        rightAscension = (rightAscension + (Math.floor(sunTrueLong / 90) - Math.floor(rightAscension / 90)) * 90) / 15;
        
        // T = H + RA - (0.06571 * t) - 6.622
        double localMeanTime = sunLocalHour + rightAscension - (longitudeHour * 0.06571) - 6.622; // T
        
        // UT = T - lngHour
        return localMeanTime - baseLongitudeHour;
    }
    
    private static final int DAYMSEC = 24 * 60 * 60 * 1000;
    protected static long convertDate(Calendar calendar, double utcTime)  {
        // localT = UT + localOffset
        int time = (int)(utcTime * 3600000) + calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
        // UT potentially needs to be adjusted into the range [0,24) by adding/subtracting 24
        //if (utcTime >= 24.) utcTime -= 24; else if (utcTime < 0) utcTime += 24;
        if (time >= DAYMSEC) time -= DAYMSEC; else if (time < 0) time += DAYMSEC;
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MILLISECOND, time);
        return calendar.getTimeInMillis();
    }
}
