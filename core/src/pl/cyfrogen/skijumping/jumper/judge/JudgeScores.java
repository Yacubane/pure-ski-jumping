package pl.cyfrogen.skijumping.jumper.judge;

import java.util.ArrayList;
import java.util.List;

public class JudgeScores {

    List<JudgeScore> judgeScores = new ArrayList<JudgeScore>();

    public JudgeScores(int[] points) {
        for (int point : points) {
            judgeScores.add(new JudgeScore(point));
        }

        JudgeScore minJudgeScore = judgeScores.get(0);
        JudgeScore maxJudgeScore = judgeScores.get(0);

        for (JudgeScore judgeScore : judgeScores) {
            judgeScore.setActive(true);
            if (judgeScore.getPoints() < minJudgeScore.getPoints()) {
                minJudgeScore = judgeScore;
            }
            if (judgeScore.getPoints() > maxJudgeScore.getPoints()) {
                maxJudgeScore = judgeScore;
            }
        }

        if(minJudgeScore == maxJudgeScore) { //every score is same
            maxJudgeScore = judgeScores.get(1);
        }

        minJudgeScore.setActive(false);
        maxJudgeScore.setActive(false);
    }

    public int size() {
        return 5;
    }

    public int getFinalPoints() {
        int points = 0;
        for (JudgeScore judgeScore : judgeScores) {
            if (judgeScore.isActive()) points += judgeScore.getPoints();
        }
        return points;
    }

    public JudgeScore getJudgeScore(int i) {
        return judgeScores.get(i);
    }
}
