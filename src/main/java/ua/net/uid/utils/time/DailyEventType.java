package ua.net.uid.utils.time;

public enum DailyEventType {
    NORMAL_DAY("normal"), 
    POLAR_NIGHT("night"), 
    POLAR_DAY("day");
    
    private final String title;

    DailyEventType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
