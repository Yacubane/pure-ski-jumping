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
import pl.cyfrogen.skijumping.gui.actors.tutorial.images.TutorialImageTapTwoThumbs;
import pl.cyfrogen.skijumping.gui.actors.tutorial.util.TutorialAssets;
import pl.cyfrogen.skijumping.gui.actors.tutorial.widgets.TutorialLabel;

public class TutorialTakeOffHelp extends FBOWidget {
    private final Group group;

    public TutorialTakeOffHelp(TutorialAssets assets, final OnTutorialAccept onTutorialAccept) {
        super();
        group = new Group();
        group.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        addActor(group);

        setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setColor(new Color(1f, 1f, 1f, 0.75f));

        TutorialLabel label = new TutorialLabel("TAP WITH TWO FINGERS TO TAKE OFF");
        label.setPosition(Gdx.graphics.getWidth() / 2f - label.getWidth() / 2f, Gdx.graphics.getHeight() - label.getHeight() * 1.5f);
        group.addActor(label);

        Actor tutorialImage = new TutorialImageTapTwoThumbs(
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
            int touches = 0;

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touches++;
                if (touches == 2) {
                    if(!tutorialAccepted) {
                        tutorialAccepted = true;
                        onTutorialAccept.tutorialAccepted();
                    }
                    close();
                }
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touches--;
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
