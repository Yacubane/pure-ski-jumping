package pl.cyfrogen.skijumping.gui.actors.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;


public class GateWidget extends Group {
    final float WIDTH_HEIGHT_RATIO = 65 / 58f;

    public GateWidget(TextureRegion textureRegion, int gate) {
        setTouchable(Touchable.disabled);
        float width = Gdx.graphics.getWidth() * 0.16f;
        float height = width / WIDTH_HEIGHT_RATIO;

        setSize(width, height);

        Image background = new Image(textureRegion);
        background.setSize(width, height);
        addActor(background);

        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();


        FilledLabel numberLabel = new FilledLabel(width*0.75f, height*0.20f, 0.9f, 0.48f, "Startgate: " + gate,
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.WHITE));
        numberLabel.setPosition(width*0.25f, height*0.75f);
        addActor(numberLabel);
    }

}
