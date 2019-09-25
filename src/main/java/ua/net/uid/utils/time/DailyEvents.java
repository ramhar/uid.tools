package ua.net.uid.utils.time;

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
}
