package pl.cyfrogen.skijumping.gui.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.asset.MenuAssets;
import pl.cyfrogen.skijumping.data.HillSetup;
import pl.cyfrogen.skijumping.data.WorldCupSetup;
import pl.cyfrogen.skijumping.gui.actors.common.FBOWidget;
import pl.cyfrogen.skijumping.gui.actors.common.MenuButton;
import pl.cyfrogen.skijumping.gui.actors.hill.HillWidget;
import pl.cyfrogen.skijumping.gui.interfaces.OnReturnListener;
import pl.cyfrogen.skijumping.gui.utils.Direction;
import pl.cyfrogen.skijumping.gui.utils.MainMenuUtils;
import pl.cyfrogen.skijumping.hill.HillFile;

public class HillsStage extends MenuStage {
    private final ScrollPane scrollPane;
    private float competitorWidth = Gdx.graphics.getWidth() * .7f;
    private float competitorHeight = Gdx.graphics.getHeight() * .12f;

    public HillsStage(MainMenuController mainMenuController) {
        super(mainMenuController);


        scrollPane = new ScrollPane(createContainer());
        scrollPane.setSize(competitorWidth, Gdx.graphics.getHeight() * 0.75f);
        scrollPane.setPosition((Gdx.graphics.getWidth() - competitorWidth) / 2f, 0f);

        addMenuActor(scrollPane, 0.12f);

        addTitlebar("HILLS");
        addBackButton();

        TextureRegion addTextureRegion = getMenuAssets().get(MenuAssets.Asset.ADD_BUTTON_TEXTURE,
                TextureRegion.class);

        MenuButton addButton = new MenuButton(addTextureRegion);
        float backButtonSize = Gdx.graphics.getHeight() * 0.15f;
        addButton.setSize(backButtonSize, backButtonSize);
        addButton.setPosition(Gdx.graphics.getWidth() - backButtonSize * 1.5f, backButtonSize * 0.25f);
        addButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainMenuUtils.showStage(
                        getMenuController(),
                        new ChooseHillStage(getMenuController(),
                                new ChooseHillStage.onHillSelected() {
                                    @Override
                                    public void hillSelected(HillSetup hillSetup) {
                                        Main.getInstance().getSaveData().getWorldCupSetup()
                                                .getHillSetups().add(hillSetup);
                                        Main.getInstance().getSaveData().saveWorldCupSetup();
                                        scrollPane.setActor(createContainer());
                                    }
                                })
                                .withOnReturnListener(new OnReturnListener() {
                                    @Override
                                    public void returned(MenuStage menuStage) {
                                        show(Direction.RIGHT);
                                    }
                                }));
                hide();
            }
        });
        addMenuActor(addButton, 0.3f);

        animateShowing(Direction.LEFT);
    }

    private Actor createContainer() {

        final WorldCupSetup worldCupSetup = Main.getInstance().getSaveData().getWorldCupSetup();


        List<HillWidget> hillWidgets = new ArrayList<HillWidget>();
        for (final HillSetup hillSetup : worldCupSetup.getHillSetups()) {
            try {
                final HillFile hillFile = new HillFile(hillSetup.getHillLocation());

                HillWidget hillWidget = new HillWidget(
                        hillFile,
                        hillSetup,
                        competitorWidth,
                        competitorHeight,
                        getMenuAssets());

                hillWidget.setOnDeleteClick(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        worldCupSetup.getHillSetups().remove(hillSetup);
                        Main.getInstance().getSaveData().saveWorldCupSetup();
                        scrollPane.setActor(createContainer());
                    }
                });

                hillWidget.setOnArrowUpClick(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        int prevIndex = worldCupSetup.getHillSetups().indexOf(hillSetup);
                        int newIndex = Math.max(0, prevIndex - 1);
                        worldCupSetup.getHillSetups().remove(hillSetup);
                        worldCupSetup.getHillSetups().add(newIndex, hillSetup);
                        Main.getInstance().getSaveData().saveWorldCupSetup();
                        scrollPane.setActor(createContainer());
                    }
                });

                hillWidget.setOnArrowDownClick(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        int prevIndex = worldCupSetup.getHillSetups().indexOf(hillSetup);
                        int newIndex = Math.min(worldCupSetup.getHillSetups().size() - 1, prevIndex + 1);
                        worldCupSetup.getHillSetups().remove(hillSetup);
                        worldCupSetup.getHillSetups().add(newIndex, hillSetup);
                        Main.getInstance().getSaveData().saveWorldCupSetup();
                        scrollPane.setActor(createContainer());
                    }
                });

                hillWidget.setOnEditClick(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        hide();
                        MainMenuUtils.showStage(
                                getMenuController(),
                                new HillSetupStage(hillFile, hillSetup, getMenuController(), null)
                                        .withOnReturnListener(new OnReturnListener() {
                                            @Override
                                            public void returned(MenuStage menuStage) {
                                                scrollPane.setActor(createContainer());
                                                Main.getInstance().getSaveData().saveWorldCupSetup();
                                                show(Direction.RIGHT);
                                            }
                                        }));


                    }
                });


                hillWidgets.add(hillWidget);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        Group group = group(competitorWidth, competitorHeight, hillWidgets.toArray(new HillWidget[0]));
        group.setTransform(false);
        FBOWidget FBOWidget = new FBOWidget();
        FBOWidget.setSize(group.getWidth(), group.getHeight());
        FBOWidget.addActor(group);

        Container<Actor> container = MainMenuUtils.createScrollPanelContainer(
                FBOWidget, Gdx.graphics.getHeight() * 0.75f);


        return container;
    }

    public Group group(float width, float oneWidgetHeight, Actor... actors) {
        Group group = new Group();
        group.setSize(width, oneWidgetHeight * actors.length);
        float y = group.getHeight();
        for (Actor actor : actors) {
            y -= actor.getHeight();
            actor.setPosition(0, y);
            group.addActor(actor);
        }
        return group;


    }


}
