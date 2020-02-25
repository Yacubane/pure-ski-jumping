package pl.cyfrogen.skijumping.gui.actors.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.gui.actors.common.FilledLabel;

public class ScreenButton extends Group {
    public ScreenButton(boolean text) {
        setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (text) {
            FilledLabel numberLabel = new FilledLabel(getWidth(), getHeight() * 0.15f,
                    0.9f, 0.3f, "Tap to continue...",
                    new Label.LabelStyle(Main.getInstance().getAssets().getFont(), Color.WHITE));
            addActor(numberLabel);

            setColor(new Color(1f, 1f, 1f, 0f));

            addAction(Actions.sequence(
                    Actions.delay(2f),
                    Actions.alpha(0.8f, 1f)
            ));
        }
    }

    @Override
    public boolean remove() {
        addAction(Actions.sequence(
                Actions.touchable(Touchable.disabled),
                Actions.alpha(0f, 1f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        ScreenButton.super.remove();
                    }
                })
        ));
        return true;
    }
}
