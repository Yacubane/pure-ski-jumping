package pl.cyfrogen.skijumping.game.physics;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Line {
    Point start, end;

    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public void draw(ShapeRenderer renderer) {
        renderer.line((float) start.x, (float) start.y, (float) end.x, (float) end.y);
    }

    public Point getPoint1() {
        return start;
    }

    public Point getPoint2() {
        return end;
    }
}
