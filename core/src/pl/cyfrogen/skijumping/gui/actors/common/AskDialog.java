package pl.cyfrogen.skijumping.gui.actors.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.gui.interfaces.OnOkClick;
import pl.cyfrogen.skijumping.gui.utils.Direction;
import pl.cyfrogen.skijumping.gui.utils.FontSizeUtils;
import pl.cyfrogen.skijumping.gui.utils.MenuAnimations;

import static pl.cyfrogen.skijumping.gui.utils.MenuAnimations.ShowingAnimationBuilder.CHANGE_ALPHA_DURATION;

public class AskDialog extends Group {
    private final Image background0;
    private final Group mainGroup;
    private final Stage parentStage;
    private OnOkClick onOkClick;
    private OnOkClick onRightClick;

    public AskDialog(Stage parentStage, String title, String description, String leftButton, String rightButton) {
        this.parentStage = parentStage;
        setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
                1f, 0.4f, title,
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.WHITE));
        titleLabel.setPosition(background.getX(),
                background.getY() + background.getHeight() - titleLabel.getHeight());
        mainGroup.addActor(titleLabel);

        Image button1Background = new Image(whiteDot);
        button1Background.setColor(Color.valueOf("FFFFFF"));
        button1Background.setSize(background.getWidth() / 2f, background.getHeight() * 0.2f);

        FilledLabel button1Label = new FilledLabel(
                background.getWidth() / 2f, background.getHeight() * 0.2f,
                1f, 0.4f, leftButton,
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.BLACK));

        Button button1 = new Button();
        button1.addActor(button1Background);
        button1.addActor(button1Label);
        button1.setSize(button1Background.getWidth(), button1Background.getHeight());
        button1.setPosition(background.getX(), background.getY());
        button1.addListener(new ClickListener() {
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
                                if (onOkClick != null)
                                    onOkClick.okClick();
                            }
                        }),
                        Actions.removeActor()));


            }
        });
        mainGroup.addActor(button1);

        Image button2Background = new Image(whiteDot);
        button2Background.setColor(Color.valueOf("FFFFFF"));
        button2Background.setSize(background.getWidth() / 2f, background.getHeight() * 0.2f);


        FilledLabel button2Label = new FilledLabel(
                background.getWidth() / 2f, background.getHeight() * 0.2f,
                1f, 0.4f, rightButton,
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.BLACK));

        Button button2 = new Button();
        button2.addActor(button2Background);
        button2.addActor(button2Label);
        button2.setSize(button2Background.getWidth(), button2Background.getHeight());
        button2.setPosition(background.getX() + button2.getWidth(), background.getY());
        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuAnimations.apply(MenuAnimations.hidingAnimation()
                                .withDirection(Direction.LEFT)
                                .withStartIdleTime(0),
                        mainGroup);

                background0.addAction(Actions.alpha(0f, CHANGE_ALPHA_DURATION * 0.7f));

                addAction(Actions.sequence(
                        Actions.delay(CHANGE_ALPHA_DURATION),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                if (onRightClick != null)
                                    onRightClick.okClick();
                            }
                        }),
                        Actions.removeActor()));


            }
        });
        mainGroup.addActor(button2);

        FontSizeUtils.calculateFontScaleByHeight(Main.getInstance().getAssets().getFont(), background.getHeight() * 0.05f);
        DistanceFieldLabel main = new DistanceFieldLabel(description, new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.WHITE));
        main.setWrap(true);
        main.setWidth(background.getWidth());
        main.setHeight(main.getPrefHeight());

        if (main.getPrefHeight() > background.getHeight() * 0.6f) {
            ScrollPane scrollPane = new ScrollPane(main);
            scrollPane.setSize(background.getWidth(), background.getHeight() * 0.6f);
            scrollPane.setPosition(background.getX(), button1.getY() + button1.getHeight());
            mainGroup.addActor(scrollPane);
        } else {
            main.setPosition(background.getX(), button1.getY() + button1.getHeight() + background.getHeight() * 0.6f * 0.5f - main.getHeight() * 0.5f);
            mainGroup.addActor(main);
        }


        main.setAlignment(Align.center);

        Divider divider = new Divider(background.getWidth(), Color.WHITE);
        divider.setPosition(background.getX(), background.getY() + background.getHeight() * 0.8f);
        mainGroup.addActor(divider);

        addActor(mainGroup);


    }

    public AskDialog withOnLeftClick(OnOkClick onOkClick) {
        this.onOkClick = onOkClick;
        return this;
    }

    public AskDialog withOnRightClick(OnOkClick onOkClick) {
        this.onRightClick = onOkClick;
        return this;
    }

    public void show() {
        mainGroup.setColor(new Color(1f, 1f, 1f, 0f));
        parentStage.addActor(this);

        MenuAnimations.apply(MenuAnimations.showingAnimation()
                        .withDirection(Direction.LEFT)
                        .withStartIdleTime(0),
                mainGroup);

        background0.addAction(Actions.alpha(0.5f, CHANGE_ALPHA_DURATION));
    }
}
