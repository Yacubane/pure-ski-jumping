package pl.cyfrogen.skijumping.gui.actors.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class MenuButton extends Button {
    private final Image image;
    private TextureRegion region;

    public MenuButton(TextureRegion region) {
        super();
        this.region = region;
        image = new Image(region);
        image.setSize(getWidth(), getHeight());
        addActor(image);
        setOrigin(Align.center);
    }

    public void setTexture(TextureRegion region) {
        this.region = region;
        image.setDrawable(new TextureRegionDrawable(region));
    }

    @Override
    public void setColor(Color color) {
        image.setColor(color);
    }

    @Override
    public Color getColor() {
        return image.getColor();
    }

    @Override
    public void sizeChanged() {
        image.setSize(getWidth(), getHeight());
    }

    public void setWidthPreserveAspectRatio(float width) {
        setWidth(width);
        setHeight(region.getRegionHeight() / (float) region.getRegionWidth() * width);
    }
}
