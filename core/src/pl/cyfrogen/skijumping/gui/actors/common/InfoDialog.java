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

public class InfoDialog extends Group {
    private final Image background0;
    private final Group mainGroup;
    private final Stage parentStage;
    private OnOkClick onOkClick;

    public InfoDialog(Stage parentStage, String title, float fontSizePercentage, String description) {
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

        Image buttonBackground = new Image(whiteDot);
        buttonBackground.setColor(Color.valueOf("FFFFFF"));
        buttonBackground.setSize(background.getWidth(), background.getHeight() * 0.2f);

        FilledLabel buttonLabel = new FilledLabel(
                background.getWidth(), background.getHeight() * 0.2f,
                1f, 0.4f, "OK",
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.BLACK));

        Button button = new Button();
        button.addActor(buttonBackground);
        button.addActor(buttonLabel);
        button.setSize(buttonBackground.getWidth(), buttonBackground.getHeight());
        button.setPosition(background.getX(), background.getY());
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MenuAnimations.apply(MenuAnimations.hidingAnimation()
                                .withDirection(Direction.LEFT)
                                .withStartIdleTime(0),
                        mainGroup);

                background0.addAction(Actions.alpha(0f, CHANGE_ALPHA_DURATION));

                addAction(Actions.sequence(
                        Actions.delay(CHANGE_ALPHA_DURATION * 0.7f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                if (onOkClick != null)
                                    onOkClick.okClick();
                            }
                        }),
                        Actions.delay(CHANGE_ALPHA_DURATION * 0.3f),
                        Actions.removeActor()));


            }
        });
        mainGroup.addActor(button);

        FontSizeUtils.calculateFontScaleByHeight(Main.getInstance().getAssets().getFont(), background.getHeight() * fontSizePercentage);
        DistanceFieldLabel main = new DistanceFieldLabel(description, new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.WHITE));
        main.setWrap(true);
        main.setWidth(background.getWidth()*0.9f);
        main.setHeight(main.getPrefHeight());

        if (main.getPrefHeight() > background.getHeight() * 0.6f) {
            ScrollPane scrollPane = new ScrollPane(main);
            scrollPane.setSize(background.getWidth()*0.9f, (background.getHeight() * 0.6f)*0.9f);
            scrollPane.setPosition(background.getX()+background.getWidth()*0.05f, button.getY() + button.getHeight() + (background.getHeight() * 0.6f)*0.05f);
            mainGroup.addActor(scrollPane);
        } else {
            main.setPosition(background.getX() + background.getWidth() * 0.05f, button.getY() + button.getHeight() + background.getHeight() * 0.6f * 0.5f - main.getHeight() * 0.5f);
            mainGroup.addActor(main);
        }


        main.setAlignment(Align.center);

        Divider divider = new Divider(background.getWidth(), Color.WHITE);
        divider.setPosition(background.getX(), background.getY() + background.getHeight() * 0.8f);
        mainGroup.addActor(divider);

        addActor(mainGroup);

    }


    public InfoDialog(Stage parentStage, String title, String description) {
      this(parentStage, title, 0.05f, description);

    }

    public InfoDialog withOnOkClickAction(OnOkClick onOkClick) {
        this.onOkClick = onOkClick;
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
