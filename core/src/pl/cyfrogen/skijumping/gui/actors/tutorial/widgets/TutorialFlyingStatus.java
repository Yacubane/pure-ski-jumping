package pl.cyfrogen.skijumping.gui.actors.tutorial.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class TutorialFlyingStatus extends Group {
    private final Sprite thumb;
    private final float thumbMinY;
    private final float thumbMaxY;
    float width = Gdx.graphics.getWidth() * 0.1f;
    float height = Gdx.graphics.getHeight() * 0.5f;
    private float positionPercent;

    public TutorialFlyingStatus(TextureRegion backgroundTexture, TextureRegion thumbTexture) {
        setTouchable(Touchable.disabled);
        float minScale = Math.min(width / (float) thumbTexture.getRegionWidth(),
                Math.min(height / (float) thumbTexture.getRegionHeight(),
                        Math.min(width / (float) backgroundTexture.getRegionWidth(),
                                height / (float) backgroundTexture.getRegionHeight())));

        setSize(width, height);

        final Sprite background = new Sprite(backgroundTexture);
        background.setSize(backgroundTexture.getRegionWidth() * minScale, backgroundTexture.getRegionHeight() * minScale);
        background.setPosition(width / 2f - background.getWidth() / 2f,
                height / 2f - background.getHeight() / 2f);

        thumb = new Sprite(thumbTexture);
        thumb.setSize(thumbTexture.getRegionWidth() * minScale, thumbTexture.getRegionHeight() * minScale);
        thumb.setPosition(width / 2f - thumb.getWidth() / 2f,
                height / 2f - thumb.getHeight() / 2f);

        thumbMinY = thumb.getY() - background.getHeight() / 2f;
        thumbMaxY = thumb.getY() + background.getHeight() / 2f;


        addActor(new Actor() {
            @Override
            public void draw(Batch batch, float alpha) {
                background.draw(batch, alpha);
                thumb.draw(batch, alpha);
            }
        });
    }

    public void setPercentage(float positionPercent, float goodFlyingPercent) {
        if (positionPercent > 0) {
            thumb.setY(thumbMinY + (thumbMaxY - thumbMinY) / 2f * goodFlyingPercent);
        } else {
            thumb.setY(thumbMinY + thumbMaxY - (thumbMaxY - thumbMinY) / 2f * goodFlyingPercent);
        }
    }

    public void close() {
        addAction(Actions.sequence(
                Actions.alpha(0, 0.5f, Interpolation.smooth),
                Actions.removeActor()
        ));
    }

    public void show() {
        setColor(Color.CLEAR);
        addAction(Actions.sequence(
                Actions.alpha(1f, 0.5f, Interpolation.smooth)
        ));
    }
}
