package pl.cyfrogen.skijumping.game.physics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SkiJumpPhysicsTest {
    @Test
    /**
     * Ride from 45 degrees platform with
     * Height/width = 50/50
     * Expected speed ~ 31,304951685 m/s
     */
    public void checkVelocityAfterSlidingFrom45DegreesPlatform() {
        Edge e1 = new Edge(new Line(new Point(0, 200), new Point(100, 100)));
        EdgeGroup edgeGroup = new EdgeGroup();
        edgeGroup.addEdge(e1);

        final SkiJumpPhysics physics = new SkiJumpPhysics(1 / 120f);
        physics.setKineticFrictionCoefficient(0);
        physics.addEdgeGroup(edgeGroup);

        physics.setCollidingListener(new CollidingListener() {
            @Override
            public void startColliding(Edge collidingEdge, Point collisionPoint) {

            }

            @Override
            public void endColliding(Edge collidingEdge, Point collisionPoint) {
                assertEquals(31.304952, physics.getJumperVelocity().len(), 0.000001f);
            }

            @Override
            public void nextColliding(Edge collidingEdge, Edge nextCollidingEdge, Point collisionPoint) {

            }
        });

        physics.setJumperPosition(50, 150);
        float time = 10f;
        physics.update(time);
    }
}