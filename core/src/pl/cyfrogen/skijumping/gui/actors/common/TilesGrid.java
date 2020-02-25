package pl.cyfrogen.skijumping.gui.actors.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import pl.cyfrogen.skijumping.gui.utils.Direction;
import pl.cyfrogen.skijumping.gui.utils.MenuAnimations;

public class TilesGrid extends Group {
    private static final float ELEMENT_ANIMATION_START_IDLE_TIME = 0.06f;
    private final Actor[][] actors;
    private final float tileWidth;
    private final float tileHeight;
    private final float padding;

    public TilesGrid(int rows, int columns, float tileWidth, float tileHeight, float padding) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.padding = padding;
        float width = columns * (tileWidth + padding) - padding;
        float height = rows * (tileHeight + padding) - padding;

        setSize(width, height);

        actors = new Actor[rows][columns];
    }

    public void add(Actor actor, int row, int column) {
        actors[row][column] = actor;
        actor.setPosition(
                column * (tileWidth + padding),
                getHeight() - tileHeight - (row * (tileHeight + padding)));
        addActor(actor);
        //actor.setColor(0, 0, 0, 0);
    }

    public void show(Direction direction) {
        for (int row = 0; row < actors.length; row++) {
            for (int column = 0; column < actors[row].length; column++) {
                int number;
                if (direction == Direction.RIGHT) {
                    number = actors.length * actors[0].length - (column * actors.length + row);
                } else {
                    number = column * actors.length + row;
                }
                float idleTime = ELEMENT_ANIMATION_START_IDLE_TIME * number;
                if (actors[row][column] != null) {
                    actors[row][column].setColor(new Color(1f, 1f, 1f, 0f));
                    MenuAnimations.apply(MenuAnimations.showingAnimation()
                                    .withStartIdleTime(idleTime)
                                    .withDirection(direction),
                            actors[row][column]);
                }
            }
        }
    }

    public void hide(Direction direction) {
        for (int row = 0; row < actors.length; row++) {
            for (int column = 0; column < actors[row].length; column++) {
                int number;
                if (direction == Direction.RIGHT) {
                    number = actors.length * actors[0].length - (column * actors.length + row);
                } else {
                    number = column * actors.length + row;
                }

                float idleTime = ELEMENT_ANIMATION_START_IDLE_TIME * number;
                if (actors[row][column] != null) {
                 MenuAnimations.apply(MenuAnimations.hidingAnimation()
                                    .withStartIdleTime(idleTime)
                                    .withDirection(direction),
                            actors[row][column]);
                }
            }
        }
    }

}
