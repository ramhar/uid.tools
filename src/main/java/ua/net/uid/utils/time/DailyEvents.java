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
