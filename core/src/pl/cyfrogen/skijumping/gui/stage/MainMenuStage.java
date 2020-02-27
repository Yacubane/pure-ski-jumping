package pl.cyfrogen.skijumping.gui.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.asset.MenuAssets;
import pl.cyfrogen.skijumping.gui.actors.common.AskDialog;
import pl.cyfrogen.skijumping.gui.actors.common.InfoDialog;
import pl.cyfrogen.skijumping.gui.actors.common.MenuButton;
import pl.cyfrogen.skijumping.gui.actors.common.TitleBar;
import pl.cyfrogen.skijumping.gui.actors.menu.SlideText;
import pl.cyfrogen.skijumping.gui.interfaces.OnOkClick;
import pl.cyfrogen.skijumping.gui.interfaces.OnReturnListener;
import pl.cyfrogen.skijumping.gui.utils.Direction;
import pl.cyfrogen.skijumping.gui.utils.MainMenuUtils;

public class MainMenuStage extends MenuStage {


    public MainMenuStage(final MainMenuController mainMenuController) {
        super(mainMenuController);

        Music music = getMenuAssets().get(MenuAssets.Asset.MENU_THEME, Music.class);
        music.play();


        TextureRegion soloTextureRegion = getMenuAssets().get(MenuAssets.Asset.WORLD_CUP_TEXTURE,
                TextureRegion.class);
        TextureRegion multiplayerTextureRegion = getMenuAssets().get(MenuAssets.Asset.ONLINE_TEXTURE,
                TextureRegion.class);
        TextureRegion helpTextureRegion = getMenuAssets().get(MenuAssets.Asset.HELP_TEXTURE,
                TextureRegion.class);
        TextureRegion exitTextureRegion = getMenuAssets().get(MenuAssets.Asset.EXIT_TEXTURE,
                TextureRegion.class);

        MenuButton button1 = new MenuButton(soloTextureRegion);
        button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Main.getInstance().getPreferences().getBoolean("tutorialDialogShown")) {
                    hide();
                    MainMenuUtils.showStage(
                            mainMenuController,
                            new WorldCupStage(getMenuController())
                                    .withOnReturnListener(new OnReturnListener() {
                                        @Override
                                        public void returned(MenuStage menuStage) {
                                            show(Direction.RIGHT);
                                        }
                                    }));
                } else {
                    new AskDialog(MainMenuStage.this, "Tutorial",
                            "Do you want to play tutorial?",
                            "Yes",
                            "No")
                            .withOnLeftClick(new OnOkClick() {
                                @Override
                                public void okClick() {
                                    hide();
                                    HelpStage.playTutorial(getMenuController());
                                }
                            })
                            .withOnRightClick(new OnOkClick() {
                                @Override
                                public void okClick() {
                                    new InfoDialog(MainMenuStage.this, "Tutorial", "You can always access this tutorial in:\n main menu > help > tutorial")
                                            .withOnOkClickAction(new OnOkClick() {
                                                @Override
                                                public void okClick() {
                                                    hide();
                                                    MainMenuUtils.showStage(
                                                            mainMenuController,
                                                            new WorldCupStage(getMenuController())
                                                                    .withOnReturnListener(new OnReturnListener() {
                                                                        @Override
                                                                        public void returned(MenuStage menuStage) {
                                                                            show(Direction.RIGHT);
                                                                        }
                                                                    }));
                                                }
                                            })
                                            .show();
                                }
                            })
                            .show();
                    Main.getInstance().getPreferences().putBoolean("tutorialDialogShown", true);
                    Main.getInstance().getPreferences().flush();

                }

            }
        });


        MenuButton button2 = new MenuButton(multiplayerTextureRegion);
        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Main.getInstance().getPlatformAPI().areLeaderboardsSupported()) {
                    hide();

                    MainMenuUtils.showStage(
                            mainMenuController,
                            new OnlineStage(getMenuController())
                                    .withOnReturnListener(new OnReturnListener() {
                                        @Override
                                        public void returned(MenuStage menuStage) {
                                            show(Direction.RIGHT);
                                        }
                                    }));
                } else {
                    new InfoDialog(MainMenuStage.this, "Error", 0.04f,
                            "Leaderboards are not available in FLOSS version of game (without Google Play Services).")
                            .show();
                }
            }
        });

        MenuButton button3 = new MenuButton(helpTextureRegion);
        button3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();

                MainMenuUtils.showStage(
                        mainMenuController,
                        new HelpStage(getMenuController())
                                .withOnReturnListener(new OnReturnListener() {
                                    @Override
                                    public void returned(MenuStage menuStage) {
                                        show(Direction.RIGHT);
                                    }
                                }));
            }
        });

        MenuButton button4 = new MenuButton(exitTextureRegion);
        button4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });


        addTitlebar("MAIN MENU");

        TitleBar alphaBanner = new TitleBar("ALPHA VERSION", Gdx.graphics.getHeight() * 0.08f);
        alphaBanner.setPosition((Gdx.graphics.getWidth() - alphaBanner.getWidth()), Gdx.graphics.getHeight() - alphaBanner.getHeight() * 1.25f);
        addMenuActor(alphaBanner, 0.12f);

        ScrollPane scrollPane = addTiles(button1, button2, button3, button4);
        float BOTTOM_TEXT_SIZE = Gdx.graphics.getHeight() * 0.07f;
        float BUTTON_TEXT_PADDING = Gdx.graphics.getHeight() * 0.025f;

        boolean bannerActive = false;

        if (bannerActive) {
            scrollPane.setY(scrollPane.getY() + BOTTOM_TEXT_SIZE + BUTTON_TEXT_PADDING);
            String slideTextValue =
                    "We know that you want more hills, but we have a lot of work at the moment during studies and not only. We will work on this game in February and you can expect new hill in mid/end of February. You will get notification on your phone when new hill is ready! Thank for your support, understanding and patience! Cyfrogen.";
            SlideText slideText = new SlideText(slideTextValue, Gdx.graphics.getWidth(), BOTTOM_TEXT_SIZE);

            addMenuActor(slideText, 0.2f);

        }

        animateShowing(Direction.LEFT);


    }

    @Override
    public boolean keyDown(final int keycode) {
        if (keycode == Input.Keys.BACK) {
            Gdx.app.exit();
        }
        return super.keyDown(keycode);
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
