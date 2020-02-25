package pl.cyfrogen.skijumping.game.physics;

class AABB {
    Point start;
    Point size;

    public AABB(Point start, Point size) {
        this.start = start;
        this.size = size;
    }

    public AABB(double x1, double y1, double width, double height) {
        this.start = new Point(x1, y1);
        this.size = new Point(width, height);
    }

    public static boolean intersects(AABB object1, AABB object2) {
        if (object1.start.x < object2.start.x + object2.size.x + PhysicsUtils.EPS &&
                object1.start.x + object1.size.x + PhysicsUtils.EPS > object2.start.x &&
                object1.start.y < object2.start.y + object2.size.y + PhysicsUtils.EPS &&
                object1.start.y + object1.size.y + PhysicsUtils.EPS > object2.start.y) {
            return true;
        }
        return false;
    }

    public static AABB of(Point corner1, Point corner2) {
        double x1 = Math.min(corner1.x, corner2.x);
        double y1 = Math.min(corner1.y, corner2.y);
        double x2 = Math.max(corner1.x, corner2.x);
        double y2 = Math.max(corner1.y, corner2.y);

        double width = x2 - x1;
        double height = y2 - y1;

        return new AABB(x1, y1, width, height);

    }
}
