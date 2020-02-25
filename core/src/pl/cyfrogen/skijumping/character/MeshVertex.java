package pl.cyfrogen.skijumping.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class MeshVertex {
    float u;
    float v;
    private final Vector2 originalPoint;
    private float color = Color.WHITE.toFloatBits();
    private float rotationForce;
    private Vector2 point = new Vector2();

    public MeshVertex(Vector2 originalPoint) {
        this.originalPoint = originalPoint;
        this.rotationForce = 1f;
    }

    public MeshVertex(MeshVertex meshVertex) {
        this.originalPoint = meshVertex.getPoint();
        this.rotationForce = meshVertex.getRotationForce();
        this.color = meshVertex.getColor();
        this.point = meshVertex.getPoint();
    }

    public static void setForce(MeshVertex[] points, float min, float max) {
        float dst = 0;
        for (int i = 1; i < points.length; i++) {
            dst += points[i].getOriginalPoint().dst(points[i - 1].getOriginalPoint());
        }
        float actualDst = 0;
        for (int i = 0; i < points.length; i++) {
            if (i > 0)
                actualDst += points[i].getOriginalPoint().dst(points[i - 1].getOriginalPoint());
            float percentage = actualDst / dst;
            points[i].setRotationForce(min + (max - min) * percentage);
        }

    }

    public void setU(float u) {
        this.u = u;
    }

    public void setV(float v) {
        this.v = v;
    }

    public float getU() {
        return u;
    }

    public float getV() {
        return v;
    }

    public float getX() {
        return point.x;
    }

    public float getY() {
        return point.y;
    }

    public Vector2 getOriginalPoint() {
        return originalPoint;
    }

    public void move(float x, float y) {
        originalPoint.add(x, y);
    }

    public void rotateAround(Vector2 rotationOrigin, float rotationDegress) {
        point.sub(rotationOrigin);
        VectorUtils.fastRotateRad(point, rotationDegress * rotationForce).add(rotationOrigin);
    }

    public float getRotationForce() {
        return rotationForce;
    }

    public MeshVertex setRotationForce(float rotationForce) {
        this.rotationForce = rotationForce;
        return this;
    }

    public Vector2 getPoint() {
        return point;
    }

    public void reset() {
        point.set(originalPoint);
    }

    public float getColor() {
        return color;
    }

    public void setColor(float color) {
        this.color = color;
    }
}
