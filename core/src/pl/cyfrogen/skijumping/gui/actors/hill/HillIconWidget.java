package pl.cyfrogen.skijumping.gui.actors.hill;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.gui.actors.common.Button;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;

public class HillIconWidget extends Button {
    private final Image image;
    private final Image background;
    private final Image whiteBackground;
    private final FilledLabel label;
    private TextureRegion region;

    public HillIconWidget(TextureRegion region, String name, float width, float height) {
        super();
        setSize(width, height);
        this.region = region;
        setOrigin(Align.center);

        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();

        background = new Image(whiteDot);
        background.setColor(Color.valueOf("000000"));
        background.setSize(width, height);
        addActor(background);

        image = new Image(region);
        image.setScaling(Scaling.fit);
        image.setSize(getWidth(), getHeight());
        addActor(image);

        whiteBackground = new Image(whiteDot);
        whiteBackground.setColor(Color.valueOf("FFFFFF"));
        whiteBackground.setSize(width, height * 0.125f);
        whiteBackground.setPosition(0, height * 0.05f);
        addActor(whiteBackground);

        label = new FilledLabel(width, height * 0.125f,
                0.9f, 0.45f, name,
                Main.getInstance().getAssets().getLabelStyle(Color.BLACK));
        label.setPosition(0, whiteBackground.getY());
        addActor(label);


    }

    public void setTexture(TextureRegion region) {
        this.region = region;
        image.setDrawable(new TextureRegionDrawable(region));
    }

}
