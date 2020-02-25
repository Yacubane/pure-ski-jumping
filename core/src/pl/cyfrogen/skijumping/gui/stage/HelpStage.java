package pl.cyfrogen.skijumping.gui.stage;

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
import pl.cyfrogen.skijumping.platform.SignInListener;

public class HelpStage extends MenuStage {
    public static void playTutorial(MainMenuController menuController) {
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

        menuController.closeMenuAndShowStage(new TutorialGameStage(jumperData));
    }

    public HelpStage(final MainMenuController mainMenuController) {
        super(mainMenuController);

        TextureRegion soloTextureRegion = getMenuAssets().get(MenuAssets.Asset.TUTORIAL, TextureRegion.class);
        TextureRegion leaderboardsTextureRegion = getMenuAssets().get(MenuAssets.Asset.DEV_NOTE, TextureRegion.class);
        TextureRegion aboutTextureRegion = getMenuAssets().get(MenuAssets.Asset.ABOUT, TextureRegion.class);

        MenuButton tutorialButton = new MenuButton(soloTextureRegion);
        tutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                playTutorial(getMenuController());


            }


        });


        MenuButton developersNoteButton = new MenuButton(leaderboardsTextureRegion);
        developersNoteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new InfoDialog(HelpStage.this, "Developers' note", 0.04f,
                        "Hi! As you can see, this game feels incomplete - one hill, missing online options and bugs. \n" +
                                "I have written this game during sleepless nights for many weeks and I don't take any financial benefits from it. That's why support for this game is very limited, and I would be really pleased if you take this into account when writing review at Google Play. \n" +
                                "You liking this game? There is chance for you! - maybe you want to make new hills? Want better online modes? Want PC release? Or maybe you want me to release this game as open-source and contribute to game? I'm open for new ideas, you can always reach me at cyfrogen@gmail.com. " +
                                "\nI just want to get community voice :) \nBest regards, Jakub Dybczak")
                        .show();
            }
        });


        MenuButton aboutButton = new MenuButton(aboutTextureRegion);
        aboutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new InfoDialog(HelpStage.this, "About", 0.04f,
                        "Programming:\n Jakub Dybczak\n\n" +
                                "Graphics: \nRafal Michaluszek\nJakub Dybczak\nthefrob\n\n" +
                                //  "Music: Jani Golob\n" +
                                "Music:\n Tim Beek\nKamil Gluza\n\n" +
                                "Materials:\n" +
                                "Physics based on:\n \"Description of a ski jumper's flight\" by Renata Filipowska\n\n" +
                                "Contact us here: cyfrogen@gmail.com")
                        .show();
            }
        });

        addTiles(tutorialButton, developersNoteButton, aboutButton);
        addTitlebar("HELP");
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
                    signInListener.success();
                }

                @Override
                public void error() {
                    signInListener.error();
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
