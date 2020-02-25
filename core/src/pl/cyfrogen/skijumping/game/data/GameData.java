package pl.cyfrogen.skijumping.game.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.cyfrogen.skijumping.common.interfaces.OnEnd;
import pl.cyfrogen.skijumping.data.HillSetup;
import pl.cyfrogen.skijumping.data.JumperData;
import pl.cyfrogen.skijumping.data.WorldCupJumperData;

public class GameData {
    private final int rounds;
    private final List<HillSetup> hills;
    private final OnEnd onEnd;
    List<WorldCupJumperData> jumpers;
    List<WorldCupJumperData> startingJumpers;
    List<WorldCupJumperData> restJumpers;

    public GameData(List<HillSetup> hillsData, List<JumperData> jumperDataList, int rounds, OnEnd onEnd) {
        this.onEnd = onEnd;
        this.rounds = rounds;
        jumpers = new ArrayList<WorldCupJumperData>();
        hills = hillsData;
        int jumperNumber = 0;
        for (JumperData jumperData : jumperDataList) {
            jumpers.add(new WorldCupJumperData(jumperData, ++jumperNumber, hills.size(), rounds));
        }

        startingJumpers = new ArrayList<WorldCupJumperData>(jumpers);
        restJumpers = new ArrayList<WorldCupJumperData>();
    }

    public int getRounds() {
        return rounds;
    }

    public List<WorldCupJumperData> getStartingJumpers() {
        return startingJumpers;
    }

    public WorldCupJumperData popStartingJumper() {
        WorldCupJumperData startingJumper = startingJumpers.remove(0);
        restJumpers.add(startingJumper);
        return startingJumper;
    }

    public ArrayList<WorldCupJumperData> getStartingJumpersList(final int hill) {
        ArrayList<WorldCupJumperData> worldCupJumperData = new ArrayList<WorldCupJumperData>();
        worldCupJumperData.addAll(startingJumpers);
        Collections.sort(restJumpers, new Comparator<WorldCupJumperData>() {
            @Override
            public int compare(WorldCupJumperData d1, WorldCupJumperData d2) {
                return Integer.compare(d1.getPlace(hill), d2.getPlace(hill));
            }
        });
        worldCupJumperData.addAll(restJumpers);
        return worldCupJumperData;
    }

    public WorldCupJumperData getActualBestJumper(final int hill) {
        WorldCupJumperData max = jumpers.get(0);
        for(WorldCupJumperData jumperData : jumpers) {
            if(jumperData.getPoints(hill).getIntegerPoints() > max.getPoints(hill).getIntegerPoints()) {
                max = jumperData;
            }
        }
        return max;
    }

    public ArrayList<WorldCupJumperData> getSortedJumpers(final int hill) {
        ArrayList<WorldCupJumperData> worldCupJumperData = new ArrayList<WorldCupJumperData>(jumpers);
        Collections.sort(worldCupJumperData, new Comparator<WorldCupJumperData>() {
            @Override
            public int compare(WorldCupJumperData d1, WorldCupJumperData d2) {
                return Integer.compare(d1.getPlace(hill), d2.getPlace(hill));
            }
        });
        return worldCupJumperData;
    }

    public void updateJumperPositions(final int hill) {
        ArrayList<WorldCupJumperData> temp = new ArrayList<WorldCupJumperData>(jumpers);
        Collections.sort(temp, new Comparator<WorldCupJumperData>() {
            @Override
            public int compare(WorldCupJumperData d1, WorldCupJumperData d2) {
                return Integer.compare(d2.getPoints(hill).getIntegerPoints(), d1.getPoints(hill).getIntegerPoints());
            }
        });

        int lastIntegerPoints = 0;
        int lastPlace = 0;
        for (int i = 0; i < temp.size(); i++) {
            int integerPoints = temp.get(i).getPoints(hill).getIntegerPoints();

            int newPlace = i;
            if (integerPoints == lastIntegerPoints) {
                newPlace = lastPlace;
            }
            lastIntegerPoints = integerPoints;

            lastPlace = newPlace;

            if (temp.get(i).hasPlace(hill) || restJumpers.contains(temp.get(i))) {
                //don't update starting jumpers
                temp.get(i).setPlace(hill, newPlace + 1);
            }

        }
    }

    public void updateJumpersWorldCupPlace() {
        ArrayList<WorldCupJumperData> temp = new ArrayList<WorldCupJumperData>(jumpers);
        Collections.sort(temp, new Comparator<WorldCupJumperData>() {
            @Override
            public int compare(WorldCupJumperData d1, WorldCupJumperData d2) {
                return Integer.compare(d2.getWorldCupPoints(), d1.getWorldCupPoints());
            }
        });

        int lastIntegerPoints = 0;
        int lastPlace = 0;
        for (int i = 0; i < temp.size(); i++) {
            int integerPoints = temp.get(i).getWorldCupPoints();

            int newPlace = i;
            if (integerPoints == lastIntegerPoints) {
                newPlace = lastPlace;
            }
            lastIntegerPoints = integerPoints;

            lastPlace = newPlace;

            temp.get(i).setWorldCupPlace(newPlace + 1);


        }
    }

    public void nextRound(int hill) {
        ArrayList<WorldCupJumperData> sortedJumpers = getSortedJumpers(hill);
        Collections.reverse(sortedJumpers);
        startingJumpers = new ArrayList<WorldCupJumperData>(sortedJumpers);
        restJumpers.clear();
    }

    public void updateJumperWorldCupPoints(int hillNum) {
        for (WorldCupJumperData jumper : jumpers) {
            jumper.addWorldCupPoints(getWorldCupForPosition(jumper.getPlace(hillNum)));
        }
        updateJumpersWorldCupPlace();
    }

    private int getWorldCupForPosition(int place) {
        // <Position, Points>
        Map<Integer, Integer> pointsForPlace = new HashMap<Integer, Integer>();
        pointsForPlace.put(1, 100);
        pointsForPlace.put(2, 80);
        pointsForPlace.put(3, 60);
        pointsForPlace.put(4, 50);
        pointsForPlace.put(5, 45);
        pointsForPlace.put(6, 40);
        pointsForPlace.put(7, 36);
        pointsForPlace.put(8, 32);
        pointsForPlace.put(9, 29);
        pointsForPlace.put(10, 26);
        pointsForPlace.put(11, 24);
        pointsForPlace.put(12, 22);
        pointsForPlace.put(13, 20);
        pointsForPlace.put(14, 18);
        pointsForPlace.put(15, 16);
        pointsForPlace.put(16, 15);
        pointsForPlace.put(17, 14);
        pointsForPlace.put(18, 13);
        pointsForPlace.put(19, 12);
        pointsForPlace.put(20, 11);
        pointsForPlace.put(21, 10);
        pointsForPlace.put(22, 9);
        pointsForPlace.put(23, 8);
        pointsForPlace.put(24, 7);
        pointsForPlace.put(25, 6);
        pointsForPlace.put(26, 5);
        pointsForPlace.put(27, 4);
        pointsForPlace.put(28, 3);
        pointsForPlace.put(29, 2);
        pointsForPlace.put(30, 1);

        if (pointsForPlace.get(place) != null) {
            return pointsForPlace.get(place);
        }
        return 0;
    }

    public List<HillSetup> getHills() {
        return hills;
    }

    public OnEnd getOnEnd() {
        return onEnd;
    }
}
