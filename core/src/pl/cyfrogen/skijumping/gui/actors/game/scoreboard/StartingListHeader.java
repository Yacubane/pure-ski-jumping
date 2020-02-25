package pl.cyfrogen.skijumping.gui.actors.game.scoreboard;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;


public class StartingListHeader extends Group {
    private final FilledLabel nameLabel;

    public StartingListHeader(float width, float height) {
        setSize(width, height);

        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        Image background = new Image(whiteDot);
        background.setColor(Color.valueOf("FFFFFF"));
        background.setSize(width, height);
        addActor(background);


        nameLabel = new FilledLabel(
                background.getWidth() * 0.5f,
                background.getHeight(),
                0.6f, 0.4f, "STARTING LIST",
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.BLACK));
        nameLabel.setPosition(height * 1.25f, 0);
        nameLabel.setAlignment(Align.left);
        addActor(nameLabel);


        FilledLabel meters1Label = new FilledLabel(
                background.getWidth() * 0.15f,
                background.getHeight(),
                0.9f, 0.4f, "METERS",
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.BLACK));
        meters1Label.setX(width * 0.5f);
        addActor(meters1Label);

        FilledLabel meters2Label = new FilledLabel(
                background.getWidth() * 0.15f,
                background.getHeight(),
                0.9f, 0.4f, "METERS",
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.BLACK));
        meters2Label.setX(width * 0.65f);
        addActor(meters2Label);

        FilledLabel pointsLabel = new FilledLabel(
                background.getWidth() * 0.2f - height,
                background.getHeight(),
                0.9f, 0.4f, "POINTS",
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.BLACK));
        pointsLabel.setX(width * 0.8f + height);
        addActor(pointsLabel);

    }

    public void setTitle(String headerTitle) {
        nameLabel.setText(headerTitle);
    }
}

