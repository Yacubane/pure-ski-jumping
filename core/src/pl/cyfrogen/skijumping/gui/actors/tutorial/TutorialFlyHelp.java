package pl.cyfrogen.skijumping.gui.actors.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import pl.cyfrogen.skijumping.gui.actors.common.FBOWidget;
import pl.cyfrogen.skijumping.gui.actors.tutorial.images.TutorialImageSwipe;
import pl.cyfrogen.skijumping.gui.actors.tutorial.util.TutorialAssets;
import pl.cyfrogen.skijumping.gui.actors.tutorial.widgets.TutorialLabel;

public class TutorialFlyHelp extends FBOWidget {
    private final Group group;

    public TutorialFlyHelp(TutorialAssets assets, final OnTutorialAccept onTutorialAccept) {
        super();
        group = new Group();
        group.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        addActor(group);

        setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setColor(new Color(1f, 1f, 1f, 0.75f));

        Actor label = new TutorialLabel("SWIPE YOUR FINGER TO STEER");
        label.setPosition(Gdx.graphics.getWidth() / 2f - label.getWidth() / 2f, Gdx.graphics.getHeight() * 0.95f - label.getHeight());
        group.addActor(label);

        Actor tutorialImage = new TutorialImageSwipe(
                assets.getPhoneTexture(),
                assets.getThumbTexture());

        tutorialImage.setPosition(Gdx.graphics.getWidth() / 2f - tutorialImage.getWidth() / 2f,
                tutorialImage.getHeight() * 0.1f);
        group.addActor(tutorialImage);

        group.setColor(Color.CLEAR);
        group.addAction(Actions.sequence(
                Actions.alpha(1f, 0.5f, Interpolation.smooth)
        ));

        addListener(new InputListener() {
            private boolean tutorialAccepted;
            private float lastY;
            private int pointer = -1;
            float touchDraggedSum = 0;

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                this.pointer = pointer;
                lastY = y;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == this.pointer) {
                    this.pointer = -1;
                    touchDraggedSum = 0;
                }
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (pointer == this.pointer) {
                    touchDraggedSum += Math.abs(lastY-y);
                    lastY = y;
                    if (touchDraggedSum > Gdx.graphics.getHeight() * 0.02f) {
                        if(!tutorialAccepted) {
                            tutorialAccepted = true;
                            onTutorialAccept.tutorialAccepted();
                        }
                        close();
                    }

                }
            }

        });
    }

    private void close() {
        setTouchable(Touchable.disabled);
        group.addAction(Actions.sequence(
                Actions.alpha(0, 0.5f, Interpolation.smooth),
                Actions.removeActor()
        ));
    }
}
