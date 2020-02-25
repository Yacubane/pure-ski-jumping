package pl.cyfrogen.skijumping.game.map.lines;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import pl.cyfrogen.skijumping.game.physics.Point;

public class HillLines {
    public static Vector2[] generate(List<Vector2> outrun,
                                     List<Vector2> outrunTopBorder,
                                     List<Vector2> outrunBottomBorder,
                                     float desiredDistance) {

        double distance = 0;
        Vector2 result = null;
        Vector2 dir = null;
        for (int i = 0; i < outrun.size() - 1; i++) {
            Point point1 = new Point(outrun.get(i));
            Point point2 = new Point(outrun.get(i + 1));
            double newDistance = distance + point1.dst(point2);
            if (newDistance < desiredDistance) {
                distance = newDistance;
            } else {
                double percentage = (desiredDistance - distance) / (newDistance - distance);
                result = point1.cpy().lerp(point2, percentage).toVec2();
                dir = point2.cpy().sub(point1).nor().toVec2();
                break;
            }
        }

        float LINE_HEIGHT = 0.1f;
        float MAX_LINE_WIDTH = 20f;
        Vector2 leftCenter = result.cpy().add(dir.cpy().scl(-LINE_HEIGHT));
        Vector2 rightCenter = result.cpy().add(dir.cpy().scl(LINE_HEIGHT));

        Vector2 upperLeftCorner = leftCenter.cpy().add(dir.cpy().rotate(90).scl(MAX_LINE_WIDTH));
        Vector2 bottomLeftCorner = leftCenter.cpy().add(dir.cpy().rotate(90).scl(-MAX_LINE_WIDTH));
        Vector2 bottomRightCorner = rightCenter.cpy().add(dir.cpy().rotate(90).scl(-MAX_LINE_WIDTH));
        Vector2 upperRightCorner = rightCenter.cpy().add(dir.cpy().rotate(90).scl(MAX_LINE_WIDTH));

        Vector2 tmp = new Vector2();
        for (int i = 1; i < outrunTopBorder.size(); i++) {
            Vector2 vector21 = outrunTopBorder.get(i-1);
            Vector2 vector22 = outrunTopBorder.get(i);
            if (Intersector.intersectSegments(upperLeftCorner, bottomLeftCorner, vector21, vector22, tmp)) {
                upperLeftCorner.set(tmp);
            }
            if (Intersector.intersectSegments(bottomRightCorner, upperRightCorner, vector21, vector22, tmp)) {
                upperRightCorner.set(tmp);
            }
        }

        for (int i = 1; i < outrunBottomBorder.size(); i++) {
            Vector2 vector21 = outrunBottomBorder.get(i-1);
            Vector2 vector22 = outrunBottomBorder.get(i);
            if (Intersector.intersectSegments(upperLeftCorner, bottomLeftCorner, vector21, vector22, tmp)) {
                bottomLeftCorner.set(tmp);
            }
            if (Intersector.intersectSegments(bottomRightCorner, upperRightCorner, vector21, vector22, tmp)) {
                bottomRightCorner.set(tmp);
            }
        }


        Vector2[] vector2s = new Vector2[]{
                upperLeftCorner,
                bottomLeftCorner,
                bottomRightCorner,
                upperRightCorner
        };

        return vector2s;
    }

    public static Vector2[] getBounds(Vector2[] kLineVector) {
        Vector2 min = kLineVector[0].cpy();
        for (int i = 1; i < kLineVector.length; i++) {
            if (kLineVector[i].x < min.x) {
                min.x = kLineVector[i].x;
            }
            if (kLineVector[i].y < min.y) {
                min.y = kLineVector[i].y;
            }
        }

        Vector2 max = kLineVector[0].cpy();
        for (int i = 1; i < kLineVector.length; i++) {
            if (kLineVector[i].x > max.x) {
                max.x = kLineVector[i].x;
            }
            if (kLineVector[i].y > max.y) {
                max.y = kLineVector[i].y;
            }
        }
        return new Vector2[]{min, max.sub(min)};
    }
}
