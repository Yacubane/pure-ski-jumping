package pl.cyfrogen.skijumping.game.physics;


public class Collision {
    private Point point;
    private final Edge edge;
    private boolean isOnEdge;

    public Collision(Point point, Edge edge) {

        this.point = point;
        this.edge = edge;
    }

    public Collision(Point point, Edge nearestEdge, boolean isOnEdge) {
        this.point = point;
        this.edge = nearestEdge;
        this.isOnEdge = isOnEdge;
    }

    public Point getPoint() {
        return point;
    }

    public Edge getEdge() {
        return edge;
    }

    public boolean collides() {
        return edge != null;
    }

    public boolean isOnEdge() {
        return isOnEdge;
    }
}