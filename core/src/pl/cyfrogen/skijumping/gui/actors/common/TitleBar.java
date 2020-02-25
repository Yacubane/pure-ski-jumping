package pl.cyfrogen.skijumping.gui.actors.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.gui.utils.FontSizeUtils;

public class TitleBar extends Group {
    private final BitmapFont font;

    public TitleBar(String text) {
        this(text, Gdx.graphics.getHeight() * 0.15f);
    }

    public TitleBar(String text, float height) {
        font = Main.getInstance().getAssets().getFont();
        float textHeight = height * 0.4f;
        float fontScale = FontSizeUtils.calculateFontScaleByHeight(font, textHeight);

        float textWidth = FontSizeUtils.calculateFontWidth(font, text, fontScale);
        float paddingLeft = height * 0.3f;

        float totalWidth = paddingLeft + textWidth + paddingLeft;

        setSize(totalWidth, height);

        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        Image background = new Image(whiteDot);
        background.setColor(Color.valueOf("000000"));
        background.setSize(totalWidth, height);
        addActor(background);

        FilledLabel filledLabel = new FilledLabel(textWidth, textHeight, 1f, 1f, text,
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.WHITE));
        filledLabel.setPosition(paddingLeft, (height - textHeight) / 2f);
        addActor(filledLabel);


    }
}
