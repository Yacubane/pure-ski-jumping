package pl.cyfrogen.skijumping.gui.actors.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import pl.cyfrogen.skijumping.common.interfaces.OnEnd;

public class CyfrogenLogo extends Actor {

    private static final float FULL_TIME = 1.5f;
    public static final float CANVAS_WIDTH_HEIGHT_RATIO = 1000 / 152f;
    private final Texture cyfrogenTex;
    private float time;
    private OnEnd onEnd;


    public CyfrogenLogo(float width, float height) {
        setSize(width, height);

        cyfrogenTex = new Texture(Gdx.files.internal("textures/cyfrogen/cyfrogen.png"), true);
        cyfrogenTex.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
    }


    public void act(float delta) {
        super.act(delta);
        time += delta;
        if (time > FULL_TIME) {
            if (onEnd != null) {
                onEnd.end();
                onEnd = null;
            }
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (Gdx.input.isTouched()) {
            if (onEnd != null) {
                onEnd.end();
                onEnd = null;
            }
        }
        batch.setColor(getColor());
        batch.draw(cyfrogenTex, getX(), getY(), getWidth(), getHeight());
    }

    public void dispose() {
        cyfrogenTex.dispose();
    }

    public void setOnEnd(OnEnd onEnd) {
        this.onEnd = onEnd;
    }
}
