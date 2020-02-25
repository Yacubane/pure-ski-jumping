package pl.cyfrogen.skijumping.gui.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.asset.MenuAssets;
import pl.cyfrogen.skijumping.data.JumperData;
import pl.cyfrogen.skijumping.gui.actors.common.InfoDialog;
import pl.cyfrogen.skijumping.gui.actors.common.MenuButton;
import pl.cyfrogen.skijumping.gui.utils.Direction;
import pl.cyfrogen.skijumping.jumper.OnJumperLanded;
import pl.cyfrogen.skijumping.platform.LeaderboardListener;
import pl.cyfrogen.skijumping.platform.SignInListener;

public class OnlineStage extends MenuStage {

    public OnlineStage(final MainMenuController mainMenuController) {
        super(mainMenuController);

        TextureRegion soloTextureRegion = getMenuAssets().get(MenuAssets.Asset.GO_FOR_RECORD, TextureRegion.class);
        TextureRegion leaderboardsTextureRegion = getMenuAssets().get(MenuAssets.Asset.LEADERBOARDS_TEXTURE, TextureRegion.class);
        TextureRegion helpTextureRegion = getMenuAssets().get(MenuAssets.Asset.HELP_TEXTURE, TextureRegion.class);

        MenuButton playButton = new MenuButton(soloTextureRegion);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ensureLoggedIn(new SignInListener() {


                    @Override
                    public void success() {
                        hide();

                        JumperData jumperData = new JumperData();
                        jumperData.setName("Player");
                        jumperData.getColors().getHelmetColor().setColor(Color.valueOf("f1c40f"));
                        jumperData.getColors().getGoggleBandColor().setColor(Color.valueOf("ecf0f1"));
                        jumperData.getColors().getGoggleColor().setColor(Color.valueOf("e67e22"));
                        jumperData.getColors().getArmLeftColor().setColor(Color.valueOf("3498db"));
                        jumperData.getColors().getBodyLeftColor().setColor(Color.valueOf("3498db"));
                        jumperData.getColors().getArmRightColor().setColor(Color.valueOf("ecf0f1"));
                        jumperData.getColors().getBodyRightColor().setColor(Color.valueOf("ecf0f1"));
                        jumperData.getColors().getBootColor().setColor(Color.valueOf("bdc3c7"));
                        jumperData.getColors().getGloveColor().setColor(Color.valueOf("bdc3c6"));
                        jumperData.getColors().getSkiColor().setColor(Color.valueOf("2ecc71"));

                        getMenuController().closeMenuAndShowStage(new OnlineGameStage(jumperData)
                                .withOnJumperLanded(new OnJumperLanded() {

                                    @Override
                                    public void jumperLanded(boolean crash, double distance, double points) {
                                        if (!crash) {
                                            int score = (int) (distance * 100);
                                            Main.getInstance().getPlatformAPI().submitScore("CgkI2Lm9gaMMEAIQAg", score);
                                        }
                                    }
                                }));

                    }


                    @Override
                    public void error() {
                        new InfoDialog(OnlineStage.this, "Login",
                                "Login error")
                                .show();
                    }
                });


            }
        });


        MenuButton competitorsButton = new MenuButton(leaderboardsTextureRegion);
        competitorsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ensureLoggedIn(new SignInListener() {

                    @Override
                    public void error() {
                        new InfoDialog(OnlineStage.this, "Login",
                                "Login error")
                                .show();
                    }

                    @Override
                    public void success() {
                        Main.getInstance().getPlatformAPI().showLeaderboard("CgkI2Lm9gaMMEAIQAg",
                                new LeaderboardListener() {
                                    @Override
                                    public void success() {

                                    }

                                    @Override
                                    public void error() {
                                        new InfoDialog(OnlineStage.this, "Leaderboards",
                                                "Leaderboards show error")
                                                .show();
                                    }
                                });
                    }
                });


            }
        });

        MenuButton hillsButton = new MenuButton(helpTextureRegion);
        hillsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new InfoDialog(OnlineStage.this, "Online mode",
                        "Online modes are pretty limited right now - there are only online leaderboards. \n" +
                                "You can only be placed in leaderboard if you play gamemode from this menu. \n" +
                                " This gamemode is equal for every player with same conditions for every player (no wind, etc.)")
                        .show();
            }
        });

        addTiles(playButton, competitorsButton, hillsButton);
        addTitlebar("ONLINE");
        addBackButton();
        animateShowing(Direction.LEFT);

    }

    private void ensureLoggedIn(final SignInListener signInListener) {
        if (Main.getInstance().getPlatformAPI().isSignedIn()) {
            signInListener.success();
        } else {
            Main.getInstance().getPlatformAPI().signInAsync(new SignInListener() {
                @Override
                public void success() {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            signInListener.success();
                        }
                    });
                }

                @Override
                public void error() {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            signInListener.error();
                        }
                    });
                }

            });
        }
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
