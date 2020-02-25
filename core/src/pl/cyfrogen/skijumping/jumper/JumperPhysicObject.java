package pl.cyfrogen.skijumping.jumper;

import com.badlogic.gdx.math.Vector2;

import pl.cyfrogen.skijumping.game.physics.Point;
import pl.cyfrogen.skijumping.game.physics.SkiJumpPhysics;

public class JumperPhysicObject {
    private final SkiJumpPhysics physics;

    public JumperPhysicObject(SkiJumpPhysics physics, Vector2 position) {
        this.physics = physics;
        physics.setJumperPosition(position.x, position.y);
    }

    public Vector2 getPosition() {
        return physics.getJumperPosition().toVec2();
    }

    public Vector2 getLinearVelocity() {
        return physics.getJumperVelocity().toVec2();
    }

    public void setLinearVelocity(float x, float y) {
        physics.setJumperVelocity(x,y);
    }

    public void applyForce(Vector2 vector2) {
        physics.setForce(Point.of(vector2));
    }
}
