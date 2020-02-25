package pl.cyfrogen.skijumping.gui.actors.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;


public class JudgeScoreWidget extends Group {
    public JudgeScoreWidget(float width, float height, int score) {
        setSize(width, height);

        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        Image background = new Image(whiteDot);
        background.setColor(Color.valueOf("161616"));
        background.setSize(width, height);
        addActor(background);

        String scoreString = score / 10 + "." + score % 10;
        FilledLabel numberLabel = new FilledLabel(background, 0.9f, 0.4f, scoreString,
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.WHITE));
        addActor(numberLabel);

    }
}
