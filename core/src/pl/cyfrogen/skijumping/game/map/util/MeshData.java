package pl.cyfrogen.skijumping.game.map.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class MeshData {
    private final Texture texture;
    private final float color;
    private final Vector2[] points;

    public MeshData(Vector2[] points, Texture texture, float color) {
        this.texture = texture;
        this.color = color;
        this.points = points;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getColor() {
        return color;
    }

    public Vector2[] getPoints() {
        return points;
    }

    public float[] getPointsInFloatArray() {
        float[] floatPoints = new float[points.length * 2];
        int i = 0;
        for (Vector2 vector2 : points) {
            floatPoints[i++] = vector2.x;
            floatPoints[i++] = vector2.y;
        }
        return floatPoints;
    }
}
