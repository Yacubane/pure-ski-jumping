package pl.cyfrogen.skijumping.game.input;

import com.badlogic.gdx.math.Vector2;

import static pl.cyfrogen.skijumping.game.input.Pointer.State.CREATED;
import static pl.cyfrogen.skijumping.game.input.Pointer.State.TOUCH_DOWN;
import static pl.cyfrogen.skijumping.game.input.Pointer.State.TOUCH_DRAGGED;

public class Pointer {
    private State state = CREATED;
    private Vector2 touchDownPosition = new Vector2();

    public Vector2 getTouchDraggedPosition() {
        return touchDraggedPosition;
    }

    private Vector2 touchDraggedPosition = new Vector2();
    private Vector2 position = new Vector2();

    private long lastTouchDownTime;

    public Pointer() {

    }

    public void updateState(State state, int x, int y) {
        this.state = state;
        if (state == TOUCH_DOWN) {
            lastTouchDownTime = System.currentTimeMillis();
            this.touchDownPosition.set(x, y);
        } else if(state == TOUCH_DRAGGED) {
            this.touchDraggedPosition.set(x, y);
        }
        this.position.set(x, y);

    }

    public State getState() {
        return state;
    }


    public Vector2 getTouchDownPosition() {
        return touchDownPosition;
    }

    public long getLastTouchDownTime() {
        return lastTouchDownTime;
    }

    public Vector2 getPosition() {
        return position;
    }

    public enum State {CREATED, TOUCH_DRAGGED, TOUCH_UP, TOUCH_DOWN}
}
