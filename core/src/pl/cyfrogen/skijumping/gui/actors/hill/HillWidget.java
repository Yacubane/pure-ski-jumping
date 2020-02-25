package pl.cyfrogen.skijumping.gui.actors.hill;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.asset.MenuAssets;
import pl.cyfrogen.skijumping.data.HillSetup;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;
import pl.cyfrogen.skijumping.gui.actors.common.MenuButton;
import pl.cyfrogen.skijumping.hill.HillFile;

public class HillWidget extends Group {
    private final MenuAssets menuAssets;
    private final MenuButton editIcon;
    private final MenuButton deleteButton;
    private final MenuButton arrowDownButton;
    private final MenuButton arrowUpButton;

    public HillWidget(final HillFile hillFile, final HillSetup jumperData, float width, float height, MenuAssets menuAssets) {
        setSize(width, height);
        this.menuAssets = menuAssets;

        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        Image background = new Image(whiteDot);
        background.setColor(Color.BLACK);
        background.setSize(width, height);
        addActor(background);


        FilledLabel label = new FilledLabel(width * 0.5f, height,
                1f, 0.3f,
                hillFile.getMetaData().get("name").textValue().toUpperCase(),
                Main.getInstance().getAssets().getLabelStyle(Color.WHITE));
        FilledLabel.leftAlign(label);
        label.setPosition(height * 0.35f, 0);
        addActor(label);


        float padding = height * 0.4f;
        deleteButton = new MenuButton(
                menuAssets.get(MenuAssets.Asset.DELETE_ICON_TEXTURE, TextureRegion.class));
        deleteButton.setSize(height, height);
        deleteButton.setPosition(width - height - padding, 0);
        addActor(deleteButton);

        editIcon = new MenuButton(
                menuAssets.get(MenuAssets.Asset.EDIT_ICON_TEXTURE, TextureRegion.class));

        editIcon.setSize(height, height);
        editIcon.setPosition(deleteButton.getX() - padding - height, 0);
        addActor(editIcon);

        arrowDownButton = new MenuButton(
                menuAssets.get(MenuAssets.Asset.ARROW_DOWN_TEXTURE, TextureRegion.class));
        arrowDownButton.setPosition(editIcon.getX() - padding - height, 0);
        arrowDownButton.setSize(height, height);
        addActor(arrowDownButton);

        arrowUpButton = new MenuButton(
                menuAssets.get(MenuAssets.Asset.ARROW_UP_TEXTURE, TextureRegion.class));
        arrowUpButton.setSize(height, height);
        arrowUpButton.setPosition(arrowDownButton.getX() - padding - height, 0);
        addActor(arrowUpButton);
    }

    public void setOnEditClick(ClickListener clickListener) {
        editIcon.addListener(clickListener);
    }

    @Override
    public void draw(Batch batch, float a) {
        super.draw(batch, a);
    }

    public void setOnDeleteClick(ClickListener clickListener) {
        deleteButton.addListener(clickListener);
    }

    public void setOnArrowUpClick(ClickListener clickListener) {
        arrowUpButton.addListener(clickListener);
    }

    public void setOnArrowDownClick(ClickListener clickListener) {
        arrowDownButton.addListener(clickListener);
    }
}
