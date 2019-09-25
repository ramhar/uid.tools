package ua.net.uid.utils.time;

public enum Zenith {
    ASTRONOMICAL(108.), // Astronomical sunrise/set is when the sun is 18 degrees below the horizon.
    NAUTICAL(102.),     // Nautical sunrise/set is when the sun is 12 degrees below the horizon.
    CIVIL(96.),         // Civil sunrise/set (dawn/dusk) is when the sun is 6 degrees below the horizon.
    OFFICIAL(90.8333);  // Official sunrise/set is when the sun is 50' below the horizon.
    
    private final double degrees;

    Zenith(double degrees) {
        this.degrees = degrees;
    }

    public double getDegrees() {
        return degrees;
    }
}
