package pl.cyfrogen.skijumping.game.physics;

public interface CollidingListener {
    void startColliding(Edge collidingEdge, Point collisionPoint);
    void endColliding(Edge collidingEdge, Point collisionPoint);
    void nextColliding(Edge collidingEdge, Edge nextCollidingEdge, Point collisionPoint);
}
