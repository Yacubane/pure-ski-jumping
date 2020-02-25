package pl.cyfrogen.skijumping.game.physics;

import com.badlogic.gdx.math.Vector2;

/**
 * Implementation similar to Vector2 but with increased precision (double)
 */
public class Point {

    public double x;
    public double y;

    public Point() {

    }

    public Point(Point point) {
        set(point);
    }

    public Point(double x, double y) {
        set(x, y);
    }

    public Point(Vector2 vector) {
        set(vector.x, vector.y);
    }

    public Point set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Point set(Point point) {
        this.x = point.x;
        this.y = point.y;
        return this;
    }

    public Point lerp(Point target, double alpha) {
        final double invAlpha = 1.0f - alpha;
        this.x = (x * invAlpha) + (target.x * alpha);
        this.y = (y * invAlpha) + (target.y * alpha);
        return this;
    }

    public Point cpy() {
        return new Point(x, y);
    }

    public Point scl(double scale) {
        this.x *= scale;
        this.y *= scale;
        return this;
    }

    public Point add(Point point) {
        this.x += point.x;
        this.y += point.y;
        return this;
    }

    public Point sub(Point point) {
        this.x -= point.x;
        this.y -= point.y;
        return this;
    }

    public double len() {
        return Math.sqrt(x * x + y * y);
    }

    public double dst(Point point) {
        final double x_d = point.x - x;
        final double y_d = point.y - y;
        return Math.sqrt(x_d * x_d + y_d * y_d);
    }

    public double dst2(Point point) {
        final double x_d = point.x - x;
        final double y_d = point.y - y;
        return x_d * x_d + y_d * y_d;
    }

    public double dot(Point point) {
        return x * point.x + y * point.y;
    }

    public double crs(Point point) {
        return this.x * point.y - this.y * point.x;
    }

    public double angleRad(Point reference) {
        return Math.atan2(crs(reference), dot(reference));
    }

    public Point nor() {
        double len = len();
        if (len != 0) {
            x /= len;
            y /= len;
        }
        return this;
    }

    public Vector2 toVec2() {
        return new Vector2((float) x, (float) y);
    }

    public static Point of(Vector2 vector2) {
        return new Point(vector2);
    }
}
