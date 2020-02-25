package pl.cyfrogen.skijumping.gui.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class MenuAnimations {
    public interface ActionBuilder {
        Action build();
    }

    public static class ShowingAnimationBuilder implements ActionBuilder {
        private float startIdleTime = 0f;
        private float moveDistance = Gdx.graphics.getWidth() * 0.15f;
        private float moveDuration = 0.5f;
        public static final float CHANGE_ALPHA_DURATION = 0.5f;
        private Direction direction;

        public ShowingAnimationBuilder() {

        }

        public ShowingAnimationBuilder withStartIdleTime(float startIdleTime) {
            this.startIdleTime = startIdleTime;
            return this;
        }

        public ShowingAnimationBuilder withDirection(Direction direction) {
            this.direction = direction;
            return this;
        }

        public Action build() {
            float realMoveDistance = moveDistance;
            if (direction == Direction.RIGHT) {
                realMoveDistance = -moveDistance;
            }
            return Actions.sequence(
                    Actions.alpha(0),
                    Actions.moveBy(realMoveDistance, 0),
                    Actions.delay(startIdleTime),
                    Actions.parallel(
                            Actions.alpha(1, CHANGE_ALPHA_DURATION,
                                    Interpolation.pow2Out),
                            Actions.moveBy(-realMoveDistance, 0,
                                    moveDuration,
                                    Interpolation.pow2Out))

            );
        }
    }

    public static class HidingAnimationBuilder implements ActionBuilder {
        private float startIdleTime = 0f;
        private float moveDistance = Gdx.graphics.getWidth() * 0.15f;
        private float moveDuration = 0.5f;
        private float changeAlphaDuration = 0.5f;
        private Direction direction = Direction.LEFT;

        public HidingAnimationBuilder() {

        }

        public HidingAnimationBuilder withStartIdleTime(float startIdleTime) {
            this.startIdleTime = startIdleTime;
            return this;
        }

        public HidingAnimationBuilder withDirection(Direction direction) {
            this.direction = direction;
            return this;
        }

        public Action build() {
            float realMoveDistance = moveDistance;
            if (direction == Direction.LEFT) {
                realMoveDistance = -moveDistance;
            } else {
                realMoveDistance = moveDistance;
            }
            return Actions.sequence(
                    Actions.delay(startIdleTime),
                    Actions.parallel(
                            Actions.alpha(0, changeAlphaDuration,
                                    Interpolation.pow2Out),
                            Actions.moveBy(realMoveDistance, 0,
                                    moveDuration,
                                    Interpolation.pow2Out)),
                    Actions.moveBy(-realMoveDistance, 0)
            );
        }
    }


    public static ShowingAnimationBuilder showingAnimation() {
        return new ShowingAnimationBuilder();
    }

    public static HidingAnimationBuilder hidingAnimation() {
        return new HidingAnimationBuilder();
    }

    public static void apply(ActionBuilder actionBuilder, Actor actor) {
        actor.addAction(actionBuilder.build());
    }
}
