package pl.cyfrogen.skijumping.gui.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
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
import pl.cyfrogen.skijumping.gui.ScreenHandler;
import pl.cyfrogen.skijumping.gui.actors.common.MenuButton;
import pl.cyfrogen.skijumping.gui.actors.game.GameSlideText;
import pl.cyfrogen.skijumping.gui.actors.game.GateWidget;
import pl.cyfrogen.skijumping.gui.actors.game.JudgeScoresWidget;
import pl.cyfrogen.skijumping.gui.actors.game.PauseMenu;
import pl.cyfrogen.skijumping.gui.actors.game.ScoreWidget;
import pl.cyfrogen.skijumping.gui.actors.game.ScreenButton;
import pl.cyfrogen.skijumping.gui.actors.game.WindWidget;
import pl.cyfrogen.skijumping.gui.actors.online.OnlineAfterJumpMenu;
import pl.cyfrogen.skijumping.gui.interfaces.OnOkClick;
import pl.cyfrogen.skijumping.hill.HillFile;
import pl.cyfrogen.skijumping.hill.Hills;
import pl.cyfrogen.skijumping.jumper.JumperController;
import pl.cyfrogen.skijumping.jumper.JumperListener;
import pl.cyfrogen.skijumping.jumper.OnJumperLanded;
import pl.cyfrogen.skijumping.jumper.judge.JudgeScores;

public class OnlineGameStage extends Stage {
    private static final float WIDGET_SHOW_TIME = 0.5f;
    private final Group backgroundLayer;
    private final Music music;
    private int gate;
    private int wind;
    private final TextureAtlas commonAtlas;
    public GameWorld world;
    private boolean passInputToWorld = true;
    private JumperData jumperData;
    private int round;
    private int hillNum = 0;
    private OnJumperLanded onJumperLanded;

    public OnlineGameStage(final JumperData jumperData) {
        super();
        music = Gdx.audio.newMusic(Gdx.files.internal("music/wind_loop.ogg"));
        music.setLooping(true);
        music.setVolume(0.6f);
        music.play();

        this.commonAtlas = new TextureAtlas(Gdx.files.internal("textures/common/common.atlas"));
        this.jumperData = jumperData;

        backgroundLayer = new Group();
        addActor(backgroundLayer);

        MenuButton pauseButton = new MenuButton(commonAtlas.findRegion("pause_button"));
        pauseButton.setWidthPreserveAspectRatio(Gdx.graphics.getWidth() * 0.05f);
        pauseButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                world.setPause(true);
                new PauseMenu(OnlineGameStage.this)
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
            HillFile hillFile = new HillFile(Hills.PLANICA_HS240v1);
            HillSetup.Mode mode = HillSetup.createWeather(
                    hillFile.getMetaData().get("defaultCompetitiveMode").textValue());
            HillSetup.Snowing snowing = HillSetup.createSnowing(
                    hillFile.getMetaData().get("defaultCompetitiveSnowing").textValue());
            wind = (int) (hillFile.getMetaData().get("physics").get("defaultCompetitiveWind").floatValue() * 10);
            gate = hillFile.getMetaData().get("defaultCompetitiveStartGate").intValue();
            world = new GameWorld(hillFile, snowing, mode, wind / 10f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        addActor(new GameSlideText("Go for record!", 0.1f, new Runnable() {
            @Override
            public void run() {
                releaseJumper(jumperData);
            }
        }));

    }


    public void releaseJumper(final JumperData jumperData) {
        world.setJumper(jumperData, gate);


        final Group jumperStatusWidgets = new Group();
        jumperStatusWidgets.setColor(1f, 1f, 1f, 0f);

        final WindWidget windWidget = new WindWidget(
                commonAtlas.findRegion("jumperIcon"),
                commonAtlas.findRegion("wind"),
                commonAtlas.findRegion("windMask"),
                wind);
        windWidget.setPosition(Gdx.graphics.getWidth() * 0.015f, Gdx.graphics.getHeight() * 0.65f);
        jumperStatusWidgets.addActor(windWidget);

        final GateWidget gateWidget = new GateWidget(commonAtlas.findRegion("gate"), gate);
        gateWidget.setPosition(Gdx.graphics.getWidth() * 0.015f, Gdx.graphics.getHeight() * 0.2f);
        jumperStatusWidgets.addActor(gateWidget);


        final ScoreWidget scoreWidget = new ScoreWidget(
                "0",
                "Player",
                "",
                "",
                "",
                "");
        scoreWidget.setPosition((Gdx.graphics.getWidth() - scoreWidget.getWidth()) / 2f,
                Gdx.graphics.getHeight() * 0.05f);
        scoreWidget.setColor(1f, 1f, 1f, 0f);


        addActor(scoreWidget);
        scoreWidget.addAction(Actions.alpha(1, WIDGET_SHOW_TIME));

        addActor(jumperStatusWidgets);
        jumperStatusWidgets.addAction(Actions.alpha(1, WIDGET_SHOW_TIME));


        world.setJumper(jumperData, gate);
        world.setJumperListener(new JumperListener() {

            @Override
            public void jumperStartedRide() {
                scoreWidget.addAction(Actions.sequence(
                        Actions.alpha(0, WIDGET_SHOW_TIME),
                        Actions.removeActor()));

                jumperStatusWidgets.addAction(Actions.sequence(
                        Actions.delay(3f),
                        Actions.alpha(0, WIDGET_SHOW_TIME),
                        Actions.removeActor()));
            }

            @Override
            public void jumperJumped(Vector2 speedBeforeJump, Vector2 speedAfterJump) {

            }

            @Override
            public boolean shouldCrash(Vector2 speed, boolean telemark, float hillAngle, float timeFromPreparingToLand) {
                if (telemark && hillAngle > -28f * MathUtils.degreesToRadians)
                    return true;
                if (telemark && timeFromPreparingToLand < JumperController.PREPARE_TO_LAND_TIME * 0.9f)
                    return true;
                if (!telemark && timeFromPreparingToLand < JumperController.PREPARE_TO_LAND_TIME * 0.6f)
                    return true;

                return false;
            }

            @Override
            public void jumperLanded(float distance, boolean crashed, Vector2 speed, boolean telemark, float timeFromPreparingToLand, JudgeScores judgeScore) {
                Logger.debug("RNJ: Jumper landed");

                double points = 60;

                double multiplier = 1.8;
                if (world.getHillModel().getConstructionPointPathLength() > 170) {
                    //big hill
                    points = 120;
                    multiplier = 1.2;
                }

                points += (distance - world.getHillModel().getConstructionPointPathLength()) * multiplier;
                points += judgeScore.getFinalPoints() / (double) 10;

                if (points < 0) points = 0;

                if (onJumperLanded != null)
                    onJumperLanded.jumperLanded(crashed, distance, points);


                int distanceInteger = (int) (distance * 100);
                String formattedDistance = distanceInteger / 100 + "." + distanceInteger % 100 + " m";

                final ScoreWidget scoreWidget = new ScoreWidget(
                        "0",
                        "Player",
                        formattedDistance,
                        "",
                        "1",
                        "");
                scoreWidget.setPosition((Gdx.graphics.getWidth() - scoreWidget.getWidth()) / 2f,
                        Gdx.graphics.getHeight() * 0.05f);
                scoreWidget.setColor(1f, 1f, 1f, 0f);
                addActor(scoreWidget);
                scoreWidget.addAction(Actions.alpha(1, WIDGET_SHOW_TIME));

                final JudgeScoresWidget judgeScores = new JudgeScoresWidget(judgeScore);
                judgeScores.setPosition(scoreWidget.getX() + scoreWidget.getWidth() - judgeScores.getWidth(),
                        scoreWidget.getY() + scoreWidget.getHeight() * 1.5f);
                addActor(judgeScores);
                judgeScores.setColor(Color.CLEAR);

                judgeScores.addAction(Actions.sequence(
                        Actions.delay(0.5f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                judgeScores.setColor(Color.WHITE);
                                judgeScores.showWithAnimation();
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
                                        new OnlineAfterJumpMenu(OnlineGameStage.this)
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
                        judgeScores.addAction(Actions.sequence(
                                Actions.alpha(0, WIDGET_SHOW_TIME),
                                Actions.removeActor()
                        ));
                        screenButton.remove();
                    }
                }, screenButton, scoreWidget);

                backgroundLayer.addActor(screenButton);
            }
        });
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
        music.stop();
        music.dispose();
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
        return handled;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boolean handled = super.touchUp(screenX, screenY, pointer, button);
        if (!handled && passInputToWorld) world.touchUp(screenX, screenY, pointer, button);
        return handled;

    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        boolean handled = super.touchDragged(screenX, screenY, pointer);
        if (!handled && passInputToWorld) world.touchDragged(screenX, screenY, pointer);
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


    public OnlineGameStage withOnJumperLanded(OnJumperLanded onJumperLanded) {
        this.onJumperLanded = onJumperLanded;
        return this;
    }


    public OnlineGameStage withWind(int i) {
        this.wind = i;
        return this;
    }

}
