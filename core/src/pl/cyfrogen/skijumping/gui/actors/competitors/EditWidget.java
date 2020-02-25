package pl.cyfrogen.skijumping.gui.actors.competitors;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.asset.MenuAssets;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;

public class EditWidget extends Group {
    private final MenuAssets menuAssets;
    private final Group placeholder;

    public EditWidget(String name, float width, float height, MenuAssets menuAssets) {
        setSize(width, height);
        this.menuAssets = menuAssets;
        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();

        Image background = new Image(whiteDot);
        background.setColor(Color.valueOf("000000"));
        background.setSize(width, height);
        addActor(background);

        Image whiteBackground = new Image(whiteDot);
        whiteBackground.setColor(Color.valueOf("FFFFFF"));
        whiteBackground.setSize(width * 0.3f, height);
        addActor(whiteBackground);

        FilledLabel label = new FilledLabel(width * 0.3f, height,
                1f, 0.3f,
                name,
                Main.getInstance().getAssets().getLabelStyle(Color.BLACK));
        label.setPosition(0, 0);
        addActor(label);

        placeholder = new Group();
        placeholder.setSize(width * 0.7f, height);
        placeholder.setPosition(width * 0.3f, 0);
        addActor(placeholder);

    }

    public Group getPlaceholder() {
        return placeholder;
    }
}
