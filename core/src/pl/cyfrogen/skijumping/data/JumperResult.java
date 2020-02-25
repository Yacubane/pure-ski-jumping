package pl.cyfrogen.skijumping.data;

public class JumperResult {
    private final double precisePoints;
    private final double preciseDistance;
    private final int distanceInteger;
    private final int pointsInteger;

    public JumperResult(double distance, double points) {
        this.preciseDistance = distance;
        this.precisePoints = points;

        this.distanceInteger = (int) (distance * 100);
        this.pointsInteger = (int) (points * 10);
    }

    public int getIntegerPoints() {
        return pointsInteger;
    }

    public int getDistanceInteger() {
        return distanceInteger;
    }

    public String getFormattedDistance() {
        return distanceInteger / 100 + "." + distanceInteger % 100 + " m";
    }
}
