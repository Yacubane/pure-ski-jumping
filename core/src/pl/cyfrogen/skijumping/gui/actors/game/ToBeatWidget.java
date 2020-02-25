package pl.cyfrogen.skijumping.gui.actors.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;


public class ToBeatWidget extends Group {
    final float WIDTH_HEIGHT_RATIO = 3.45f / 0.66f;

    public ToBeatWidget(int distance) {


        setTouchable(Touchable.disabled);
        float width = Gdx.graphics.getWidth() * 0.16f;
        float height = width / WIDTH_HEIGHT_RATIO;

        setSize(width, height);

        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        Image background = new Image(whiteDot);
        background.setColor(Color.valueOf("161616"));
        background.setSize(width, height);
        addActor(background);

        float padding = height * 0.25f;
        FilledLabel numberLabel = new FilledLabel(width - padding, height, 0.8f, 0.42f, "To beat: " + distance / 100 + "." + distance % 100 + " m",
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.WHITE));
        FilledLabel.leftAlign(numberLabel);
        numberLabel.setX(padding);
        addActor(numberLabel);
    }

}
