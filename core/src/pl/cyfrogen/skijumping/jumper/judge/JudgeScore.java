package pl.cyfrogen.skijumping.jumper.judge;

public class JudgeScore {
    public boolean isActive() {
        return active;
    }

    private boolean active;

    public int getPoints() {
        return points;
    }

    private final int points;

    public JudgeScore(int points) {
        this.points = points;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
