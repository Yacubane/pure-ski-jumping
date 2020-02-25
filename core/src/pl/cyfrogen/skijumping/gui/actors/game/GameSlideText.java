package pl.cyfrogen.skijumping.gui.actors.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;

public class GameSlideText extends Group {
    private static final float BACKGROUND_ANIMATION_TIME = 0.5f;
    private static final float TEXT_VISIBLE_ANIMATION_TIME = 1f;

    private boolean canSkip = true;
    private float moveDistance = Gdx.graphics.getWidth() * 0.25f;

    public GameSlideText(String text, float height, final Runnable runnable) {
        setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        final Image background = new Image(whiteDot);
        background.setColor(Color.valueOf("000000"));
        background.setSize(getWidth(), getHeight());
        addActor(background);

        final Image background2 = new Image(whiteDot);
        background2.setColor(Color.valueOf("000000"));
        background2.setSize(getWidth(), getHeight() * height * 1.6f);
        background2.setPosition(0, (Gdx.graphics.getHeight() - background2.getHeight()) / 2f);
        addActor(background2);

        final FilledLabel numberLabel = new FilledLabel(getWidth(), getHeight(),
                0.9f, height, text,
                new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.WHITE));
        addActor(numberLabel);

        background.setColor(0f, 0f, 0f, 0f);
        background2.setColor(0f, 0f, 0f, 0f);
        numberLabel.setColor(1f, 1f, 1f, 0f);

        background.addAction(Actions.sequence(
                Actions.alpha(0.7f, BACKGROUND_ANIMATION_TIME,
                        Interpolation.pow2Out),
                Actions.delay(TEXT_VISIBLE_ANIMATION_TIME),
                Actions.alpha(0f, BACKGROUND_ANIMATION_TIME,
                        Interpolation.pow2In)
        ));

        background2.addAction(getTextMoveActions());

        numberLabel.addAction(Actions.sequence(
                getTextMoveActions(),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        runnable.run();
                        GameSlideText.this.remove();
                    }
                })
        ));


        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (canSkip) {
                    canSkip = false;
                    background.getActions().clear();
                    background2.getActions().clear();
                    numberLabel.getActions().clear();

                    background.addAction(Actions.sequence(
                            Actions.alpha(0f, BACKGROUND_ANIMATION_TIME,
                                    Interpolation.pow2In)
                    ));

                    background2.addAction(Actions.parallel(
                            Actions.alpha(0, BACKGROUND_ANIMATION_TIME,
                                    Interpolation.pow2In),
                            Actions.moveBy(-moveDistance, 0,
                                    BACKGROUND_ANIMATION_TIME,
                                    Interpolation.pow2In)));

                    numberLabel.addAction(Actions.sequence(
                            Actions.parallel(
                                    Actions.alpha(0, BACKGROUND_ANIMATION_TIME,
                                            Interpolation.pow2In),
                                    Actions.moveBy(-moveDistance, 0,
                                            BACKGROUND_ANIMATION_TIME,
                                            Interpolation.pow2In)),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    runnable.run();
                                    GameSlideText.this.remove();
                                }
                            })
                    ));

                }
            }
        });


    }

    private Action getTextMoveActions() {

        return Actions.sequence(Actions.alpha(0),
                Actions.moveBy(moveDistance, 0),
                Actions.parallel(
                        Actions.alpha(1, BACKGROUND_ANIMATION_TIME,
                                Interpolation.pow2Out),
                        Actions.moveBy(-moveDistance, 0,
                                BACKGROUND_ANIMATION_TIME,
                                Interpolation.pow2Out)),
                Actions.delay(TEXT_VISIBLE_ANIMATION_TIME),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        canSkip = false;
                    }
                }),
                Actions.parallel(
                        Actions.alpha(0, BACKGROUND_ANIMATION_TIME,
                                Interpolation.pow2In),
                        Actions.moveBy(-moveDistance, 0,
                                BACKGROUND_ANIMATION_TIME,
                                Interpolation.pow2In)));
    }


}
