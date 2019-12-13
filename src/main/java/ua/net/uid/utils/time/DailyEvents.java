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

import java.util.Objects;

public class DailyEvents {
    private final Long sunrise;
    private final Long sunset;
    private final DailyEventType type;

    DailyEvents(Long sunrise, Long sunset, DailyEventType type) {
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.type = type;
    }

    public Long getSunrise() {
        return sunrise;
    }

    public Long getSunset() {
        return sunset;
    }

    public DailyEventType getType() {
        return type;
    }

    public boolean equals(DailyEvents other) {
        return other == this || (other != null
            && Objects.equals(sunrise, other.sunrise)
            && Objects.equals(sunset, other.sunset)
            && Objects.equals(type, other.type));
    }

    @Override
    public int hashCode() {
        return Objects.hash(sunrise, sunset, type) ^ getClass().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof DailyEvents && equals((DailyEvents) other);
    }
}
