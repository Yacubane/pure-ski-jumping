package pl.cyfrogen.skijumping.gui.stage;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.List;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.asset.MenuAssets;
import pl.cyfrogen.skijumping.common.interfaces.OnEnd;
import pl.cyfrogen.skijumping.data.HillSetup;
import pl.cyfrogen.skijumping.data.JumperData;
import pl.cyfrogen.skijumping.game.data.GameData;
import pl.cyfrogen.skijumping.gui.actors.common.InfoDialog;
import pl.cyfrogen.skijumping.gui.actors.common.MenuButton;
import pl.cyfrogen.skijumping.gui.interfaces.OnReturnListener;
import pl.cyfrogen.skijumping.gui.utils.Direction;
import pl.cyfrogen.skijumping.gui.utils.MainMenuUtils;

public class WorldCupStage extends MenuStage {

    public WorldCupStage(final MainMenuController mainMenuController) {
        super(mainMenuController);

        TextureRegion soloTextureRegion = getMenuAssets().get(MenuAssets.Asset.START_TEXTURE, TextureRegion.class);
        TextureRegion helpTextureRegion = getMenuAssets().get(MenuAssets.Asset.COMPETITORS_TEXTURE, TextureRegion.class);
        TextureRegion hillsTextureRegion = getMenuAssets().get(MenuAssets.Asset.HILLS_TEXTURE, TextureRegion.class);

        MenuButton playButton = new MenuButton(soloTextureRegion);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                List<JumperData> jumperData = new ArrayList<JumperData>(Main.getInstance().getSaveData().getJumpers());
                List<HillSetup> hillsData = Main.getInstance().getSaveData().getWorldCupSetup().getHillSetups();
                for (int i = 0; i < jumperData.size(); i++) {
                    if (!jumperData.get(i).isActiveInWorldCup())
                        jumperData.remove(i--);
                }

                if (jumperData.size() == 0) {
                    new InfoDialog(WorldCupStage.this, "No jumpers",
                            "You haven't selected any jumper")
                            .show();
                } else if (hillsData.size() == 0) {
                    new InfoDialog(WorldCupStage.this, "No hills",
                            "You haven't selected any hill")
                            .show();
                } else {
                    hide();
                    getMenuController().closeMenuAndShowStage(
                            new WorldCupGameStage(new GameData(hillsData, jumperData, 2, new OnEnd() {
                                @Override
                                public void end() {
                                    getMenuController().reshow();
                                }
                            })));
                }
            }
        });


        MenuButton competitorsButton = new MenuButton(helpTextureRegion);
        competitorsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                MainMenuUtils.showStage(
                        getMenuController(),
                        new CompetitorsStage(getMenuController())
                                .withOnReturnListener(new OnReturnListener() {
                                    @Override
                                    public void returned(MenuStage menuStage) {
                                        show(Direction.RIGHT);
                                    }
                                }));
            }
        });

        MenuButton hillsButton = new MenuButton(hillsTextureRegion);
        hillsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                MainMenuUtils.showStage(
                        getMenuController(),
                        new HillsStage(getMenuController())
                                .withOnReturnListener(new OnReturnListener() {
                                    @Override
                                    public void returned(MenuStage menuStage) {
                                        show(Direction.RIGHT);
                                    }
                                }));
            }
        });

        addTiles(playButton, competitorsButton, hillsButton);
        addTitlebar("WORLD CUP");
        addBackButton();
        animateShowing(Direction.LEFT);

    }


    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void draw() {
        super.draw();
    }
}
