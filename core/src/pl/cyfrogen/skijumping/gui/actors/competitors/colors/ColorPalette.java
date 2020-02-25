package pl.cyfrogen.skijumping.gui.actors.competitors.colors;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.gui.actors.common.MenuButton;

import static pl.cyfrogen.skijumping.gui.utils.MainMenuUtils.createScrollPanelContainer;

public class ColorPalette extends Group {

    public ColorPalette(Color defaultColor,
                        Color[] colorsToChoose, float width, float height, int columns,
                        final OnColorSelected onColorUpdate) {
        setSize(width, height);
        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();

        Group colors = new Group();
        float oneColorWidth = width / (float) columns;
        colors.setSize(width, (float) Math.ceil(colorsToChoose.length / (float) columns) * oneColorWidth);
        for (int i = 0; i < colorsToChoose.length; i++) {
            final MenuButton imageButton = new MenuButton(new TextureRegion(whiteDot));
            imageButton.setColor(colorsToChoose[i]);
            imageButton.setSize(oneColorWidth, oneColorWidth);
            imageButton.setPosition(
                    oneColorWidth * (i % columns),
                    colors.getHeight() - oneColorWidth * (1 + i / columns));
            colors.addActor(imageButton);

            imageButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    onColorUpdate.colorSelected(imageButton.getColor());
                }
            });
        }

        ScrollPane scrollPane = new ScrollPane(createScrollPanelContainer(colors, height));
        scrollPane.setSize(width, height);
        addActor(scrollPane);

    }
}
