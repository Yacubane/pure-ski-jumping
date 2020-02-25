package pl.cyfrogen.skijumping.gui.actors.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.gui.actors.common.FBOWidget;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;


public class WindWidget extends FBOWidget {
    private static final float MAX_WIND_FORCE = 20;
    final float WIDTH_HEIGHT_RATIO = 3.45f;

    public WindWidget(TextureRegion jumperIcon, TextureRegion windIcon, TextureRegion windMaskIcon, int windForce) {
        setTouchable(Touchable.disabled);
        float width = Gdx.graphics.getWidth() * 0.16f;
        float height = width / WIDTH_HEIGHT_RATIO;

        setSize(width, height);

        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        Image background = new Image(whiteDot);
        background.setColor(Color.valueOf("161616"));
        background.setSize(width, height * 0.66f);
        addActor(background);

        WindAnimationWidget windWidget = new WindAnimationWidget(
                windIcon, windMaskIcon,
                width / 2f, height * 0.66f);
        if (windForce > 0)
            windWidget.setPosition(width / 2f, 0);

        windWidget.set(200f * -windForce / MAX_WIND_FORCE, windForce > 0);

        if (windForce != 0)
            addActor(windWidget);

        TextureRegion jumperTexture = new TextureRegion(jumperIcon);

        int absoluteWindForce = Math.abs(windForce);
        String windText = absoluteWindForce / 10 + "." + absoluteWindForce % 10 + " m/s";
        FilledLabel numberLabel = new FilledLabel(width, height * 0.4f, 0.5f, 0.7f, windText,
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.WHITE));
        addActor(numberLabel);

        Image image = new Image(jumperTexture);
        jumperTexture.flip(true, false);
        image.setSize(width / 6f, height * 0.66f);
        image.setPosition(width / 2f - image.getWidth() / 2f, (height - image.getHeight()) * 1f);
        addActor(image);


    }

}
