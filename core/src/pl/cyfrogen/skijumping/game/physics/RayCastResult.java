package pl.cyfrogen.skijumping.game.physics;

public class RayCastResult {
    private final Point result;
    private final Edge collidedEdge;
    private final boolean found;

    public RayCastResult(Point result, Edge collidedEdge, boolean found) {

        this.result = result;
        this.collidedEdge = collidedEdge;
        this.found = found;
    }

    public Point getResult() {
        return result;
    }

    public Edge getCollidedEdge() {
        return collidedEdge;
    }

    public boolean isFound() {
        return found;
    }
}