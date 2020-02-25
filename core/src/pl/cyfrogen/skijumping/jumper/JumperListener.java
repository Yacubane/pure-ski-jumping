package pl.cyfrogen.skijumping.jumper;

import com.badlogic.gdx.math.Vector2;

import pl.cyfrogen.skijumping.jumper.judge.JudgeScores;

public interface JumperListener {
    void jumperStartedRide();
    void jumperJumped(Vector2 speedBeforeJump, Vector2 speedAfterJump);
    boolean shouldCrash(Vector2 speed, boolean telemark, float hillAngle,  float timeFromPreparingToLand);
    void jumperLanded(float distance, boolean crashed, Vector2 speed, boolean telemark, float timeFromPreparingToLand, JudgeScores points);
}
