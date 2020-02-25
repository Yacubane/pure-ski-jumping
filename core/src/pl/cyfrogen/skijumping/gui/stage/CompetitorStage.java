package pl.cyfrogen.skijumping.gui.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.asset.MenuAssets;
import pl.cyfrogen.skijumping.data.JumperData;
import pl.cyfrogen.skijumping.gui.actors.common.FBOWidget;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;
import pl.cyfrogen.skijumping.gui.actors.common.MenuButton;
import pl.cyfrogen.skijumping.gui.actors.competitors.EditWidget;
import pl.cyfrogen.skijumping.gui.actors.competitors.JumperWidget;
import pl.cyfrogen.skijumping.gui.interfaces.OnReturnListener;
import pl.cyfrogen.skijumping.gui.utils.Direction;
import pl.cyfrogen.skijumping.gui.utils.MainMenuUtils;
import pl.cyfrogen.skijumping.gui.utils.MenuAnimations;
import pl.cyfrogen.skijumping.gui.utils.OnDisposeListener;

public class CompetitorStage extends MenuStage {


    private final JumperWidget jumperWidget;

    public CompetitorStage(final JumperData jumperData, final MainMenuController mainMenuController) {
        super(mainMenuController);


        float competitorWidth = Gdx.graphics.getWidth() * .45f;
        float competitorHeight = Gdx.graphics.getHeight() * .12f;

        jumperWidget = new JumperWidget(jumperData, competitorWidth, Gdx.graphics.getHeight() * 0.5f);
        jumperWidget.setPosition(Gdx.graphics.getWidth() - competitorWidth,
                (Gdx.graphics.getHeight() * 0.7f - Gdx.graphics.getHeight() * 0.5f) / 2f);


        addActor(jumperWidget);
        MenuAnimations.apply(MenuAnimations.showingAnimation()
                        .withDirection(Direction.LEFT)
                        .withStartIdleTime(0.1f),
                jumperWidget);


        EditWidget editWidget = new EditWidget("Name", competitorWidth, competitorHeight, mainMenuController.getMenuAssets());
        EditWidget editWidget2 = new EditWidget("Type", competitorWidth, competitorHeight, mainMenuController.getMenuAssets());
        EditWidget editWidget3 = new EditWidget("Colors", competitorWidth, competitorHeight, mainMenuController.getMenuAssets());

        Group editNamePlaceholder = editWidget.getPlaceholder();

        MenuButton editNameButton = new MenuButton(
                mainMenuController.getMenuAssets().get(MenuAssets.Asset.EDIT_ICON_TEXTURE, TextureRegion.class));

        editNameButton.setSize(editNamePlaceholder.getHeight(), editNamePlaceholder.getHeight());
        editNameButton.setPosition(editNamePlaceholder.getWidth() - editNameButton.getWidth(), 0);
        editNamePlaceholder.addActor(editNameButton);


        final FilledLabel name = new FilledLabel(
                editNamePlaceholder.getWidth() - editNameButton.getWidth(),
                editNamePlaceholder.getHeight(),
                1f, 0.3f,
                jumperData.getName(),
                Main.getInstance().getAssets().getLabelStyle(Color.WHITE));
        name.setPosition(0, 0);


        editNameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.getTextInput(new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        jumperData.setName(text);
                        name.setText(text);
                    }

                    @Override
                    public void canceled() {

                    }
                }, "Change jumper's name", jumperData.getName(), "");
            }
        });


        editNamePlaceholder.addActor(name);

        Group editTypePlaceholder = editWidget2.getPlaceholder();

        final MenuButton cpuType = new MenuButton(
                mainMenuController.getMenuAssets().get(MenuAssets.Asset.CPU_PLAYER_DISABLED_TEXTURE, TextureRegion.class));
        final MenuButton playerType = new MenuButton(
                mainMenuController.getMenuAssets().get(MenuAssets.Asset.NORMAL_PLAYER_DISABLED_TEXTURE, TextureRegion.class));

        cpuType.setSize(competitorHeight, competitorHeight);
        cpuType.setPosition(editTypePlaceholder.getWidth() / 2f - cpuType.getWidth(), 0);
        playerType.setSize(competitorHeight, competitorHeight);
        playerType.setPosition(editTypePlaceholder.getWidth() / 2f, 0);

        editTypePlaceholder.addActor(cpuType);
        editTypePlaceholder.addActor(playerType);


        cpuType.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cpuType.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.CPU_PLAYER_ENABLED_TEXTURE, TextureRegion.class));
                playerType.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.NORMAL_PLAYER_DISABLED_TEXTURE, TextureRegion.class));
                jumperData.setCpuPlayer(true);
            }
        });

        playerType.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cpuType.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.CPU_PLAYER_DISABLED_TEXTURE, TextureRegion.class));
                playerType.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.NORMAL_PLAYER_ENABLED_TEXTURE, TextureRegion.class));
                jumperData.setCpuPlayer(false);
            }
        });

        if (jumperData.isCpuPlayer()) {
            cpuType.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.CPU_PLAYER_ENABLED_TEXTURE, TextureRegion.class));
            playerType.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.NORMAL_PLAYER_DISABLED_TEXTURE, TextureRegion.class));
        } else {
            cpuType.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.CPU_PLAYER_DISABLED_TEXTURE, TextureRegion.class));
            playerType.setTexture(mainMenuController.getMenuAssets().get(MenuAssets.Asset.NORMAL_PLAYER_ENABLED_TEXTURE, TextureRegion.class));
        }

        Group editColorPlaceholder = editWidget3.getPlaceholder();

        MenuButton editColorButton = new MenuButton(
                mainMenuController.getMenuAssets().get(MenuAssets.Asset.EDIT_ICON_TEXTURE, TextureRegion.class));

        editColorButton.setSize(editColorPlaceholder.getHeight(), editColorPlaceholder.getHeight());
        editColorButton.setPosition((editColorPlaceholder.getWidth() - editColorButton.getWidth()) / 2f, 0);
        editColorPlaceholder.addActor(editColorButton);

        editColorButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                MainMenuUtils.showStage(
                        mainMenuController,
                        new CompetitorColorStage(jumperWidget, jumperData, getMenuController())
                                .withOnReturnListener(new OnReturnListener() {
                                    @Override
                                    public void returned(MenuStage menuStage) {
                                        show(Direction.RIGHT);
                                    }
                                }));
            }
        });


        Group group = group(competitorWidth, competitorHeight, editWidget, editWidget3);

        Container<Actor> container = MainMenuUtils.createScrollPanelContainer(
                group, Gdx.graphics.getHeight() * 0.75f);

        ScrollPane scrollPane = new ScrollPane(FBOWidget.of(container));
        scrollPane.setSize(competitorWidth, Gdx.graphics.getHeight() * 0.75f);
        scrollPane.setPosition(Gdx.graphics.getWidth() * 0.05f, 0f);

        addMenuActor(scrollPane, 0.12f);


        addTitlebar("COMPETITOR");
        addBackButton();
        animateShowing(Direction.LEFT);

        setOnClose(new OnDisposeListener() {

            @Override
            public void disposed() {
                jumperWidget.dispose();
            }
        });
    }

    @Override
    void backPressed() {
        super.backPressed();
        MenuAnimations.apply(MenuAnimations.hidingAnimation()
                        .withDirection(Direction.RIGHT)
                        .withStartIdleTime(0.1f),
                jumperWidget);
    }

    public Group group(float width, float oneWidgetHeight, Actor... actors) {
        Group group = new Group();
        group.setSize(width, oneWidgetHeight * actors.length);
        float y = group.getHeight();
        for (Actor actor : actors) {
            group.addActor(actor);
            y -= actor.getHeight();
            actor.setPosition(0, y);
            group.addActor(actor);
        }
        return group;
    }


}
