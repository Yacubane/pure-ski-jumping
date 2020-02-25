package pl.cyfrogen.skijumping.game.physics;


public class Edge {

    private final Point direction;
    private final Point gravityDirection;
    private final boolean isElevation;
    private final float angle;
    public Line line;
    public Edge nextLine;
    public Edge prevLine;
    private float inclinedPlaneAngle;
    private boolean colliding;
    private boolean wasColliding;
    private EdgeGroup edgeGroup;
    private AABB aabb;
    private Object userData;

    public Edge(Line line) {
        this.line = line;
        direction = new Point(line.end).sub(line.start).nor();

        gravityDirection = new Point(direction);
        if (direction.y > 0) {
            gravityDirection.scl(-1);
            isElevation = true;
        } else {
            isElevation = false;
        }

        inclinedPlaneAngle = (float) Math.atan(Math.abs(direction.y / direction.x));
        angle = (float) Math.atan(direction.y / direction.x);

        aabb = AABB.of(line.start, line.end);
    }

    public Edge withNextEdge(Edge line) {
        this.nextLine = line;
        return this;
    }

    public Edge withPrevEdge(Edge line) {
        this.prevLine = line;
        return this;
    }

    public Line getLine() {
        return line;
    }

    public Point getDirection() {
        return direction;
    }

    public Point getGravityDirection() {
        return gravityDirection;
    }

    public float getInclinedPlaneAngle() {
        return inclinedPlaneAngle;
    }

    public Edge getNextEdge() {
        return nextLine;
    }

    public Edge getPrevEdge() {
        return prevLine;
    }

    public boolean isElevation() {
        return isElevation;
    }

    public void beginTick() {
        this.colliding = false;
    }

    public boolean endTick() {
        if (wasColliding && !colliding) {
            this.wasColliding = false;
            return true;
        }
        this.wasColliding = colliding;
        return false;
    }

    public void setColliding() {
        this.colliding = true;
    }

    public void endColliding() {
        this.colliding = false;
        this.wasColliding = false;
    }

    public boolean isWasColliding() {
        return wasColliding;
    }

    public void setEdgeGroup(EdgeGroup edgeGroup) {
        this.edgeGroup = edgeGroup;
    }


    public AABB getAABB() {
        return aabb;
    }

    public EdgeGroup getEdgeGroup() {
        return edgeGroup;
    }

    public float getAngle() {
        return angle;
    }

    public void setUserData(Object object) {
        this.userData = object;
    }

    public Object getUserData() {
        return userData;
    }
}
