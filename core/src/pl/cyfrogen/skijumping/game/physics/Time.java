package pl.cyfrogen.skijumping.game.physics;

class Time {
    public static final Time NONE = new Time();
    private double time;
    private final boolean isTime;

    private Time(double time) {
        this.time = time;
        this.isTime = true;
    }

    private Time() {
        this.isTime = false;
    }

    public static Time none() {
        return NONE;
    }

    public static Time of(double time) {
        if (time < 0)
            return NONE;
        return new Time(time);
    }

    public boolean isTime() {
        return isTime;
    }

    public double get() {
        if (!isTime())
            throw new IllegalStateException("Cannot get NONE time");
        return time;
    }

    public static Time getSmallest(Time... times) {
        Time smallestTime = Time.none();
        for (Time time : times) {
            if (smallestTime.isTime() && time.isTime() && time.get() < smallestTime.get()) {
                smallestTime = time;
            } else if (!smallestTime.isTime() && time.isTime()) {
                smallestTime = time;
            }
        }
        return smallestTime;
    }

    @Override
    public String toString() {
        if (isTime()) {
            return "[" + get() + "]";

        } else {
            return "[null]";
        }
    }
}
