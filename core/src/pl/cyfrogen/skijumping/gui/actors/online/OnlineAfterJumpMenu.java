package pl.cyfrogen.skijumping.gui.actors.online;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.gui.actors.common.Button;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;
import pl.cyfrogen.skijumping.gui.interfaces.OnOkClick;
import pl.cyfrogen.skijumping.gui.utils.Direction;
import pl.cyfrogen.skijumping.gui.utils.MenuAnimations;

import static pl.cyfrogen.skijumping.gui.utils.MenuAnimations.ShowingAnimationBuilder.CHANGE_ALPHA_DURATION;

public class OnlineAfterJumpMenu extends Group {
    private final Image background0;
    private final Group mainGroup;
    private final Stage parentStage;
    private final float restBackgroundSpace;
    private OnOkClick onReturnToGame;
    private OnOkClick onGoToMainMenu;

    public OnlineAfterJumpMenu(Stage parentStage) {
        this.parentStage = parentStage;
        setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setTouchable(Touchable.enabled);
        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        background0 = new Image(whiteDot);
        background0.setColor(new Color(0f, 0f, 0f, 0f));
        background0.setSize(getWidth(), getHeight());
        addActor(background0);


        mainGroup = new Group();
        mainGroup.setSize(getWidth(), getHeight());

        Image background = new Image(whiteDot);
        background.setColor(Color.valueOf("161616"));
        background.setSize(getWidth() * 0.8f, getHeight() * 0.8f);
        background.setPosition((Gdx.graphics.getWidth() - background.getWidth()) / 2f,
                (Gdx.graphics.getHeight() - background.getHeight()) / 2f);
        mainGroup.addActor(background);

        FilledLabel titleLabel = new FilledLabel(
                background.getWidth(), background.getHeight() * 0.2f,
                1f, 0.4f, "Go for record",
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.WHITE));
        titleLabel.setPosition(background.getX(),
                background.getY() + background.getHeight() - titleLabel.getHeight());
        restBackgroundSpace = background.getHeight() - titleLabel.getHeight();
        mainGroup.addActor(titleLabel);

        createReturnToGameButton(whiteDot, background);
        createGoToMainMenuButton(whiteDot, background);

        addActor(mainGroup);


    }

    private void createReturnToGameButton(Texture whiteDot, Image background) {
        Image returnToGameBackground = new Image(whiteDot);
        returnToGameBackground.setColor(Color.valueOf("FFFFFF"));
        returnToGameBackground.setSize(background.getWidth(), background.getHeight() * 0.2f);

        FilledLabel returnToGameText = new FilledLabel(
                background.getWidth(), background.getHeight() * 0.2f,
                1f, 0.4f, "Another try!",
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.BLACK));
        mainGroup.addActor(returnToGameText);

        Button button = new Button();
        button.addActor(returnToGameBackground);
        button.addActor(returnToGameText);
        button.setSize(returnToGameBackground.getWidth(), returnToGameBackground.getHeight());
        button.setPosition(background.getX(), background.getY() + (restBackgroundSpace * 0.66f) - button.getHeight() / 2f);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuAnimations.apply(MenuAnimations.hidingAnimation()
                                .withDirection(Direction.LEFT)
                                .withStartIdleTime(0),
                        mainGroup);

                background0.addAction(Actions.alpha(0f, CHANGE_ALPHA_DURATION));

                addAction(Actions.sequence(
                        Actions.delay(CHANGE_ALPHA_DURATION),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                if (onReturnToGame != null)
                                    onReturnToGame.okClick();
                            }
                        }),
                        Actions.removeActor()));


            }
        });
        mainGroup.addActor(button);
    }

    private void createGoToMainMenuButton(Texture whiteDot, Image background) {
        Image returnToGameBackground = new Image(whiteDot);
        returnToGameBackground.setColor(Color.valueOf("FFFFFF"));
        returnToGameBackground.setSize(background.getWidth(), background.getHeight() * 0.2f);

        FilledLabel returnToGameText = new FilledLabel(
                background.getWidth(), background.getHeight() * 0.2f,
                1f, 0.4f, "Main menu",
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.BLACK));
        mainGroup.addActor(returnToGameText);

        Button button = new Button();
        button.addActor(returnToGameBackground);
        button.addActor(returnToGameText);
        button.setSize(returnToGameBackground.getWidth(), returnToGameBackground.getHeight());
        button.setPosition(background.getX(), background.getY() + (restBackgroundSpace * 0.33f) - button.getHeight() / 2f);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuAnimations.apply(MenuAnimations.hidingAnimation()
                                .withDirection(Direction.LEFT)
                                .withStartIdleTime(0),
                        mainGroup);

                background0.addAction(Actions.alpha(0f, CHANGE_ALPHA_DURATION));

                addAction(Actions.sequence(
                        Actions.delay(CHANGE_ALPHA_DURATION),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                if (onGoToMainMenu != null)
                                    onGoToMainMenu.okClick();
                            }
                        }),
                        Actions.removeActor()));


            }
        });
        mainGroup.addActor(button);
    }

    public OnlineAfterJumpMenu withAnotherTryClickAction(OnOkClick onOkClick) {
        this.onReturnToGame = onOkClick;
        return this;
    }

    public OnlineAfterJumpMenu withOnGoToMainMenuClickAction(OnOkClick onOkClick) {
        this.onGoToMainMenu = onOkClick;
        return this;
    }

    public void show() {
        parentStage.addActor(this);

        mainGroup.setColor(Color.CLEAR);
        MenuAnimations.apply(MenuAnimations.showingAnimation()
                        .withDirection(Direction.LEFT)
                        .withStartIdleTime(0),
                mainGroup);

        background0.addAction(Actions.alpha(0.5f, CHANGE_ALPHA_DURATION));

    }
}
