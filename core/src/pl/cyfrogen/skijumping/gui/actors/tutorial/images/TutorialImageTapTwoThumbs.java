package pl.cyfrogen.skijumping.gui.actors.tutorial.images;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import pl.cyfrogen.skijumping.Main;

public class TutorialImageTapTwoThumbs extends Group {
    private final Sprite phoneSprite;
    private final Sprite thumbSprite;

    public TutorialImageTapTwoThumbs(TextureRegion phoneIcon, final TextureRegion thumbIcon) {
        float size = Gdx.graphics.getHeight() * 0.3f;
        setSize(size, size);

        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        Image background = new Image(whiteDot);
        background.setColor(Color.valueOf("000000"));
        background.setSize(size, size);
        addActor(background);

        phoneSprite = new Sprite(phoneIcon);
        float phoneSpriteHeight = size * 0.4f;
        phoneSprite.setSize(phoneIcon.getRegionWidth() / (float) phoneIcon.getRegionHeight() * phoneSpriteHeight, phoneSpriteHeight);
        phoneSprite.setPosition(size / 2f - phoneSprite.getWidth() / 2f,
                size / 2f - phoneSprite.getHeight() / 2f);

        final float thumbSize = phoneSpriteHeight * 0.37f;

        thumbSprite = new Sprite(thumbIcon);
        thumbSprite.setSize(thumbSize, thumbSize);
        thumbSprite.setPosition(phoneSprite.getX() + phoneSprite.getWidth() * 0.6f,
                phoneSprite.getY() + phoneSprite.getHeight() / 2f - thumbSprite.getHeight() / 2f);
        thumbSprite.setOriginCenter();

        final Sprite thumbSprite2 = new Sprite(thumbIcon);
        thumbSprite2.setSize(thumbSize, thumbSize);
        thumbSprite2.setPosition(phoneSprite.getX() + phoneSprite.getWidth() - (phoneSprite.getWidth() * 0.6f + thumbSize),
                phoneSprite.getY() + phoneSprite.getHeight() / 2f - thumbSprite.getHeight() / 2f);
        thumbSprite2.setOriginCenter();

        addActor(new Actor() {
            float time = 0;

            @Override
            public void draw(Batch batch, float alpha) {
                phoneSprite.draw(batch, alpha);

                time += Gdx.graphics.getDeltaTime();
                float BLANK_TIME = 0.1f;

                if (time < BLANK_TIME) {
                } else if (time < BLANK_TIME + 0.15f && time > BLANK_TIME) {
                    float percentage = (time - BLANK_TIME) / 0.15f;
                    thumbSprite.setScale(MathUtils.lerp(1.4f, 1f, percentage));
                    thumbSprite.draw(batch, alpha);
                    thumbSprite.setScale(1f);
                    thumbSprite2.setScale(MathUtils.lerp(1.4f, 1f, percentage));
                    thumbSprite2.draw(batch, alpha);
                    thumbSprite2.setScale(1f);
                } else if (time < BLANK_TIME + 0.5f && time > BLANK_TIME + 0.15f) {
                    thumbSprite.draw(batch, alpha);
                    thumbSprite2.draw(batch, alpha);
                } else {
                    time = 0;
                }
            }
        });
    }


}
