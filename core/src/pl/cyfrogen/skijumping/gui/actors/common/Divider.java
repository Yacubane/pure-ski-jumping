package pl.cyfrogen.skijumping.gui.actors.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import pl.cyfrogen.skijumping.Main;


public class Divider extends Group {
    public Divider(float width, float height, Color color) {
        setSize(width, height);

        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        Image background = new Image(whiteDot);
        background.setColor(color);
        background.setSize(width, height);
        addActor(background);

        addActor(background);

    }

    public Divider(float width, Color color) {
        this(width, Math.min(Gdx.graphics.getHeight() * 0.003f, 1), color);
    }
}
