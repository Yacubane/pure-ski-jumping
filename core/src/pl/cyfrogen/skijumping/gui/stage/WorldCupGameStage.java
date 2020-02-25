package pl.cyfrogen.skijumping.gui.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

import pl.cyfrogen.skijumping.data.HillSetup;
import pl.cyfrogen.skijumping.data.WorldCupJumperData;
import pl.cyfrogen.skijumping.debug.Logger;
import pl.cyfrogen.skijumping.game.GameWorld;
import pl.cyfrogen.skijumping.game.data.GameData;
import pl.cyfrogen.skijumping.gui.actors.common.Divider;
import pl.cyfrogen.skijumping.gui.actors.common.MenuButton;
import pl.cyfrogen.skijumping.gui.actors.game.GameSlideText;
import pl.cyfrogen.skijumping.gui.actors.game.GateWidget;
import pl.cyfrogen.skijumping.gui.actors.game.JudgeScoresWidget;
import pl.cyfrogen.skijumping.gui.actors.game.PauseMenu;
import pl.cyfrogen.skijumping.gui.actors.game.ScoreWidget;
import pl.cyfrogen.skijumping.gui.actors.game.ScreenButton;
import pl.cyfrogen.skijumping.gui.actors.game.ToBeatWidget;
import pl.cyfrogen.skijumping.gui.actors.game.WindWidget;
import pl.cyfrogen.skijumping.gui.actors.game.scoreboard.RoundSummaryBoard;
import pl.cyfrogen.skijumping.gui.actors.game.scoreboard.StartingList;
import pl.cyfrogen.skijumping.gui.actors.game.scoreboard.WorldCupSummaryBoard;
import pl.cyfrogen.skijumping.gui.interfaces.OnOkClick;
import pl.cyfrogen.skijumping.gui.utils.MainMenuUtils;
import pl.cyfrogen.skijumping.hill.HillFile;
import pl.cyfrogen.skijumping.jumper.JumperController;
import pl.cyfrogen.skijumping.jumper.JumperListener;
import pl.cyfrogen.skijumping.jumper.OnJumperLanded;
import pl.cyfrogen.skijumping.jumper.judge.JudgeScores;

public class WorldCupGameStage extends Stage {
    private static final float WIDGET_SHOW_TIME = 0.5f;
    private final Group backgroundLayer;
    private final Music music;
    private int wind;
    private final TextureAtlas commonAtlas;
    public GameWorld world;
    private boolean passInputToWorld = true;
    private GameData gameData;
    private int round;
    private int hillNum = 0;
    private OnJumperLanded onJumperLanded;
    private int gate;

    public WorldCupGameStage(GameData gameData) {
        super();
        music = Gdx.audio.newMusic(Gdx.files.internal("music/wind_loop.ogg"));
        music.setLooping(true);
        music.setVolume(0.6f);
        music.play();

        this.commonAtlas = new TextureAtlas(Gdx.files.internal("textures/common/common.atlas"));
        this.gameData = gameData;

        backgroundLayer = new Group();
        addActor(backgroundLayer);

        MenuButton pauseButton = new MenuButton(commonAtlas.findRegion("pause_button"));
        pauseButton.setWidthPreserveAspectRatio(Gdx.graphics.getWidth() * 0.05f);
        pauseButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                world.setPause(true);
                new PauseMenu(WorldCupGameStage.this)
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

        createNewWorld();

        addActor(new GameSlideText("Round 1", 0.1f, new Runnable() {
            @Override
            public void run() {
                releaseNextJumper();
            }
        }));

    }

    private void createNewWorld() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            FileHandle root = gameData.getHills().get(hillNum).getHillLocation().getFileHandle();
            FileHandle manifest = root.child("manifest.json");
            JsonNode manifestNode = objectMapper.readTree
                    (manifest.readString());

            HillSetup hillSetup = gameData.getHills().get(hillNum);
            HillFile hillFile = new HillFile(hillSetup.getHillLocation());

            this.wind = (int) MathUtils.random(hillSetup.getMinWind() * 10, hillSetup.getMaxWind() * 10);
            gate = hillSetup.getStartGate();

            world = new GameWorld(hillFile, hillSetup.getSnowing(), hillSetup.getMode(), wind / 10f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void releaseNextJumper() {
        Logger.debug("RNJ: Releasing new jumper");

        if (gameData.getStartingJumpers().size() != 0) {
            ArrayList<WorldCupJumperData> startingList = gameData.getStartingJumpersList(hillNum);
            WorldCupJumperData worldCupJumperData = gameData.popStartingJumper();
            releaseJumper(worldCupJumperData, startingList, round,
                    new Runnable() {
                        @Override
                        public void run() {
                            releaseNextJumper();
                        }
                    });
            Logger.debug("RNJ: Starting new jumper. Size of staring jumpers is now: " + gameData.getStartingJumpers().size());
        } else {
            round++;
            Logger.debug("RNJ: New round: " + round);
            gameData.nextRound(hillNum);

            if (round >= gameData.getRounds()) {
                Logger.debug("RNJ: Rounds is higher than " + gameData.getRounds());
                showRoundSummary(new Runnable() {
                    @Override
                    public void run() {
                        Logger.debug("RNJ: Showing world cup summary");
                        gameData.updateJumperWorldCupPoints(hillNum);
                        showWorldCupSummary(new Runnable() {
                            @Override
                            public void run() {
                                world.dispose();
                                hillNum++;
                                if (hillNum >= gameData.getHills().size()) {
                                    showMenu();
                                } else {
                                    createNewWorld();
                                    round = 0;
                                    releaseNextJumper();
                                }
                            }
                        });
                    }
                });
            } else {
                Logger.debug("RNJ: Showing round summary");
                showRoundSummary(new Runnable() {
                    @Override
                    public void run() {
                        addActor(new GameSlideText("Round 2", 0.1f, new Runnable() {

                            @Override
                            public void run() {
                                releaseNextJumper();

                            }
                        }));
                    }
                });
            }
        }

    }

    private void showWorldCupSummary(final Runnable runnable) {
        final ScreenButton screenButton = new ScreenButton(true);
        backgroundLayer.addActor(screenButton);

        final WorldCupSummaryBoard startingList = new WorldCupSummaryBoard(hillNum, gameData.getSortedJumpers(hillNum));
        MainMenuUtils.positionActorOnScreen(startingList, 0.5f, 0.5f);
        startingList.setColor(Color.CLEAR);
        startingList.addAction(Actions.alpha(1, WIDGET_SHOW_TIME));
        addActor(startingList);

        addTapListener(new Runnable() {
            @Override
            public void run() {
                startingList.addAction(Actions.sequence(
                        Actions.alpha(0, WIDGET_SHOW_TIME),
                        Actions.run(runnable),
                        Actions.removeActor()
                ));
                screenButton.remove();
            }
        }, screenButton, startingList);
    }

    private void showRoundSummary(final Runnable runnable) {
        final ScreenButton screenButton = new ScreenButton(true);
        backgroundLayer.addActor(screenButton);

        final RoundSummaryBoard startingList = new RoundSummaryBoard(hillNum, gameData.getSortedJumpers(hillNum));
        MainMenuUtils.positionActorOnScreen(startingList, 0.5f, 0.5f);
        startingList.setColor(Color.CLEAR);
        startingList.addAction(Actions.alpha(1, WIDGET_SHOW_TIME));
        addActor(startingList);

        addTapListener(new Runnable() {
            @Override
            public void run() {
                startingList.addAction(Actions.sequence(
                        Actions.alpha(0, WIDGET_SHOW_TIME),
                        Actions.run(runnable),
                        Actions.removeActor()
                ));
                screenButton.remove();
            }
        }, screenButton, startingList);
    }

    public void releaseJumper(final WorldCupJumperData worldCupJumperData,
                              ArrayList<WorldCupJumperData> startingJumpersList,
                              final int round, final Runnable callback) {
        final StartingList startingList = new StartingList(hillNum, "STARTING ORDER", startingJumpersList);
        MainMenuUtils.positionActorOnScreen(startingList, 0.5f, 0.5f);
        startingList.setColor(Color.CLEAR);
        startingList.addAction(Actions.alpha(1, WIDGET_SHOW_TIME));
        addActor(startingList);

        final Group jumperStatusWidgets = new Group();
        jumperStatusWidgets.setColor(1f, 1f, 1f, 0f);

        final WindWidget windWidget = new WindWidget(
                commonAtlas.findRegion("jumperIcon"),
                commonAtlas.findRegion("wind"),
                commonAtlas.findRegion("windMask"),
                wind);
        windWidget.setPosition(Gdx.graphics.getWidth() * 0.015f, Gdx.graphics.getHeight() * 0.65f);
        jumperStatusWidgets.addActor(windWidget);

        Divider divider = new Divider(windWidget.getWidth(), Color.WHITE);
        divider.setPosition(Gdx.graphics.getWidth() * 0.015f, windWidget.getY() - divider.getHeight());
        jumperStatusWidgets.addActor(divider);

        WorldCupJumperData bestJumper = gameData.getActualBestJumper(hillNum);
        if (bestJumper == worldCupJumperData) {
            final ToBeatWidget toBeatWidget = new ToBeatWidget(0);
            toBeatWidget.setPosition(Gdx.graphics.getWidth() * 0.015f, divider.getY() - toBeatWidget.getHeight());
            jumperStatusWidgets.addActor(toBeatWidget);
        } else {
            double neededPoints = bestJumper.getPoints(hillNum).getIntegerPoints() / 10f - worldCupJumperData.getPoints(hillNum).getIntegerPoints() / 10f;

            //points = 60 + (19 * 3) + (distance - Calculating2.k) * 1.8;

            double distance = (neededPoints - 60 - (18 * 3)) / 1.8d + world.getHillModel().getConstructionPointPathLength();
            if (distance > 0) {
                final ToBeatWidget toBeatWidget = new ToBeatWidget((int) (distance * 100));
                toBeatWidget.setPosition(Gdx.graphics.getWidth() * 0.015f, divider.getY() - toBeatWidget.getHeight());
                jumperStatusWidgets.addActor(toBeatWidget);
            } else {
                final ToBeatWidget toBeatWidget = new ToBeatWidget(0);
                toBeatWidget.setPosition(Gdx.graphics.getWidth() * 0.015f, divider.getY() - toBeatWidget.getHeight());
                jumperStatusWidgets.addActor(toBeatWidget);
            }
        }

        final GateWidget gateWidget = new GateWidget(commonAtlas.findRegion("gate"), gate);
        gateWidget.setPosition(Gdx.graphics.getWidth() * 0.015f, Gdx.graphics.getHeight() * 0.2f);
        jumperStatusWidgets.addActor(gateWidget);


        final ScoreWidget scoreWidget = new ScoreWidget(
                String.valueOf(worldCupJumperData.getJumperNumber()),
                worldCupJumperData.getJumperData().getName(),
                worldCupJumperData.getDistanceString(hillNum, 0),
                worldCupJumperData.getDistanceString(hillNum, 1),
                String.valueOf(worldCupJumperData.getPlace(hillNum)),
                worldCupJumperData.getPointsString(hillNum));
        scoreWidget.setPosition((Gdx.graphics.getWidth() - scoreWidget.getWidth()) / 2f,
                Gdx.graphics.getHeight() * 0.05f);
        scoreWidget.setColor(1f, 1f, 1f, 0f);


        final ScreenButton screenButton = new ScreenButton(true);

        addTapListener(new Runnable() {
            @Override
            public void run() {
                startingList.addAction(Actions.sequence(
                        Actions.touchable(Touchable.disabled),
                        Actions.alpha(0f, WIDGET_SHOW_TIME),
                        Actions.removeActor()));
                screenButton.remove();


                addActor(scoreWidget);
                scoreWidget.addAction(Actions.alpha(1, WIDGET_SHOW_TIME));

                addActor(jumperStatusWidgets);
                jumperStatusWidgets.addAction(Actions.alpha(1, WIDGET_SHOW_TIME));

            }
        }, screenButton, startingList);

        backgroundLayer.addActor(screenButton);


        world.setJumper(worldCupJumperData.getJumperData(), gate);
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
//                if (telemark && hillAngle > -28f * MathUtils.degreesToRadians)
//                    return true;
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

                worldCupJumperData.addResult(hillNum, round, distance, points);
                gameData.updateJumperPositions(hillNum);


                final ScoreWidget scoreWidget = new ScoreWidget(
                        String.valueOf(worldCupJumperData.getJumperNumber()),
                        worldCupJumperData.getJumperData().getName(),
                        worldCupJumperData.getDistanceString(hillNum, 0),
                        worldCupJumperData.getDistanceString(hillNum, 1),
                        String.valueOf(worldCupJumperData.getPlace(hillNum)),
                        worldCupJumperData.getPoints(hillNum).getFormattedString());
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
                                Actions.run(callback),
                                Actions.removeActor()
                        ));
                        judgeScores.addAction(Actions.sequence(
                                Actions.alpha(0, WIDGET_SHOW_TIME),
                                Actions.removeActor()
                        ));
                        screenButton.remove();
                    }
                }, screenButton, startingList);

                backgroundLayer.addActor(screenButton);
            }
        });
    }


    private void showMenu() {
        dispose();
        gameData.getOnEnd().end();
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
    public boolean scrolled(int amount) {
        boolean handled = super.scrolled(amount);
        if (!handled && passInputToWorld) world.scrolled(amount);
        return handled;
    }


    public WorldCupGameStage withOnJumperLanded(OnJumperLanded onJumperLanded) {
        this.onJumperLanded = onJumperLanded;
        return this;
    }


    public WorldCupGameStage withWind(int i) {
        this.wind = i;
        return this;
    }
}
