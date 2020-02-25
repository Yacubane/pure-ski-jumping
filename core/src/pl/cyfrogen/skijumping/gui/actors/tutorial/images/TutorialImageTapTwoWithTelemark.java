package pl.cyfrogen.skijumping.gui.actors.tutorial.images;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;

public class TutorialImageTapTwoWithTelemark extends Group {
    private final Sprite phoneSprite;
    private final Sprite thumbSprite;

    public TutorialImageTapTwoWithTelemark(TextureRegion phoneIcon, final TextureRegion thumbIcon) {
        float size = Gdx.graphics.getHeight() * 0.3f;
        setSize(size, size);

        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        Image background = new Image(whiteDot);
        background.setColor(Color.valueOf("000000"));
        background.setSize(size, size);
        addActor(background);

        Image whiteBackground = new Image(whiteDot);
        whiteBackground.setColor(Color.valueOf("FFFFFF"));
        whiteBackground.setSize(size, size * 0.3f);
        whiteBackground.setPosition(0, size * 0.05f);
        addActor(whiteBackground);

        BitmapFont font = Main.getInstance().getAssets().getFont();
        float textHeight = size * 0.4f;

        FilledLabel filledLabel = new FilledLabel(whiteBackground.getWidth(), whiteBackground.getHeight() / 2f,
                1f, 0.6f, "WITH",
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.BLACK));
        filledLabel.setPosition(0, whiteBackground.getY() + whiteBackground.getHeight() / 2f);
        addActor(filledLabel);

        FilledLabel filledLabel2 = new FilledLabel(whiteBackground.getWidth(), whiteBackground.getHeight() / 2f,
                1f, 0.6f, "TELEMARK",
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.BLACK));
        filledLabel2.setPosition(0, whiteBackground.getY());
        addActor(filledLabel2);


        phoneSprite = new Sprite(phoneIcon);
        float phoneSpriteHeight = size * 0.4f;
        phoneSprite.setSize(phoneIcon.getRegionWidth() / (float) phoneIcon.getRegionHeight() * phoneSpriteHeight, phoneSpriteHeight);
        phoneSprite.setPosition(size / 2f - phoneSprite.getWidth() / 2f,
                (size - phoneSprite.getHeight()) * 0.75f);

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
                float thumbY = thumbSprite.getY();
                float maxDisplacement = thumbSize * 0.5f;
                float BLANK_TIME = 0.1f;

                if (time < BLANK_TIME) {
                } else if (time < 0.15f + BLANK_TIME && time > BLANK_TIME) {
                    float percentage = (time - BLANK_TIME) / 0.15f;
                    thumbSprite.setScale(MathUtils.lerp(1.4f, 1f, percentage));
                    thumbSprite.draw(batch, alpha);
                    thumbSprite.setScale(1f);
                    thumbSprite2.setScale(MathUtils.lerp(1.4f, 1f, percentage));
                    thumbSprite2.draw(batch, alpha);
                    thumbSprite2.setScale(1f);
                } else if (time < 0.5f + BLANK_TIME && time > 0.15f + BLANK_TIME) {
                    float percentage = (time - 0.15f - BLANK_TIME) / 0.45f;
                    thumbSprite.setY(thumbY + MathUtils.lerp(0, -maxDisplacement, Interpolation.smooth.apply(percentage)));
                    thumbSprite.draw(batch, alpha);
                    thumbSprite2.draw(batch, alpha);
                    thumbSprite.setY(thumbY);
                } else if (time < 0.95 + BLANK_TIME && time > 0.5f + BLANK_TIME) {
                    thumbSprite.setY(thumbY - maxDisplacement);
                    thumbSprite.draw(batch, alpha);
                    thumbSprite2.draw(batch, alpha);
                    thumbSprite.setY(thumbY);
                } else {
                    time = 0;
                }
            }
        });
    }


}
