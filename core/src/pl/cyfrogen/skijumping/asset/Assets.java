package pl.cyfrogen.skijumping.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.DistanceFieldFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Assets {
    private final Texture whiteDot;
    private ShaderProgram customShader;

    private BitmapFont font;


    public Assets() {
        try {
            customShader = DistanceFieldFont.createDistanceFieldShader();
        } catch (IllegalArgumentException ex) {
            customShader = null;
        }

        whiteDot = new Texture("textures/common/whiteDot.png");
        Texture texture = new Texture(Gdx.files.internal("fonts/font.png"), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"), new TextureRegion(texture), false);
    }

    public BitmapFont getFont() {
        return font;
    }

    public Texture getWhiteDot() {
        return whiteDot;
    }

    public Label.LabelStyle getLabelStyle(Color color) {
        return new Label.LabelStyle(font, color);
    }

    public ShaderProgram getDistanceFieldShader() {
        return customShader;
    }
}
