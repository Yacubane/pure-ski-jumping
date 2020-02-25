package pl.cyfrogen.skijumping.gui.actors.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;


public class ScoreWidget extends Group {
    public ScoreWidget(String jumperNumber, String jumperName,
                       String distance1, String distance2,
                       String jumperPlace, String points) {
        setTouchable(Touchable.disabled);
        float height = Gdx.graphics.getHeight() * 0.09f;
        float width = Gdx.graphics.getWidth() * 0.8f;

        setSize(width, height);

        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        Image background = new Image(whiteDot);
        background.setColor(Color.valueOf("161616"));
        background.setSize(width, height);
        addActor(background);

        Image whiteBackground = new Image(whiteDot);
        whiteBackground.setColor(Color.valueOf("e7e7e7"));
        whiteBackground.setSize(height, height);
        addActor(whiteBackground);

        Image cyanBackground = new Image(whiteDot);
        cyanBackground.setColor(Color.valueOf("1dd1a1"));
        cyanBackground.setSize(height, height);
        cyanBackground.setX(width * 0.8f);
        addActor(cyanBackground);

        FilledLabel numberLabel = new FilledLabel(whiteBackground, 0.9f, 0.4f, jumperNumber,
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.BLACK));
        addActor(numberLabel);

        FilledLabel placeLabel = new FilledLabel(cyanBackground, 0.9f, 0.4f, jumperPlace,
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.BLACK));
        addActor(placeLabel);

        FilledLabel nameLabel = new FilledLabel(
                background.getWidth() * 0.5f,
                background.getHeight(),
                0.6f, 0.4f, jumperName,
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.WHITE));
        nameLabel.setPosition(whiteBackground.getX()
                + whiteBackground.getWidth()
                + whiteBackground.getWidth() * 0.25f, 0);
        nameLabel.setAlignment(Align.left);
        addActor(nameLabel);



        FilledLabel meters1Label = new FilledLabel(
                background.getWidth() * 0.15f,
                background.getHeight(),
                0.9f, 0.4f, distance1,
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.WHITE));
        meters1Label.setX(width * 0.5f);
        addActor(meters1Label);

        FilledLabel meters2Label = new FilledLabel(
                background.getWidth() * 0.15f,
                background.getHeight(),
                0.9f, 0.4f, distance2,
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.WHITE));
        meters2Label.setX(width * 0.65f);
        addActor(meters2Label);

        FilledLabel pointsLabel = new FilledLabel(
                background.getWidth() * 0.2f - cyanBackground.getWidth(),
                background.getHeight(),
                0.9f, 0.4f, points,
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.WHITE));
        pointsLabel.setX(width * 0.8f + cyanBackground.getWidth());
        addActor(pointsLabel);

    }
}
