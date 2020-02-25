package pl.cyfrogen.skijumping.character;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class VectorUtils {
    /**
     * Uses sin/cos table to make fast rotation of vector
     *
     * @param vector  vector to be rotated
     * @param radians angle in radians
     * @return rotated vector
     */
    public static Vector2 fastRotateRad(Vector2 vector, float radians) {
        float cos = MathUtils.cos(radians);
        float sin = MathUtils.sin(radians);

        float newX = vector.x * cos - vector.y * sin;
        float newY = vector.x * sin + vector.y * cos;

        vector.x = newX;
        vector.y = newY;

        return vector;
    }

}
