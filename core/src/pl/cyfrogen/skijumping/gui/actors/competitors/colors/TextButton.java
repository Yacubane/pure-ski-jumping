package pl.cyfrogen.skijumping.gui.actors.competitors.colors;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;

public class TextButton extends Group {

    private final ClickListener clickListener;
    private final Image background;
    private final FilledLabel label;

    public TextButton(String name, float width, float height) {
        setSize(width, height);
        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();

        background = new Image(whiteDot);
        background.setColor(Color.valueOf("000000"));
        background.setSize(width, height);
        addActor(background);

        label = new FilledLabel(width, height,
                0.9f, 0.3f,
                name,
                Main.getInstance().getAssets().getLabelStyle(Color.WHITE));
        label.setPosition(0, 0);
        addActor(label);

        setOrigin(Align.center);
        addListener(clickListener = new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {

            }
        });
    }

    public boolean isPressed () {
        return clickListener.isVisualPressed();
    }
    public void draw (Batch batch, float parentAlpha) {
        boolean isPressed = isPressed();
        if(isPressed) {
            setOrigin(Align.center);
            setScale(0.9f);
        } else {
            setScale(1f);
        }
        super.draw(batch, parentAlpha);
    }

    public TextButton withClickListener(ClickListener clickListener) {
        addListener(clickListener);
        return this;
    }

    public void setBackgroundColor(Color color) {
        background.setColor(color);
    }

    public void setFontColor(Color color) {
        label.setFontColor(color);
    }
}
