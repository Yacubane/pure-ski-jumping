package pl.cyfrogen.skijumping.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.ArrayList;


/**
 * Encapsulates mesh vertices for static vertices, joint vertices and children BodyParts.
 */
public class BodyPart {
    ArrayList<BodyPart> childrenBodyParts = new ArrayList<BodyPart>();
    private final Vector2 relativePosition;
    private Vector2 orginalOrigin;
    private MeshVertex[] points;
    private Vector2 origin;
    private Vector2 tmpOrigin = new Vector2();
    ArrayList<Vector2> orientationPoints = new ArrayList<Vector2>();
    ArrayList<Vector2> orginalOrientationPoints = new ArrayList<Vector2>();

    private boolean dirty;
    private float angle;
    private float localAngle;
    private MeshVertex[][] staticVertices;
    private MeshVertex[][] jointVertices;
    private Vector2[] box2dBodyVerts;
    private Body body;
    private float bodyAngle;
    private boolean angleLimit;
    private float angleLowerLimit;
    private float angleUpperLimit;
    private String name;
    private Vector2 move;
    private Joint joint;

    public BodyPart() {
        this.relativePosition = new Vector2(0, 0);

    }

    public void build() {
        if (jointVertices == null)
            jointVertices = new MeshVertex[0][0]; //TODO - find better way to avoid null pointer
        if (staticVertices == null)
            staticVertices = new MeshVertex[0][0]; //TODO - find better way to avoid null pointer

        int pointsSize = 0;
        for (int i = 0; i < staticVertices.length; i++) pointsSize += staticVertices[i].length;
        for (int i = 0; i < jointVertices.length; i++) pointsSize += jointVertices[i].length;

        points = new MeshVertex[pointsSize];
        int i = 0;

        for (MeshVertex[] array : staticVertices) {
            for (MeshVertex meshVertex : array) {
                points[i++] = meshVertex;
            }
        }
        for (MeshVertex[] array : jointVertices) {
            for (MeshVertex meshVertex : array) {
                points[i++] = meshVertex;
            }
        }

    }

    public void setOrigin(Vector2 origin) {
        this.origin = new Vector2(origin);
        this.orginalOrigin = new Vector2(origin);


    }

    public void reset() {
        angle = 0;
        origin.set(orginalOrigin);
        dirty = false;
        relativePosition.set(0, 0);
        for (BodyPart bodyPart : childrenBodyParts) {
            bodyPart.reset();
        }
    }

    private void moveRelative(Vector2 move) {
        this.relativePosition.add(move);
        for (BodyPart bodyPart : childrenBodyParts) {
            bodyPart.moveRelative(move);
        }
    }

    public void rotateAround(Vector2 point, Vector2 rotationOrigin, float rotationDegress) {
        point.sub(rotationOrigin);
        VectorUtils.fastRotateRad(point, rotationDegress).add(rotationOrigin);
    }

    public void setRelativePosition(Vector2 position) {
        Vector2 move = position.sub(this.relativePosition);
        moveRelative(move);

    }

    public void generateBox2DBody(Vector2 position, Vector2 move, World world, Body prevBody, Vector2 velocity) {
        Vector2 positionCombined = position.cpy().add(move);
        bodyAngle = getAngle();
        for (Vector2 point : box2dBodyVerts) {
            point.add(relativePosition).sub(origin);
            VectorUtils.fastRotateRad(point, angle);
            point.add(origin);
            point.add(move);
        }

        Vector2 realOrigin = origin.cpy().add(positionCombined);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);
        body.setLinearVelocity(velocity);

        PolygonShape shape = new PolygonShape();

        shape.set(box2dBodyVerts);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.01f;
        fixtureDef.restitution = 0.0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.filter.categoryBits = 0x0002;
        fixtureDef.filter.maskBits = 0x0001;

        Fixture fixture = body.createFixture(fixtureDef);

        shape.dispose();

        if (prevBody != null) {
            RevoluteJointDef def = new RevoluteJointDef();
            def.initialize(prevBody, body, new Vector2(realOrigin));
            if (angleLimit) {
                def.enableLimit = true;
                def.upperAngle = angleUpperLimit - localAngle;
                def.lowerAngle = angleLowerLimit - localAngle;
            } else {
                def.enableLimit = true;
                def.upperAngle = 0;
                def.lowerAngle = 0;
            }


            joint = world.createJoint(def);
        }

        for (BodyPart bodyPart : childrenBodyParts) {
            bodyPart.generateBox2DBody(position, move, world, body, velocity);
        }
    }

    public void setAngle(float angle) {
        errorIfDirty();
        this.localAngle = angle;
        this.angle = angle;

    }

    public void destroyBox2DBodyIfGenerated(World world) {
        if (body != null) {
            world.destroyBody(body);
            body = null;
        }
        for (BodyPart bodyPart : childrenBodyParts) {
            bodyPart.destroyBox2DBodyIfGenerated(world);
        }
    }

    public void calculate(Vector2 move) {
        setDirty();
        float rotation = angle - localAngle;
        for (MeshVertex point : points) {
            point.reset();
            point.getPoint().add(relativePosition).sub(origin);
            VectorUtils.fastRotateRad(point.getPoint(), rotation);
            point.getPoint().add(origin);
            point.rotateAround(origin, localAngle);
            point.getPoint().add(move);
        }
        for (int i = 0; i < orginalOrientationPoints.size(); i++) {
            Vector2 orientationPoint = orientationPoints.get(i);
            orientationPoint.set(orginalOrientationPoints.get(i));
            orientationPoint.add(relativePosition).sub(origin);
            VectorUtils.fastRotateRad(orientationPoint, angle);
            orientationPoint.add(origin);
            orientationPoint.add(move);

        }

        for (BodyPart bodyPart : childrenBodyParts) {
            bodyPart.rotateAround(origin, localAngle);
            bodyPart.calculate(move);
        }

    }

    public void calculateAndManipulate(Vector2 move, PointManipulation pointManipulation) {
        setDirty();
        float rotation = angle - localAngle;
        for (MeshVertex point : points) {
            point.reset();
            pointManipulation.manipulatePoint(point.getPoint());
            point.getPoint().add(relativePosition).sub(origin);
            VectorUtils.fastRotateRad(point.getPoint(), rotation);
            point.getPoint().add(origin);
            point.rotateAround(origin, localAngle);
            point.getPoint().add(move);
        }
        for (int i = 0; i < orginalOrientationPoints.size(); i++) {
            Vector2 orientationPoint = orientationPoints.get(i);
            orientationPoint.set(orginalOrientationPoints.get(i));
            orientationPoint.add(relativePosition).sub(origin);
            VectorUtils.fastRotateRad(orientationPoint, angle);
            orientationPoint.add(origin);
            orientationPoint.add(move);

        }

        for (BodyPart bodyPart : childrenBodyParts) {
            bodyPart.rotateAround(origin, localAngle);
            bodyPart.calculate(move);
        }

    }

    private void rotateAround(Vector2 origin, float angle) {
        tmpOrigin.set(this.origin);
        this.origin.sub(origin);
        VectorUtils.fastRotateRad(this.origin, angle);
        this.origin.add(origin);
        Vector2 moveOrigin = tmpOrigin.sub(this.origin).scl(-1);
        relativePosition.add(moveOrigin);
        this.angle += angle;

        for (BodyPart bodyPart : childrenBodyParts) {
            bodyPart.rotateAround(origin, angle);
        }
    }

    public void addBodyPart(BodyPart part) {
        childrenBodyParts.add(part);
    }

    public void debugDraw(ShapeRenderer renderer) {
        renderer.setColor(Color.BLUE);
        for (BodyPart bodyPart : childrenBodyParts) {
            bodyPart.debugDraw(renderer);
        }
        renderer.setColor(Color.WHITE);

        for (int i = 0; i < staticVertices.length; i++) {
            drawLines(renderer, staticVertices[i]);

        }
        for (int i = 0; i < jointVertices.length; i++) {
            drawLines(renderer, jointVertices[i]);

        }
    }


    private void setDirty() {
        errorIfDirty();
        dirty = true;
    }

    private void errorIfDirty() {
        if (dirty) throw new GdxRuntimeException("Dirty body part :(");
    }

    public void drawLines(ShapeRenderer renderer, MeshVertex[] points) {
        for (int i = 1; i < points.length; i++) {
            renderer.line(points[i].getX(), points[i].getY(), points[i - 1].getX(), points[i - 1].getY());
        }
    }

    public void scale(float v) {
        for (MeshVertex point : points) {
            point.getOriginalPoint().scl(v);
        }
        for (Vector2 point : orginalOrientationPoints) {
            point.scl(v);
        }
        for (Vector2 point : orientationPoints) {
            point.scl(v);
        }
        orginalOrigin.scl(v);
        if (box2dBodyVerts != null)
            for (Vector2 bodyPart : box2dBodyVerts) {
                bodyPart.scl(v);
            }
        for (BodyPart bodyPart : childrenBodyParts) {
            bodyPart.scale(v);
        }

    }

    public void addOrientationPoint(Vector2 point) {
        orginalOrientationPoints.add(new Vector2(point));
        orientationPoints.add(new Vector2());
    }

    public MeshVertex[][] getJointVertices() {
        return jointVertices;
    }

    public void setJointVertices(MeshVertex[][] jointVertices) {
        this.jointVertices = jointVertices;
    }

    public ArrayList<Vector2> getOrientationPoints() {
        return orientationPoints;
    }

    public MeshVertex[][] getStaticVertices() {
        return staticVertices;
    }

    public void setStaticVertices(MeshVertex[][] staticVertices) {
        this.staticVertices = staticVertices;
    }

    public boolean isDirty() {
        return dirty;
    }

    public float getLocalAngle() {
        return localAngle;
    }

    public float getAngle() {
        return angle;
    }


    public void move(Vector2 move) {
        this.move = move;
        for (MeshVertex point : points) {
            point.getPoint().add(move);
        }
        for (int i = 0; i < orginalOrientationPoints.size(); i++) {
            orientationPoints.get(i).add(move);
        }

        for (BodyPart bodyPart : childrenBodyParts) {
            bodyPart.move(move);
        }
    }

    public void setAsBox2d() {
        setAsBox2d(0);
    }

    public void setAsBox2d(float prevAngle) {
        setAngle(body.getAngle() + bodyAngle - prevAngle);
        for (BodyPart bodyPart : childrenBodyParts) {
            bodyPart.setAsBox2d(body.getAngle() + bodyAngle);
        }
    }

    public void setMaxAngles(float lowerDegress, float upperDegress) {
        angleLimit = true;
        angleLowerLimit = lowerDegress * MathUtils.degRad;
        angleUpperLimit = upperDegress * MathUtils.degRad;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Body getBox2dBody() {
        return body;
    }

    public void setBox2dBody(Vector2[] box2dBody) {
        this.box2dBodyVerts = new Vector2[box2dBody.length];
        for (int i = 0; i < box2dBody.length; i++) {
            this.box2dBodyVerts[i] = new Vector2(box2dBody[i]);
        }
    }

    public Vector2 getMove() {
        return move;
    }
}
