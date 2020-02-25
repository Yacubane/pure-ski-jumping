package pl.cyfrogen.skijumping.gui.actors.common;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class MenuActor {
    private final Actor actor;
    private final float delay;

    public MenuActor(Actor actor, float delay) {
        this.actor = actor;
        this.delay = delay;
    }

    public Actor getActor() {
        return actor;
    }

    public float getDelay() {
        return delay;
    }
}
