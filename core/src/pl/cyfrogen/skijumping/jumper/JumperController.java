package pl.cyfrogen.skijumping.jumper;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import pl.cyfrogen.skijumping.debug.Logger;
import pl.cyfrogen.skijumping.game.GameWorld;
import pl.cyfrogen.skijumping.hill.HillModel;
import pl.cyfrogen.skijumping.hill.PhysicsConfiguration;
import pl.cyfrogen.skijumping.jumper.animation.AnimationFrame;
import pl.cyfrogen.skijumping.jumper.body.JumperBody;
import pl.cyfrogen.skijumping.jumper.judge.JudgeScores;

public class JumperController {
    private static final int STATE_STARTING_PLATFORM = 0;
    private static final int STATE_SLIDING_DOWN = 1;
    private static final int STATE_JUMPED = 2;
    private static final int STATE_PREPARING_TO_LAND = 3;
    private static final int STATE_LANDED = 4;
    private static final int STATE_CRASHED = 5;
    private static final int STATE_POST_LANDED = 6;


    public static final float PREPARE_TO_LAND_TIME = 0.3f;
    public static final float GETTING_TO_SLIDE_POSITION_TIME = 0.7f;
    public static final float PHYSIC_ENABLED_TIME = 0.3f;

    private final JumperBody jumperBody;
    private final GameWorld game;
    private final JumperPhysicObject jumperPhysicObject;
    Vector2 position = new Vector2();
    private int state = STATE_STARTING_PLATFORM;
    private float launchAnimationTime = 0.3f;
    private float launchVelocityTime = 0.03f;

    private float time;

    private float positionPercent = -1f;
    private float automaticMovePostionSpeed = -0.48f;
    private boolean addedVelocity;
    private float firstUpperTorsoAngle;
    private float firstSkiAngle;
    private AnimationFrame launchFrame;
    private PhysicsConfiguration physicsConfiguration;
    private final HillModel hillModel;
    private float angle;
    private AnimationFrame preLandingFrame;
    private float angleStep;
    private AnimationFrame preLandedFrame;
    private boolean crashOnNextFrame;
    private AnimationFrame prePostLandedFrame;
    private float hillAngle;
    private float launchStrength;
    private boolean telemark;
    private OnJumperSlidingPosition onJumperHasSlidingPosition;
    private boolean physicAlreadyEnabled;
    private double flyingTime;

    public JumperController(GameWorld game, PhysicsConfiguration physicsConfiguration, HillModel hillModel, JumperPhysicObject jumperPhysicObject, JumperBody jumperBody) {
        this.game = game;
        this.physicsConfiguration = physicsConfiguration;
        this.hillModel = hillModel;
        this.jumperBody = jumperBody;
        this.jumperPhysicObject = jumperPhysicObject;

        AnimationFrame slidingFrame = getStartGateFrame();


        jumperBody.getUpperTorso().reset();
        jumperBody.getLeftSki().reset();
        jumperBody.getRightSki().reset();

        jumperBody.setFrame(slidingFrame);
    }

    public void draw(ShapeRenderer shapeRenderer) {
        //body.draw(shapeRenderer);
    }

    public void setPosition(Vector2 position) {
        this.position = position.cpy();
    }

    public void update(float deltaTime) {
        setPosition(jumperPhysicObject.getPosition());
        Vector2 position = getPhysicObject().getPosition().cpy();
        jumperBody.setPosition(position);
        jumperBody.update(deltaTime);

        if (crashOnNextFrame && state != STATE_CRASHED) {
            crashOnNextFrame = false;
            jumperBody.getUpperTorso().generateBox2DBody(jumperBody.getPosition(), jumperBody.getUpperTorso().getMove()
                    , game.getWorld(), null, jumperPhysicObject.getLinearVelocity());
            jumperBody.setBox2dPhysic();
            state = STATE_CRASHED;

        }

        jumperBody.getUpperTorso().reset();
        jumperBody.getLeftSki().reset();
        jumperBody.getRightSki().reset();

        if (state == STATE_STARTING_PLATFORM) {


        } else if (state == STATE_JUMPED) {
            movePosition(automaticMovePostionSpeed * deltaTime);

            time += deltaTime;
            if (GameWorld.DEBUG) {
                if (time > launchVelocityTime && !addedVelocity && !isOnInRun()) {
                    Logger.debug("JUMPING WITH VELO " + jumperPhysicObject.getLinearVelocity().len());
                    addedVelocity = true;
                    Vector2 velocityAdd = new Vector2(0, physicsConfiguration.getTakeoffVelocity() * launchStrength);
                    jumperBody.jumperLaunched();
                    if (physicsConfiguration.isTakeoffVelocityPerpendicular())
                        velocityAdd.rotateRad(jumperPhysicObject.getLinearVelocity().angleRad());
                    game.getJumperListener().jumperJumped(jumperPhysicObject.getLinearVelocity(),
                            jumperPhysicObject.getLinearVelocity().cpy().add(velocityAdd));
                    jumperPhysicObject.setLinearVelocity(jumperPhysicObject.getLinearVelocity().x + velocityAdd.x, jumperPhysicObject.getLinearVelocity().y + velocityAdd.y);
                    setAngle(0);
                    game.onTakeoff();
                }
            } else {
                if (time > launchVelocityTime && !addedVelocity) {
                    addedVelocity = true;
                    if (isOnInRun()) {
                        Vector2 velocityAdd = new Vector2(0, physicsConfiguration.getTakeoffVelocity() * launchStrength);
                        jumperBody.jumperLaunched();
                        if (physicsConfiguration.isTakeoffVelocityPerpendicular())
                            velocityAdd.rotateRad(jumperPhysicObject.getLinearVelocity().angleRad());
                        game.getJumperListener().jumperJumped(jumperPhysicObject.getLinearVelocity(),
                                jumperPhysicObject.getLinearVelocity().cpy().add(velocityAdd));
                        jumperPhysicObject.setLinearVelocity(jumperPhysicObject.getLinearVelocity().x + velocityAdd.x, jumperPhysicObject.getLinearVelocity().y + velocityAdd.y);
                        setAngle(0);
                        game.onTakeoff();
                    }
                }
            }

            float percent = Math.min(time / launchAnimationTime, 1f);


            AnimationFrame frame = launchFrame.cpy().lerp(getFlyingFrame(positionPercent), percent);
            jumperBody.setFrame(frame);

            position.set(0, 0);
        } else if (state == STATE_SLIDING_DOWN) {
            AnimationFrame slidingFrame = getSlidingFrame();
            slidingFrame.upperTorsoAngle.set(angle - MathUtils.PI * 0.5f);
            slidingFrame.leftSkiAngle.set(angle);
            slidingFrame.rightSkiAngle.set(angle);

            time += deltaTime;
            if (time > PHYSIC_ENABLED_TIME && !physicAlreadyEnabled) {
                physicAlreadyEnabled = true;
                onJumperHasSlidingPosition.enablePhysic();
            }
            float percent = time / GETTING_TO_SLIDE_POSITION_TIME;
            percent = Interpolation.smoother.apply(percent);
            if (percent > 1f) {
                percent = 1f;
            }

            AnimationFrame frame = getStartGateFrame().cpy().lerp(slidingFrame, percent);
            jumperBody.setFrame(frame);


        } else if (state == STATE_PREPARING_TO_LAND) {
            float nextAngle = getNextJumperAngle();

            angle = MathUtils.lerp(angle, nextAngle, 0.1f);

            time += deltaTime;
            float percent = time / PREPARE_TO_LAND_TIME;
            percent = Interpolation.smoother.apply(percent);
            if (percent > 1f) {
                percent = 1f;
            }

            jumperBody.setFrame(preLandingFrame.cpy().lerp(getPreLandingFrame(), percent));

            jumperBody.getLeftSki().setAngle(jumperBody.getLeftSki().getAngle() + angle);
            jumperBody.getRightSki().setAngle(jumperBody.getRightSki().getAngle() + angle);

            jumperBody.getUpperTorso().setAngle(jumperBody.getUpperTorso().getAngle() + angle);

        } else if (state == STATE_LANDED) {
            float nextAngle = getNextJumperAngle();
            hillAngle = nextAngle;

            angle = MathUtils.lerp(angle, nextAngle, 0.1f);

            time += deltaTime;
            float LANDING_TIME = 0.25f;
            float percent = time / LANDING_TIME;
            if (percent > 1f) percent = 1f;
            percent = Interpolation.smooth.apply(percent);

            AnimationFrame frame = telemark ? getTelemarkFrame() : getLandingFrame();
            frame.upperTorsoAngle.add(angle);
            frame.leftSkiAngle.add(angle);
            frame.rightSkiAngle.add(angle);

            jumperBody.setFrame(preLandedFrame.cpy().lerp(frame, percent));

            if (time > 1.5f) {
                setPostLanded();
            }

        } else if (state == STATE_POST_LANDED) {
            float nextAngle = getNextJumperAngle();

            angle = MathUtils.lerp(angle, nextAngle, 0.1f);

            time += deltaTime;
            float STANDING_TIME = 1f;
            float percent = time / STANDING_TIME;
            if (percent > 1f) percent = 1f;
            percent = Interpolation.smooth.apply(percent);

            AnimationFrame frame = getAfterLandingFrame();
            frame.upperTorsoAngle.add(angle);
            frame.leftSkiAngle.add(angle);
            frame.rightSkiAngle.add(angle);

            jumperBody.setFrame(prePostLandedFrame.cpy().lerp(frame, percent));

        } else if (state == STATE_CRASHED) {
            jumperBody.setAsBox2d();

        }
    }

    private Float getNextJumperAngle(List<Vector2> vertices) {
        Vector2 jumperPosition = jumperPhysicObject.getPosition();
        int i = 0;

        for (i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).x > jumperPosition.x) break;
        }
        if (i <= 1) {
            return null;
        }
        if (i >= vertices.size() - 2) {
            return null;
        }

        float percentage = (jumperPosition.x - vertices.get(i - 1).x) /
                (vertices.get(i).x - vertices.get(i - 1).x);

        float prevAngle = vertices.get(i - 1).cpy().sub(vertices.get(i - 2)).angleRad();
        float nextAngle = vertices.get(i + 1).cpy().sub(vertices.get(i)).angleRad();

        return prevAngle + (nextAngle - prevAngle) * percentage;
    }

    private float getNextJumperAngle() {
        Float angle1 = getNextJumperAngle(hillModel.getInRunVertices());
        Float angle2 = getNextJumperAngle(hillModel.getOutRunVertices());
        if (angle1 != null) return angle1;
        if (angle2 != null) return angle2;
        return 0;
    }


    private float getAngle(float x1, float x2, float percent) {
        return MathUtils.lerp(x1, x2, MathUtils.clamp(percent, 0, 1f));
    }

    public void movePosition(float deltaY) {
        positionPercent += deltaY;
        if (positionPercent > 1f) positionPercent = 1f;
        if (positionPercent < -1f) positionPercent = -1f;
    }

    public void setLaunch(float launchStrength) {
        time = 0;
        this.launchStrength = launchStrength;
        state = STATE_JUMPED;
        firstUpperTorsoAngle = jumperBody.getUpperTorso().getAngle() * MathUtils.radDeg;
        firstSkiAngle = jumperBody.getLeftSki().getAngle() * MathUtils.radDeg;
        launchFrame = jumperBody.dumpCurrentFrame();

    }

    public boolean isLaunched() {
        return state >= STATE_JUMPED && addedVelocity;
    }

    public float getPositionPercent() {
        return positionPercent;
    }

    public void setAngle(float angle) {
        this.angle = angle;

    }

    public float getAngle() {
        return angle;
    }

    public AnimationFrame getStartGateFrame() {
        AnimationFrame frame = new AnimationFrame();
        frame.neckAngle.setDeg(0);
        frame.upperTorsoAngle.setDeg(-10);
        frame.lowerTorsoAngle.setDeg(0);
        frame.leftUpperLegAngle.setDeg(70);
        frame.leftLowerLegAngle.setDeg(-110);
        frame.leftAnkleAngle.setDeg(25);
        frame.leftBootMeshPartAngle.setDeg(4);
        frame.leftShoulderAngle.setDeg(-5);
        frame.leftForearmAngle.setDeg(20);
        frame.leftWristAngle.setDeg(20);
        frame.leftSkiAngle.setDeg(-30);

        frame.setRightAsLeft();
        frame.leftUpperLegAngle.set(frame.leftUpperLegAngle.getAngle() + 0.2f);
        frame.leftLowerLegAngle.set(frame.leftLowerLegAngle.getAngle() - 0.2f);

        return frame;
    }

    public AnimationFrame getSlidingFrame() {
        AnimationFrame frame = new AnimationFrame();
        frame.neckAngle.setDeg(45);
        frame.upperTorsoAngle.setDeg(-98);
        frame.lowerTorsoAngle.setDeg(14);
        frame.leftUpperLegAngle.setDeg(153);
        frame.leftLowerLegAngle.setDeg(-98);
        frame.leftAnkleAngle.setDeg(25);
        frame.leftBootMeshPartAngle.setDeg(4);
        frame.leftShoulderAngle.setDeg(8);
        frame.leftForearmAngle.setDeg(0);
        frame.leftWristAngle.setDeg(0);
        frame.leftSkiAngle.setDeg(0);

        frame.setRightAsLeft();
        frame.setRightAndLeftSideDifferent();
        return frame;
    }

    public AnimationFrame getOptimalFlyingFrame() {
        return getFlyingFrame(0);
    }

    public AnimationFrame getFlyingFrame(float positionPercent) {
        float modifier = 1.5f;
        AnimationFrame frame = new AnimationFrame();
        frame.neckAngle.setDeg(39);
        frame.upperTorsoAngle.setDeg(-87 - positionPercent * 10 * modifier);
        frame.lowerTorsoAngle.setDeg(0);
        frame.leftLowerLegAngle.setDeg(0);
        frame.leftAnkleAngle.setDeg(39);
        frame.leftBootMeshPartAngle.setDeg(33);
        frame.leftShoulderAngle.setDeg(0);
        frame.leftForearmAngle.setDeg(0);
        frame.leftWristAngle.setDeg(0);
        frame.leftSkiAngle.setDeg(12 + positionPercent * 5 * modifier);
        frame.setRightAsLeft();
        frame.setRightAndLeftSideDifferent();

        frame.leftUpperLegAngle.setDeg(11 - positionPercent * 10 * modifier);
        float angle = frame.leftUpperLegAngle.getAngle();
        frame.leftUpperLegAngle.set(angle);
        frame.rightUpperLegAngle.set(angle + 0.2f);
        frame.leftAnkleAngle.setDeg(39);
        frame.rightAnkleAngle.setDeg(39);
        frame.leftLowerLegAngle.setDeg(0 + MathUtils.radiansToDegrees * 0.4f);
        frame.rightLowerLegAngle.setDeg(0);
        frame.rightSkiAngle.setDeg(9 + positionPercent * 5 * modifier);
        frame.leftSkiAngle.setDeg(13 + positionPercent * 5 * modifier);

        return frame;
    }

    public AnimationFrame getPreLandingFrame() {
        AnimationFrame frame = new AnimationFrame();
        frame.neckAngle.setDeg(0);
        frame.upperTorsoAngle.setDeg(-30);
        frame.lowerTorsoAngle.setDeg(0);
        frame.leftUpperLegAngle.setDeg(40);
        frame.leftLowerLegAngle.setDeg(-55);
        frame.leftAnkleAngle.setDeg(33);
        frame.leftBootMeshPartAngle.setDeg(11);
        frame.leftShoulderAngle.setDeg(-13);
        frame.leftForearmAngle.setDeg(0);
        frame.leftWristAngle.setDeg(0);
        frame.leftSkiAngle.setDeg(0);
        frame.setRightAsLeft();
        frame.setRightAndLeftSideDifferent();
        return frame;
    }

    public AnimationFrame getLandingFrame() {
        AnimationFrame frame = new AnimationFrame();
        frame.upperTorsoAngle.setDeg(-28);
        frame.lowerTorsoAngle.setDeg(14);
        frame.leftUpperLegAngle.setDeg(133);
        frame.leftLowerLegAngle.setDeg(-148);
        frame.leftAnkleAngle.setDeg(25);
        frame.leftBootMeshPartAngle.setDeg(4);
        frame.leftShoulderAngle.setDeg(8);
        frame.leftForearmAngle.setDeg(0);
        frame.leftWristAngle.setDeg(0);
        frame.leftSkiAngle.setDeg(0);
        frame.setRightAsLeft();
        //  frame.setRightAndLeftSideDifferent();
        return frame;
    }

    public AnimationFrame getTelemarkFrame() {
        AnimationFrame frame = new AnimationFrame();
        frame.neckAngle.setDeg(0);
        frame.upperTorsoAngle.setDeg(-22);
        frame.lowerTorsoAngle.setDeg(6);
        frame.leftUpperLegAngle.setDeg(72);
        frame.leftLowerLegAngle.setDeg(-120);
        frame.leftAnkleAngle.setDeg(24);
        frame.leftBootMeshPartAngle.setDeg(40);

        frame.rightUpperLegAngle.setDeg(102);
        frame.rightLowerLegAngle.setDeg(-86);
        frame.rightAnkleAngle.setDeg(0);
        frame.rightBootMeshPartAngle.setDeg(0);

        frame.leftShoulderAngle.setDeg(-13);
        frame.leftForearmAngle.setDeg(0);
        frame.leftWristAngle.setDeg(0);
        frame.leftSkiAngle.setDeg(0);

        frame.rightShoulderAngle.setDeg(-13);
        frame.rightForearmAngle.setDeg(0);
        frame.rightWristAngle.setDeg(0);
        frame.rightSkiAngle.setDeg(0);
        return frame;
    }

    public AnimationFrame getAfterLandingFrame() {
        AnimationFrame frame = new AnimationFrame();
        frame.leftUpperLegAngle.setDeg(20);
        frame.rightUpperLegAngle.setDeg(20);
        frame.leftLowerLegAngle.setDeg(-20);
        frame.rightLowerLegAngle.setDeg(-20);
        frame.leftSkiAngle.setDeg(-5);
        frame.rightSkiAngle.setDeg(5);

        return frame;
    }

    public double getGoodFlyingPositionPercent() {
        float absPosition = Math.abs(getPositionPercent());

        if (state == STATE_JUMPED) {
            return physicsConfiguration.getPerformanceFunction().valueAt(absPosition);
        } else {
            return 0;
        }
    }

    public boolean isLanding() {
        return state == STATE_PREPARING_TO_LAND;
    }

    public void setLanding() {
        state = STATE_PREPARING_TO_LAND;
        time = 0;
        angleStep = angle;
        jumperBody.getUpperTorso().reset();
        jumperBody.getLeftSki().reset();
        jumperBody.getRightSki().reset();
        preLandingFrame = jumperBody.dumpCurrentFrame();
    }

    public void setLandedWithCrash(float distance) {
        game.onTakeoff();
        state = STATE_LANDED;
        game.setHillFriction(1f);
        crashOnNextFrame = true;
        int basePoints = 100;
        int[] points = new int[5];
        for (int i = 0; i < points.length; i++) {
            int r = MathUtils.random(5);
            points[i] = basePoints - 5 * (r - 3);
        }

        game.getJumperListener().jumperLanded(distance, true, jumperPhysicObject.getLinearVelocity(), telemark, time, new JudgeScores(points));

    }

    public void setLanded(float distance) {
        game.onTakeoff();
        if (state == STATE_PREPARING_TO_LAND && !game.getJumperListener().shouldCrash(jumperPhysicObject.getLinearVelocity(), telemark, getNextJumperAngle(), time)) {

            float differenceFromOptimalTime = Math.abs(time - PREPARE_TO_LAND_TIME);
            float differenceToLoseHalfPoint = 0.1f;
            int losePointsLandingTime = 5 * (int) (differenceFromOptimalTime / differenceToLoseHalfPoint);
            if (losePointsLandingTime > 50) losePointsLandingTime = 50;

            double minDistanceToGetPoints = hillModel.getConstructionPointPathLength() * 3 / 4f;
            double maxDistanceToGetPoints = (hillModel.getConstructionPointPathLength() + hillModel.getHillSizePathLength()) / 2f;

            float percentage = (float) ((distance - minDistanceToGetPoints) / (maxDistanceToGetPoints - minDistanceToGetPoints));
            percentage = MathUtils.clamp(percentage, 0f, 1f);
            percentage = 1 - percentage;

            int pointsToLoseFromDistance = ((int) percentage * (10)) * 5;

            int pointsToLoseFromNoTelemark = telemark ? 0 : 30;

            int basePoints = 200 - losePointsLandingTime - pointsToLoseFromDistance - pointsToLoseFromNoTelemark;
            int[] points = new int[5];
            for (int i = 0; i < points.length; i++) {
                int r = MathUtils.random(3);
                points[i] = basePoints + 5 * (r - 2);
                if (points[i] > 200)
                    points[i] = 200;
            }

            game.getJumperListener().jumperLanded(distance, false, jumperPhysicObject.getLinearVelocity(), telemark, time, new JudgeScores(points));
            state = STATE_LANDED;
            time = 0;
            game.setHillFriction(0.1f);
            jumperBody.getUpperTorso().reset();
            jumperBody.getLeftSki().reset();
            jumperBody.getRightSki().reset();
            preLandedFrame = jumperBody.dumpCurrentFrame();

        } else {
            setLandedWithCrash(distance);
        }
    }

    private void setPostLanded() {
        state = STATE_POST_LANDED;
        time = 0;
        game.setHillFriction(1f);
        game.getPhysics().setKineticFrictionCoefficient(1f);
        jumperBody.getUpperTorso().reset();
        jumperBody.getLeftSki().reset();
        jumperBody.getRightSki().reset();
        prePostLandedFrame = jumperBody.dumpCurrentFrame();
    }

    public boolean isLanded() {
        return state == STATE_LANDED || state == STATE_CRASHED;
    }

    public boolean isFlying() {
        return state == STATE_JUMPED;
    }

    public boolean isSliding() {
        return state == STATE_SLIDING_DOWN;
    }

    public void startJumper(OnJumperSlidingPosition onJumperSlidingPosition) {
        this.onJumperHasSlidingPosition = onJumperSlidingPosition;
        physicAlreadyEnabled = false;
        this.state = STATE_SLIDING_DOWN;
        time = 0;
    }

    public void updateLanding(boolean telemark) {
        this.telemark = telemark;
    }

    public int getState() {
        return state;
    }

    public boolean isPreparingToLand() {
        return state == STATE_PREPARING_TO_LAND;
    }

    public void dispose() {
        jumperBody.dispose();
        jumperBody.getUpperTorso().destroyBox2DBodyIfGenerated(game.getWorld());
    }

    public boolean isOnInRun() {
        return jumperPhysicObject.getPosition().x <
                hillModel.getInRunVertices().get(hillModel.getInRunVertices().size() - 1).x;
    }

    public boolean isOnStartingPlatform() {
        return state == STATE_STARTING_PLATFORM;
    }

    public double getFlyingTime() {
        return flyingTime;
    }

    public void setFlyingTime(double flyingTime) {
        this.flyingTime = flyingTime;
    }

    public JumperPhysicObject getPhysicObject() {
        return jumperPhysicObject;
    }

    public JumperBody getJumperBody() {
        return jumperBody;
    }
}
