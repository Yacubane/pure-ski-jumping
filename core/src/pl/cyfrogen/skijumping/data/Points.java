package pl.cyfrogen.skijumping.data;

public class Points {
    private int pointsInt;

    public Points() {

    }

    public String getFormattedString() {
        return pointsInt / 10 + "." + pointsInt % 10;
    }

    public void set(JumperResult[] jumperResults) {
        pointsInt = 0;
        for(JumperResult jumperResult : jumperResults) {
            if(jumperResult != null) {
                pointsInt += jumperResult.getIntegerPoints();
            }
        }
    }

    public int getIntegerPoints() {
        return pointsInt;
    }
}
