package pl.cyfrogen.skijumping.gui.actors.tutorial.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;

public class TutorialDoubleLabel extends Group {
    private final BitmapFont font;

    public TutorialDoubleLabel(String line1, String line2) {
        float height = Gdx.graphics.getHeight() * 0.25f;
        float width = Gdx.graphics.getWidth() * 0.8f;

        font = Main.getInstance().getAssets().getFont();
        float textHeight = height * 0.35f / (0.25f/0.15f) * 2;


        setSize(width, height);

        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        Image background = new Image(whiteDot);
        background.setColor(Color.valueOf("FFFFFF"));
        background.setSize(width, height);
        addActor(background);

        FilledLabel filledLabel = new FilledLabel(background.getWidth(), textHeight / 2f, 1f, 0.75f, line2,
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.BLACK));
        filledLabel.setPosition(0, height * 0.333f - textHeight/4f);
        addActor(filledLabel);

        FilledLabel filledLabel2 = new FilledLabel(background.getWidth(), textHeight / 2f, 1f, 1f, line1,
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.BLACK));
        filledLabel2.setPosition(0, height * 0.666f - textHeight/4f);
        addActor(filledLabel2);

    }
}
