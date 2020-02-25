package pl.cyfrogen.skijumping.gui.actors.competitors;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.asset.MenuAssets;
import pl.cyfrogen.skijumping.data.JumperData;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;
import pl.cyfrogen.skijumping.gui.actors.common.MenuButton;
import pl.cyfrogen.skijumping.gui.actors.common.MenuCheckbox;

public class CompetitorWidget extends Group {
    private final MenuAssets menuAssets;
    private final MenuButton button2;
    private final MenuButton button1;

    public CompetitorWidget(final JumperData jumperData, float width, float height, MenuAssets menuAssets) {
        setSize(width, height);
        this.menuAssets = menuAssets;

        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        Image background = new Image(whiteDot);
        background.setColor(Color.BLACK);
        background.setSize(width, height);
        addActor(background);

        MenuCheckbox checkbox = new MenuCheckbox(
                new TextureRegion(whiteDot),
                menuAssets.get(MenuAssets.Asset.CHECKBOX_CHECKED_TEXTURE, TextureRegion.class));
        checkbox.setChecked(jumperData.isActiveInWorldCup());
        checkbox.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                jumperData.setActiveInWorldCup(!jumperData.isActiveInWorldCup());
                Main.getInstance().getSaveData().saveJumpers();
            }
        });
        checkbox.setSize(height, height);
        addActor(checkbox);

        FilledLabel label = new FilledLabel(width * 0.4f, height,
                1f, 0.3f,
                jumperData.getName(),
                Main.getInstance().getAssets().getLabelStyle(Color.WHITE));
        FilledLabel.leftAlign(label);
        label.setPosition(height * 1.3f, 0);
        addActor(label);


        float padding = height * 0.4f;
        button1 = new MenuButton(
                menuAssets.get(MenuAssets.Asset.DELETE_ICON_TEXTURE, TextureRegion.class));
        button1.setSize(height, height);
        button1.setPosition(width - height - padding, 0);
        addActor(button1);

        button2 = new MenuButton(
                menuAssets.get(MenuAssets.Asset.EDIT_ICON_TEXTURE, TextureRegion.class));

        button2.setSize(height, height);
        button2.setPosition(button1.getX() - padding - height, 0);
        addActor(button2);

        TextureRegion typeTextureRegion = menuAssets.get(MenuAssets.Asset.NORMAL_PLAYER_ENABLED_TEXTURE, TextureRegion.class);
        if(jumperData.isCpuPlayer())
            typeTextureRegion = menuAssets.get(MenuAssets.Asset.CPU_PLAYER_ENABLED_TEXTURE, TextureRegion.class);

        Image type = new Image(typeTextureRegion);
        type.setSize(height, height);
        type.setPosition(button2.getX() - padding - height, 0);
        addActor(type);
    }

    public void setOnSettingsClick(ClickListener clickListener) {
        button2.addListener(clickListener);
    }

    @Override
    public void draw(Batch batch, float a) {
        super.draw(batch, a);
    }

    public void setOnDeleteClick(ClickListener clickListener) {
        button1.addListener(clickListener);
    }
}
