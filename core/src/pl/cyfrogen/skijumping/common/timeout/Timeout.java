package pl.cyfrogen.skijumping.common.timeout;

public class Timeout {
    TimeoutEvent event;
    int currentFrame;
    int eventFrame;
    TimeoutEvent removeEvent;
    private TimeoutEventProgress progress;

    public Timeout(float sec, TimeoutEvent event) {
        this.event = event;
        eventFrame = (int) (sec * 60);
    }

    public boolean update() {
        if (currentFrame >= eventFrame) {
            return true;
        } else {
            currentFrame++;
            if (currentFrame >= eventFrame) {
                return true;
            } else {
                if (progress != null) progress.fire(getPercent());
                return false;
            }
        }
    }

    public float getPercent() {
        return (currentFrame / (float) eventFrame);
    }

    public float getFullTime() {
        return eventFrame / (float) 60;
    }
}
