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
import pl.cyfrogen.skijumping.gui.utils.FontSizeUtils;

public class TutorialLabel extends Group {
    private final BitmapFont font;

    public TutorialLabel(String text) {
        float height = Gdx.graphics.getHeight() * 0.15f;
        float width = Gdx.graphics.getWidth() * 0.8f;

        font = Main.getInstance().getAssets().getFont();
        float textHeight = height * 0.35f;
        float fontScale = FontSizeUtils.calculateFontScaleByHeight(font, textHeight);

        float textWidth = Gdx.graphics.getWidth() * 0.75f;
        float paddingLeft = height * 0.3f;


        setSize(width, height);

        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        Image background = new Image(whiteDot);
        background.setColor(Color.valueOf("FFFFFF"));
        background.setSize(width, height);
        addActor(background);

        FilledLabel filledLabel = new FilledLabel(textWidth, textHeight, 1f, 1f, text,
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.BLACK));
        filledLabel.setPosition(paddingLeft, (height - textHeight) / 2f);
        addActor(filledLabel);


    }
}
