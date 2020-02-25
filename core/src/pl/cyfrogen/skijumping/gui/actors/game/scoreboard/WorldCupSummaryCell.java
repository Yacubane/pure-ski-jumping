package pl.cyfrogen.skijumping.gui.actors.game.scoreboard;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.data.WorldCupJumperData;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;


public class WorldCupSummaryCell extends Group {
    public WorldCupSummaryCell(int hillNum, WorldCupJumperData worldCupJumperData, float width, float height) {
        setSize(width, height);

        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        Image background = new Image(whiteDot);
        background.setColor(Color.valueOf("161616"));
        background.setSize(width, height);
        addActor(background);

        Image whiteBackground = new Image(whiteDot);
        whiteBackground.setColor(Color.valueOf("1dd1a1"));
        whiteBackground.setSize(height, height);
        addActor(whiteBackground);

        FilledLabel numberLabel = new FilledLabel(whiteBackground, 0.9f, 0.4f,
                String.valueOf(worldCupJumperData.getWorldCupPlace()),
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.BLACK));
        addActor(numberLabel);

        FilledLabel nameLabel = new FilledLabel(
                background.getWidth() * 0.5f,
                background.getHeight(),
                0.6f, 0.4f, worldCupJumperData.getJumperData().getName(),
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.WHITE));
        nameLabel.setPosition(whiteBackground.getX()
                + whiteBackground.getWidth()
                + whiteBackground.getWidth() * 0.25f, 0);
        nameLabel.setAlignment(Align.left);
        addActor(nameLabel);

        FilledLabel pointsLabel = new FilledLabel(
                background.getWidth() * 0.2f - height,
                background.getHeight(),
                0.9f, 0.4f, String.valueOf(worldCupJumperData.getWorldCupPoints()),
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.WHITE));
        pointsLabel.setX(width * 0.8f + height);
        addActor(pointsLabel);
    }
}

