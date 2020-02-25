package pl.cyfrogen.skijumping.jumper.animation;

import com.badlogic.gdx.math.MathUtils;

import pl.cyfrogen.skijumping.jumper.body.JumperBody;

public class AnimationFrame {

    public Angle upperTorsoAngle = new Angle();
    public Angle lowerTorsoAngle = new Angle();
    public Angle leftUpperLegAngle = new Angle();
    public Angle leftLowerLegAngle = new Angle();
    public Angle leftAnkleAngle = new Angle();
    public Angle leftBootMeshPartAngle = new Angle();
    public Angle neckAngle = new Angle();
    public Angle leftShoulderAngle = new Angle();
    public Angle leftForearmAngle = new Angle();
    public Angle leftWristAngle = new Angle();
    public Angle leftSkiAngle = new Angle();
    public Angle rightUpperLegAngle = new Angle();
    public Angle rightLowerLegAngle = new Angle();
    public Angle rightAnkleAngle = new Angle();
    public Angle rightBootMeshPartAngle = new Angle();
    public Angle rightShoulderAngle = new Angle();
    public Angle rightForearmAngle = new Angle();
    public Angle rightWristAngle = new Angle();
    public Angle rightSkiAngle = new Angle();

    public Angle[] angles = new Angle[]{upperTorsoAngle, lowerTorsoAngle, leftUpperLegAngle, leftLowerLegAngle,
            leftAnkleAngle, leftBootMeshPartAngle, neckAngle, leftShoulderAngle, leftForearmAngle,
            leftWristAngle, leftSkiAngle, rightUpperLegAngle, rightLowerLegAngle, rightAnkleAngle,
            rightBootMeshPartAngle, rightShoulderAngle, rightForearmAngle, rightWristAngle, rightSkiAngle};

    public Angle[] getAngles() {
        return angles;
    }

    public void setRightAsLeft() {
        rightUpperLegAngle.set(leftUpperLegAngle);
        rightLowerLegAngle.set(leftLowerLegAngle);
        rightAnkleAngle.set(leftAnkleAngle);
        rightBootMeshPartAngle.set(leftBootMeshPartAngle);
        rightShoulderAngle.set(leftShoulderAngle);
        rightForearmAngle.set(leftForearmAngle);
        rightWristAngle.set(leftWristAngle);
        rightSkiAngle.set(leftSkiAngle);
    }

    public void setRightAndLeftSideDifferent() {
        leftUpperLegAngle.set(leftUpperLegAngle.getAngle() + 0.2f);
        leftLowerLegAngle.set(leftLowerLegAngle.getAngle() - 0.2f);
        rightUpperLegAngle.set(rightUpperLegAngle.getAngle() - 0.2f);
        rightLowerLegAngle.set(rightLowerLegAngle.getAngle() + 0.2f);
        leftShoulderAngle.set(leftShoulderAngle.getAngle() - 0.2f);
    }

    public void apply(JumperBody jumperBody) {
        jumperBody.getUpperTorso().setAngle(upperTorsoAngle.getAngle());
        jumperBody.getLowerTorso().setAngle(lowerTorsoAngle.getAngle());
        jumperBody.getLeftUpperLeg().setAngle(leftUpperLegAngle.getAngle());
        jumperBody.getLeftLowerLeg().setAngle(leftLowerLegAngle.getAngle());
        jumperBody.getLeftAnkle().setAngle(leftAnkleAngle.getAngle());
        jumperBody.getLeftBootBodyPart().setAngle(leftBootMeshPartAngle.getAngle());
        jumperBody.getLeftShoulder().setAngle(leftShoulderAngle.getAngle());
        jumperBody.getLeftForearm().setAngle(leftForearmAngle.getAngle());
        jumperBody.getLeftWrist().setAngle(leftWristAngle.getAngle());
        jumperBody.getLeftSki().setAngle(leftSkiAngle.getAngle());
        jumperBody.getRightUpperLeg().setAngle(rightUpperLegAngle.getAngle());
        jumperBody.getRightLowerLeg().setAngle(rightLowerLegAngle.getAngle());
        jumperBody.getRightAnkle().setAngle(rightAnkleAngle.getAngle());
        jumperBody.getRightBootBodyPart().setAngle(rightBootMeshPartAngle.getAngle());
        jumperBody.getRightShoulder().setAngle(rightShoulderAngle.getAngle());
        jumperBody.getRightForearm().setAngle(rightForearmAngle.getAngle());
        jumperBody.getRightWrist().setAngle(rightWristAngle.getAngle());
        jumperBody.getRightSki().setAngle(rightSkiAngle.getAngle());
        jumperBody.getNeck().setAngle(neckAngle.getAngle());
    }

    public void getInfo(JumperBody jumperBody) {
        upperTorsoAngle.set(jumperBody.getUpperTorso().getLocalAngle());
        lowerTorsoAngle.set(jumperBody.getLowerTorso().getLocalAngle());
        leftUpperLegAngle.set(jumperBody.getLeftUpperLeg().getLocalAngle());
        leftLowerLegAngle.set(jumperBody.getLeftLowerLeg().getLocalAngle());
        leftAnkleAngle.set(jumperBody.getLeftAnkle().getLocalAngle());
        leftBootMeshPartAngle.set(jumperBody.getLeftBootBodyPart().getLocalAngle());
        leftShoulderAngle.set(jumperBody.getLeftShoulder().getLocalAngle());
        leftWristAngle.set(jumperBody.getLeftForearm().getLocalAngle());
        leftWristAngle.set(jumperBody.getLeftWrist().getLocalAngle());
        leftSkiAngle.set(jumperBody.getLeftSki().getLocalAngle());
        rightUpperLegAngle.set(jumperBody.getRightUpperLeg().getLocalAngle());
        rightLowerLegAngle.set(jumperBody.getRightLowerLeg().getLocalAngle());
        rightAnkleAngle.set(jumperBody.getRightAnkle().getLocalAngle());
        rightBootMeshPartAngle.set(jumperBody.getRightBootBodyPart().getLocalAngle());
        rightShoulderAngle.set(jumperBody.getRightShoulder().getLocalAngle());
        rightForearmAngle.set(jumperBody.getRightForearm().getLocalAngle());
        rightWristAngle.set(jumperBody.getRightWrist().getLocalAngle());
        rightSkiAngle.set(jumperBody.getRightSki().getLocalAngle());
        neckAngle.set(jumperBody.getNeck().getLocalAngle());

    }

    public class Angle {
        float angle;

        public Angle() {

        }

        public void set(float angle) {
            this.angle = angle;
        }

        public void set(Angle angle) {
            this.angle = angle.getAngle();
        }

        public float getAngle() {
            return angle;
        }

        public void setDeg(double angle) {
            this.angle = (float) (angle * MathUtils.degRad);
        }

        public void addDeg(float angle) {
            this.angle += (angle * MathUtils.degRad);

        }

        public void add(float angle) {
            this.angle += angle;
        }
    }


    public AnimationFrame cpy() {
        AnimationFrame frame = new AnimationFrame();
        for (int i = 0; i < getAngles().length; i++) {
            frame.getAngles()[i].set(getAngles()[i]);
        }
        return frame;
    }

    public AnimationFrame lerp(AnimationFrame frame, float lerp) {
        for (int i = 0; i < getAngles().length; i++) {
            getAngles()[i].set(MathUtils.lerp(getAngles()[i].getAngle(), frame.getAngles()[i].getAngle(), lerp));
        }
        return this;
    }
}
