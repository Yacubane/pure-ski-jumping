package pl.cyfrogen.skijumping.gui.actors.common;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class Button extends Group {
    private final ClickListener clickListener;

    public Button(){
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

}
