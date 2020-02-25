package pl.cyfrogen.skijumping.gui.actors.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

class WindAnimationWidget extends Group {
    private final TextureRegion windTexture;
    private final TextureRegion windMaskTexture;
    float speed = 150f;
    private boolean windMaskOnLeft;
    float position;

    public WindAnimationWidget(TextureRegion windIcon, TextureRegion windMaskIcon, float width, float height) {
        setSize(width, height);

        windTexture = new TextureRegion(windIcon);
        windMaskTexture = new TextureRegion(windMaskIcon);

        Actor actor = new Actor() {
            @Override
            public void draw(Batch batch, float alpha) {
                float windTextureHeight = getHeight();
                float windTextureWidth = windTexture.getRegionWidth() / (float) windTexture.getRegionHeight() * windTextureHeight;

                position += speed * Gdx.graphics.getDeltaTime() * Gdx.graphics.getWidth() / 1000f;
                position %= windTextureWidth;

                batch.setColor(Color.WHITE.cpy().mul(1f, 1f, 1f, alpha));

                Rectangle clipBounds = new Rectangle(0, 0, getWidth(), getHeight());
                Rectangle scissorBounds = new Rectangle();
                getStage().calculateScissors(clipBounds, scissorBounds);

                // Enable scissors for widget area and draw the widget.
                batch.flush();
                if (ScissorStack.pushScissors(scissorBounds)) {

                    float textureX = position - windTextureWidth;
                    while (textureX < getWidth()) {
                        batch.draw(windTexture, textureX, 0, windTextureWidth, windTextureHeight);
                        textureX += windTextureWidth;
                    }
                    batch.flush();
                    ScissorStack.popScissors();
                }

                float windMaskTextureHeight = getHeight();
                float windMaskTextureWidth = windMaskTexture.getRegionWidth() / (float) windMaskTexture.getRegionHeight() * windMaskTextureHeight;
                if (windMaskOnLeft) {
                    batch.draw(windMaskTexture, 0, 0, windMaskTextureWidth, windMaskTextureHeight);

                } else {
                    batch.draw(windMaskTexture, getWidth() - windMaskTextureWidth, 0, windMaskTextureWidth, windMaskTextureHeight);
                }

            }
        };
        actor.setSize(width, height);

        addActor(actor);

    }

    public void set(float speed, boolean windMaskOnLeft) {
        this.speed = speed;
        this.windMaskOnLeft = windMaskOnLeft;
        windMaskTexture.flip(windMaskOnLeft, false);
    }
}
