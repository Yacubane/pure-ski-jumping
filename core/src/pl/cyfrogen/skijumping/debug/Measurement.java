package pl.cyfrogen.skijumping.debug;

public class Measurement {
    private final long start;
    private long end;

    public static Measurement startMeasurement() {
        return new Measurement();
    }

    public Measurement() {
        this.start = System.nanoTime();
    }

    public void end() {
        this.end = System.nanoTime();
    }

    public long getDifference() {
        return end - start;
    }
}
