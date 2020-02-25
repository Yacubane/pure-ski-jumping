package pl.cyfrogen.skijumping.gui.actors.tutorial.images;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import pl.cyfrogen.skijumping.Main;

public class TutorialImageSwipe extends Group {
    private final Sprite phoneSprite;
    private final Sprite thumbSprite;

    public TutorialImageSwipe(TextureRegion phoneIcon, final TextureRegion thumbIcon) {
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

        thumbSprite = new Sprite(thumbIcon);
        final float thumbSize = phoneSpriteHeight * 0.37f;
        thumbSprite.setSize(thumbSize, thumbSize);
        thumbSprite.setPosition(phoneSprite.getX() + phoneSprite.getWidth() * 0.6f,
                phoneSprite.getY() + phoneSprite.getHeight() / 2f - thumbSprite.getHeight() / 2f);
        thumbSprite.setOriginCenter();

        addActor(new Actor() {
            float time = 0;

            @Override
            public void draw(Batch batch, float alpha) {
                phoneSprite.draw(batch,alpha);

                time += Gdx.graphics.getDeltaTime();
                float maxDisplacement = thumbSprite.getHeight() * 0.4f;
                float thumbY = thumbSprite.getY();
                float ANIM_TIME = 0.75f;
                if (time < ANIM_TIME) {
                    float percentage = (time) / ANIM_TIME;
                    float displacement = MathUtils.lerp(-maxDisplacement, +maxDisplacement, Interpolation.smooth.apply(percentage));
                    thumbSprite.setY(thumbY + displacement);
                    thumbSprite.draw(batch,alpha);
                    thumbSprite.setY(thumbY);
                } else if (time < (ANIM_TIME * 2) && time > ANIM_TIME) {
                    float percentage = (time - ANIM_TIME) / ANIM_TIME;
                    float displacement = MathUtils.lerp(+maxDisplacement, -maxDisplacement, Interpolation.smooth.apply(percentage));
                    thumbSprite.setY(thumbY + displacement);
                    thumbSprite.draw(batch,alpha);
                    thumbSprite.setY(thumbY);
                } else {
                    time = 0;
                    thumbSprite.setY(thumbY - maxDisplacement);
                    thumbSprite.draw(batch,alpha);
                    thumbSprite.setY(thumbY);
                }
            }
        });
    }


}
