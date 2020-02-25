package pl.cyfrogen.skijumping.gui.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.List;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.asset.MenuAssets;
import pl.cyfrogen.skijumping.data.JumperData;
import pl.cyfrogen.skijumping.gui.actors.common.FBOWidget;
import pl.cyfrogen.skijumping.gui.actors.common.MenuButton;
import pl.cyfrogen.skijumping.gui.actors.competitors.CompetitorWidget;
import pl.cyfrogen.skijumping.gui.interfaces.OnReturnListener;
import pl.cyfrogen.skijumping.gui.utils.Direction;
import pl.cyfrogen.skijumping.gui.utils.MainMenuUtils;

public class CompetitorsStage extends MenuStage {
    private final ScrollPane scrollPane;
    private float competitorWidth = Gdx.graphics.getWidth() * .7f;
    private float competitorHeight = Gdx.graphics.getHeight() * .12f;

    public CompetitorsStage(MainMenuController mainMenuController) {
        super(mainMenuController);


        scrollPane = new ScrollPane(createContainer());
        scrollPane.setSize(competitorWidth, Gdx.graphics.getHeight() * 0.75f);
        scrollPane.setPosition((Gdx.graphics.getWidth() - competitorWidth) / 2f, 0f);

        addMenuActor(scrollPane, 0.12f);

        addTitlebar("COMPETITORS");
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
                Main.getInstance().getSaveData().getJumpers().add(0, new JumperData("New JUMPER"));
                Main.getInstance().getSaveData().saveJumpers();
                scrollPane.setActor(createContainer());
                scrollPane.scrollTo(0,scrollPane.getActor().getHeight(),scrollPane.getWidth(), scrollPane.getHeight());

            }
        });
        addMenuActor(addButton, 0.3f);

        animateShowing(Direction.LEFT);
    }

    private Actor createContainer() {

        final List<JumperData> jumpers = Main.getInstance().getSaveData().getJumpers();

        List<CompetitorWidget> competitorWidgets = new ArrayList<CompetitorWidget>();
        for (final JumperData jumperData : jumpers) {
            CompetitorWidget competitorWidget = new CompetitorWidget(
                    jumperData,
                    competitorWidth,
                    competitorHeight,
                    getMenuAssets());

            competitorWidget.setOnDeleteClick(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    jumpers.remove(jumperData);
                    Main.getInstance().getSaveData().saveJumpers();
                    scrollPane.setActor(createContainer());
                }
            });

            competitorWidget.setOnSettingsClick(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    hide();
                    MainMenuUtils.showStage(
                            getMenuController(),
                            new CompetitorStage(jumperData, getMenuController())
                                    .withOnReturnListener(new OnReturnListener() {
                                        @Override
                                        public void returned(MenuStage menuStage) {
                                            scrollPane.setActor(createContainer());
                                            Main.getInstance().getSaveData().saveJumpers();
                                            show(Direction.RIGHT);
                                        }
                                    }));


                }
            });


            competitorWidgets.add(competitorWidget);
        }


        Group group = group(competitorWidth, competitorHeight, competitorWidgets.toArray(new CompetitorWidget[0]));
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
