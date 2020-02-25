package pl.cyfrogen.skijumping.data;

public class WorldCupJumperData {
    private final JumperResult[][] jumperResults;
    private final Points[] points;
    private int[] places;
    private int worldCupPoints;

    private final int jumperNumber;
    private JumperData jumperData;
    private int worldCupPlace;

    public WorldCupJumperData(JumperData jumperData, int jumperNumber, int hills, int rounds) {
        this.jumperData = jumperData;
        this.jumperNumber = jumperNumber;
        this.jumperResults = new JumperResult[hills][rounds];
        this.points = new Points[hills];
        this.places = new int[hills];
        for (int i = 0; i < hills; i++) {
            points[i] = new Points();
        }
    }

    public JumperData getJumperData() {
        return jumperData;
    }

    public JumperResult getDistance(int hill, int round) {
        return jumperResults[hill][round];
    }

    public String getDistanceString(int hill, int round) {
        if (jumperResults[hill][round] == null) return "";
        return jumperResults[hill][round].getFormattedDistance();
    }

    public void addResult(int hill, int round, double distance, double points) {
        this.jumperResults[hill][round] = new JumperResult(distance, points);
        this.points[hill].set(jumperResults[hill]);
    }

    public int getJumperNumber() {
        return jumperNumber;
    }

    public String getPointsString(int hill) {
        return points[hill].getFormattedString();
    }

    public int getPlace(int hill) {
        return places[hill];
    }

    public Points getPoints(int hill) {
        return points[hill];
    }

    public void setPlace(int hill, int i) {
        this.places[hill] = i;
    }

    public boolean hasPlace(int hill) {
        return places[hill] > 0;
    }

    public void addWorldCupPoints(int worldCupForPosition) {
        this.worldCupPoints += worldCupForPosition;
    }

    public int getWorldCupPoints() {
        return worldCupPoints;
    }

    public void setWorldCupPlace(int i) {
        this.worldCupPlace = i;
    }

    public int getWorldCupPlace() {
        return worldCupPlace;
    }
}
