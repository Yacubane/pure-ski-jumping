package pl.cyfrogen.skijumping.game.physics;

public class PhysicsUtils {
    public final static double EPS = 1e-8f;

    // if angle is (0,180) then jumper is over this line P1------P2
    // else if angle is (-180,0) then jumper is under this line

    // if abs(angle) is (0, 90) then jumper is going to P2
    // else if abs(angle) is (90,180) then jumper is going to P1
    public static double getAngleBetweenTwoVectors(Point vector1, Point Point) {
        return vector1.angleRad(Point);
    }


    public static Point nearestLinePoint(Point start, Point end, Point point, Point nearest) {
        double length2 = start.dst2(end);
        if (length2 == 0) return nearest.set(start);
        double t = ((point.x - start.x) * (end.x - start.x) + (point.y - start.y) * (end.y - start.y)) / length2;
        // if (t < 0) return nearest.set(start);
        // if (t > 1) return nearest.set(end);
        return nearest.set(start.x + t * (end.x - start.x), start.y + t * (end.y - start.y));
    }

    public static boolean intersectSegments(Point p1, Point p2, Point p3, Point p4, Point intersection) {
        double x1 = p1.x, y1 = p1.y, x2 = p2.x, y2 = p2.y, x3 = p3.x, y3 = p3.y, x4 = p4.x, y4 = p4.y;

        double d = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if (d == 0) return false;

        double yd = y1 - y3;
        double xd = x1 - x3;
        double ua = ((x4 - x3) * yd - (y4 - y3) * xd) / d;
        if (ua < 0 || ua > 1) return false;

        double ub = ((x2 - x1) * yd - (y2 - y1) * xd) / d;
        if (ub < 0 || ub > 1) return false;

        if (intersection != null) intersection.set(x1 + (x2 - x1) * ua, y1 + (y2 - y1) * ua);
        return true;
    }

    public static boolean intersectLines(Point p1, Point p2, Point p3, Point p4, Point intersection) {
        double x1 = p1.x, y1 = p1.y, x2 = p2.x, y2 = p2.y, x3 = p3.x, y3 = p3.y, x4 = p4.x, y4 = p4.y;

        double d = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if (d == 0) return false;

        if (intersection != null) {
            double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / d;
            intersection.set(x1 + (x2 - x1) * ua, y1 + (y2 - y1) * ua);
        }
        return true;
    }

    public static boolean isZero(double velocity) {
        return Math.abs(velocity) < EPS;
    }


    public static double distanceSegmentPoint(Point p1, Point p2, Point p) {
        double x = p.x;
        double y = p.y;
        double x1 = p1.x;
        double y1 = p1.y;
        double x2 = p2.x;
        double y2 = p2.y;


        double A = x - x1;
        double B = y - y1;
        double C = x2 - x1;
        double D = y2 - y1;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = -1;
        if (len_sq != 0) //in case of 0 length line
            param = dot / len_sq;

        double xx, yy;

        if (param < 0) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        double dx = x - xx;
        double dy = y - yy;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
