package pl.cyfrogen.skijumping.gui.actors.common;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class MenuCheckbox extends Image {
    private final ClickListener clickListener;
    private final TextureRegion inactive;
    private final TextureRegion active;
    private boolean checked;

    public MenuCheckbox(TextureRegion inactive, TextureRegion active) {
        super(inactive);
        this.inactive = inactive;
        this.active = active;

        setOrigin(Align.center);
        addListener(clickListener = new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                onClicked();
            }
        });
    }

    private void onClicked() {
        checked = !checked;
        update();
    }

    public boolean isPressed() {
        return clickListener.isVisualPressed();
    }

    public void draw(Batch batch, float parentAlpha) {
        validate();

        boolean isPressed = isPressed();
        if (isPressed) {
            setOrigin(Align.center);
            setScale(0.9f);
        } else {
            setScale(1f);
        }
        super.draw(batch, parentAlpha);
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        update();
    }

    private void update() {
        if (checked) {
            setDrawable(new TextureRegionDrawable(active));
        } else {
            setDrawable(new TextureRegionDrawable(inactive));
        }
    }
}
