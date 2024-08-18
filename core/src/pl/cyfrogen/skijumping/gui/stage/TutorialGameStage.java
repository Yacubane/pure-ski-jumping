package pl.cyfrogen.skijumping.gui.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.io.IOException;

import pl.cyfrogen.skijumping.data.HillSetup;
import pl.cyfrogen.skijumping.data.JumperData;
import pl.cyfrogen.skijumping.debug.Logger;
import pl.cyfrogen.skijumping.game.GameWorld;
import pl.cyfrogen.skijumping.game.interfaces.OnUpdateListener;
import pl.cyfrogen.skijumping.game.physics.Point;
import pl.cyfrogen.skijumping.game.physics.RayCastResult;
import pl.cyfrogen.skijumping.gui.ScreenHandler;
import pl.cyfrogen.skijumping.gui.actors.common.MenuButton;
import pl.cyfrogen.skijumping.gui.actors.game.GameSlideText;
import pl.cyfrogen.skijumping.gui.actors.game.JudgeScoresWidget;
import pl.cyfrogen.skijumping.gui.actors.game.PauseMenu;
import pl.cyfrogen.skijumping.gui.actors.game.ScoreWidget;
import pl.cyfrogen.skijumping.gui.actors.game.ScreenButton;
import pl.cyfrogen.skijumping.gui.actors.tutorial.OnTutorialAccept;
import pl.cyfrogen.skijumping.gui.actors.tutorial.TutorialFlyHelp;
import pl.cyfrogen.skijumping.gui.actors.tutorial.TutorialLandHelp;
import pl.cyfrogen.skijumping.gui.actors.tutorial.TutorialStartHelp;
import pl.cyfrogen.skijumping.gui.actors.tutorial.TutorialTakeOffHelp;
import pl.cyfrogen.skijumping.gui.actors.tutorial.util.TutorialAssets;
import pl.cyfrogen.skijumping.gui.actors.tutorial.widgets.TutorialAfterJumpMenu;
import pl.cyfrogen.skijumping.gui.actors.tutorial.widgets.TutorialFlyingStatus;
import pl.cyfrogen.skijumping.gui.interfaces.OnOkClick;
import pl.cyfrogen.skijumping.hill.HillFile;
import pl.cyfrogen.skijumping.hill.Hills;
import pl.cyfrogen.skijumping.jumper.JumperController;
import pl.cyfrogen.skijumping.jumper.JumperListener;
import pl.cyfrogen.skijumping.jumper.judge.JudgeScores;

public class TutorialGameStage extends Stage {
    private static final float WIDGET_SHOW_TIME = 0.5f;
    private final Group backgroundLayer;
    private final TextureAtlas commonAtlas;
    private final TutorialFlyingStatus tutorialFlyingStatus;
    private final Music tutorialMusic;
    private int gate;
    public GameWorld world;
    private boolean passInputToWorld = true;
    TutorialAssets tutorialAssets = new TutorialAssets(new TextureRegion(new Texture("textures/tutorial/phone.png")),
            new TextureRegion(new Texture("textures/tutorial/thumb.png")));
    private boolean shownTakeoffHelp;
    private boolean shownLandingHelp;

    Texture flyingStatusBackground = new Texture("textures/tutorial/flying_status.png");
    Texture flyingStatusThumb = new Texture("textures/tutorial/flying_status_thumb.png");

    public TutorialGameStage(final JumperData jumperData) {
        super();
        tutorialMusic = Gdx.audio.newMusic(Gdx.files.internal("music/tutorial_song.ogg"));
        tutorialMusic.setLooping(true);
        tutorialMusic.setVolume(0.6f);
        tutorialMusic.play();

        this.commonAtlas = new TextureAtlas(Gdx.files.internal("textures/common/common.atlas"));

        backgroundLayer = new Group();
        addActor(backgroundLayer);

        flyingStatusBackground.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        flyingStatusThumb.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        tutorialAssets.getPhoneTexture().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        tutorialAssets.getThumbTexture().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        tutorialFlyingStatus = new TutorialFlyingStatus(new TextureRegion(flyingStatusBackground),
                new TextureRegion(flyingStatusThumb));

        tutorialFlyingStatus.setPosition(Gdx.graphics.getWidth() * 0.25f,
                Gdx.graphics.getHeight() / 2f - tutorialFlyingStatus.getHeight() / 2f);


        final MenuButton pauseButton = new MenuButton(commonAtlas.findRegion("pause_button"));
        pauseButton.setWidthPreserveAspectRatio(Gdx.graphics.getWidth() * 0.05f);
        pauseButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                world.setPause(true);
                new PauseMenu(TutorialGameStage.this)
                        .withOnReturnToGameClickAction(new OnOkClick() {
                            @Override
                            public void okClick() {
                                world.setPause(false);

                            }
                        })
                        .withOnGoToMainMenuClickAction(new OnOkClick() {
                            @Override
                            public void okClick() {
                                showMenu();
                            }


                        })
                        .show();
            }
        });

        float pauseButtonPadding = Gdx.graphics.getWidth() * 0.02f;
        pauseButton.setPosition(pauseButtonPadding,
                Gdx.graphics.getHeight() - pauseButton.getHeight() - pauseButtonPadding);
        backgroundLayer.addActor(pauseButton);


        try {
            HillFile hillFile = new HillFile(Hills.ZAKOPANE_HS140v1);
            gate = hillFile.getMetaData().get("defaultCompetitiveStartGate").intValue();

            world = new GameWorld(hillFile, HillSetup.Snowing.NO, HillSetup.Mode.DAY, 0f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        world.setLandingEnabled(false);

        addActor(new GameSlideText("Tutorial", 0.1f, new Runnable() {
            @Override
            public void run() {
                releaseJumper(jumperData);

            }
        }));


    }

    private void pauseGame(boolean b) {
        if (b) {
            world.setPause(true);
        } else {
            world.setPause(false);
        }
    }


    public void releaseJumper(final JumperData jumperData) {
        passInputToWorld = false;
        shownLandingHelp = false;
        shownTakeoffHelp = false;

        world.setJumper(jumperData, gate);
        world.setOnUpdate(new OnUpdateListener() {

            private float timeFlying;

            @Override
            public void onUpdate(final JumperController jumperController) {
                tutorialFlyingStatus.setPercentage(jumperController.getPositionPercent(), (float) jumperController.getGoodFlyingPositionPercent());
                if (jumperController.isSliding()) {
                    Vector2 point =world.getHillModel().getInRunVertices().get(world.getHillModel().getInRunVertices().size()-1);
                    if (!shownTakeoffHelp && jumperController.getPhysicObject().getPosition().dst(point) < 2.5) {
                        shownTakeoffHelp = true;
                        pauseGame(true);
                        addActor(new TutorialTakeOffHelp(tutorialAssets, new OnTutorialAccept() {
                            @Override
                            public void tutorialAccepted() {
                                world.startJumper();
                                pauseGame(false);
                                world.getJumperController().setLaunch(1f);

                                addAction(Actions.sequence(Actions.delay(0.1f),
                                        Actions.run(new Runnable() {
                                            @Override
                                            public void run() {
                                                pauseGame(true);
                                                addActor(tutorialFlyingStatus);
                                                tutorialFlyingStatus.show();
                                                addActor(new TutorialFlyHelp(tutorialAssets, new OnTutorialAccept() {
                                                    @Override
                                                    public void tutorialAccepted() {
                                                        passInputToWorld = true;
                                                        world.startJumper();
                                                        pauseGame(false);
                                                        world.getJumperController().setLaunch(1f);
                                                    }
                                                }));
                                            }
                                        })));


                            }
                        }));
                        //jumper.getJumperController().setLaunch(1f);
                    }
                }
                if (jumperController.isFlying()) {
                    timeFlying += Gdx.graphics.getDeltaTime();
                    if (timeFlying > 1f) {
                        RayCastResult shadowPoint = world.getPhysics().rayCast(
                                new Point(jumperController.getPhysicObject().getPosition()),
                                new Point(0, -1));
                        if (shadowPoint.isFound() &&
                                shadowPoint.getResult().dst(Point.of(jumperController.getPhysicObject().getPosition())) < 2.5f &&
                                !shownLandingHelp) {
                            tutorialFlyingStatus.close();
                            shownLandingHelp = true;
                            pauseGame(true);
                            addActor(new TutorialLandHelp(tutorialAssets, new OnTutorialAccept() {
                                @Override
                                public void tutorialAccepted() {
                                    pauseGame(false);
                                    jumperController.setLanding();
                                }
                            }));
                        }
                    }

                }
            }
        });
        world.setJumperListener(new JumperListener() {

            @Override
            public void jumperStartedRide() {

            }

            @Override
            public void jumperJumped(Vector2 speedBeforeJump, Vector2 speedAfterJump) {

            }

            @Override
            public boolean shouldCrash(Vector2 speed, boolean telemark, float hillAngle, float timeFromPreparingToLand) {
                return false;
            }

            @Override
            public void jumperLanded(float distance, boolean crashed, Vector2 speed, boolean telemark, float timeFromPreparingToLand, JudgeScores judgeScores) {
                Logger.debug("RNJ: Jumper landed");

                final ScoreWidget scoreWidget = new ScoreWidget(
                        "1",
                        "Jakub DYBCZAK",
                        ((int) distance) + "." + ((int) distance % 100) + "m",
                        "",
                        "1",
                        "");
                scoreWidget.setPosition((Gdx.graphics.getWidth() - scoreWidget.getWidth()) / 2f,
                        Gdx.graphics.getHeight() * 0.05f);
                scoreWidget.setColor(1f, 1f, 1f, 0f);
                addActor(scoreWidget);
                scoreWidget.addAction(Actions.alpha(1, WIDGET_SHOW_TIME));

                final JudgeScoresWidget judgeScoresWidget = new JudgeScoresWidget(judgeScores);
                judgeScoresWidget.setPosition(scoreWidget.getX() + scoreWidget.getWidth() - judgeScoresWidget.getWidth(),
                        scoreWidget.getY() + scoreWidget.getHeight() * 1.5f);
                addActor(judgeScoresWidget);
                judgeScoresWidget.setColor(Color.CLEAR);

                judgeScoresWidget.addAction(Actions.sequence(
                        Actions.delay(0.5f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                judgeScoresWidget.setColor(Color.WHITE);
                                judgeScoresWidget.showWithAnimation();
                            }
                        })
                ));

                final ScreenButton screenButton = new ScreenButton(false);
                addTapListener(new Runnable() {
                    @Override
                    public void run() {
                        scoreWidget.addAction(Actions.sequence(
                                Actions.alpha(0, WIDGET_SHOW_TIME),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        world.setPause(true);
                                        new TutorialAfterJumpMenu(TutorialGameStage.this)
                                                .withAnotherTryClickAction(new OnOkClick() {
                                                    @Override
                                                    public void okClick() {
                                                        world.setPause(false);
                                                        releaseJumper(jumperData);
                                                    }
                                                })
                                                .withOnGoToMainMenuClickAction(new OnOkClick() {
                                                    @Override
                                                    public void okClick() {
                                                        showMenu();
                                                    }


                                                })
                                                .show();
                                    }
                                }),
                                Actions.removeActor()
                        ));
                        judgeScoresWidget.addAction(Actions.sequence(
                                Actions.alpha(0, WIDGET_SHOW_TIME),
                                Actions.removeActor()
                        ));
                        screenButton.remove();
                    }
                }, screenButton);

                backgroundLayer.addActor(screenButton);
            }
        });

        pauseGame(true);
        addActor(new TutorialStartHelp(tutorialAssets, new OnTutorialAccept() {
            @Override
            public void tutorialAccepted() {
                world.startJumper();
                pauseGame(false);
            }
        }));
    }


    private void showMenu() {
        dispose();
        ScreenHandler.get().showStageImmediately(new MainMenuController());
    }


    private void addTapListener(final Runnable runnable, Actor... actors) {
        for (Actor actor : actors) {
            actor.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    runnable.run();
                }

            });
        }
    }

    @Override
    public void draw() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        world.update(deltaTime);
        world.draw();
        super.draw();
    }

    @Override
    public void dispose() {
        tutorialMusic.stop();
        tutorialMusic.dispose();
        flyingStatusBackground.dispose();
        flyingStatusThumb.dispose();
        tutorialAssets.getPhoneTexture().getTexture().dispose();
        tutorialAssets.getThumbTexture().getTexture().dispose();
        commonAtlas.dispose();
        world.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean handled = super.keyDown(keycode);
        if (!handled && passInputToWorld) world.keyDown(keycode);
        return handled;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean handled = super.keyUp(keycode);
        if (!handled && passInputToWorld) world.keyUp(keycode);
        return handled;
    }

    @Override
    public boolean keyTyped(char character) {
        boolean handled = super.keyTyped(character);
        if (!handled && passInputToWorld) world.keyTyped(character);
        return handled;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean handled = super.touchDown(screenX, screenY, pointer, button);
        if (!handled && passInputToWorld) world.touchDown(screenX, screenY, pointer, button);
        else world.notifyTouchDown(screenX, screenY, pointer, button);
        return handled;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boolean handled = super.touchUp(screenX, screenY, pointer, button);
        if (!handled && passInputToWorld) world.touchUp(screenX, screenY, pointer, button);
        else world.notifyTouchUp(screenX, screenY, pointer, button);
        return handled;

    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        boolean handled = super.touchDragged(screenX, screenY, pointer);
        if (passInputToWorld) world.touchDragged(screenX, screenY, pointer);
        return handled;

    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        boolean handled = super.mouseMoved(screenX, screenY);
        if (!handled && passInputToWorld) world.mouseMoved(screenX, screenY);
        return handled;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        boolean handled = super.scrolled(amountX, amountY);
        if (!handled && passInputToWorld) world.scrolled((int) amountY);
        return handled;
    }

}
