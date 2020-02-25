package pl.cyfrogen.skijumping.gui.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;

import java.util.List;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.common.timeout.Timeout;
import pl.cyfrogen.skijumping.common.timeout.TimeoutEvent;
import pl.cyfrogen.skijumping.gui.stage.MainMenuController;

public class MainMenuUtils {
    public static void showStage(final MainMenuController mainMenuController, final Stage stage) {
        showStage(mainMenuController, stage, null);
    }

    public static void showStage(final MainMenuController mainMenuController, final Stage stage, final OnStageShown onStageShown) {
        Main.getInstance().getTimeoutHandler().add(
                new Timeout(0.15f, new TimeoutEvent() {
                    @Override
                    public void fire() {
                        mainMenuController.showNextMenuStage(stage);
                        if (onStageShown != null) onStageShown.stageShown();

                    }
                }));
    }

    public static Container<Actor> createScrollPanelContainer(Actor actor, float scrollPaneHeight) {
        Container<Actor> container = new Container<Actor>();
        container.setSize(actor.getWidth(),
                Math.max(scrollPaneHeight, actor.getHeight()));
        container.setActor(actor);
        return container;
    }

    public static void positionActorOnScreen(Actor actor, float x, float y) {
        actor.setPosition(
                (Gdx.graphics.getWidth() - actor.getWidth()) * x,
                (Gdx.graphics.getHeight() - actor.getHeight()) * y);
    }

    public static Group verticalGroupOf(List<Actor> actorList) {
        float width = 0;
        float height = 0;
        for (Actor actor : actorList) {
            width = Math.max(actor.getWidth(), width);
            height += actor.getHeight();
        }
        Group group = new Group();
        group.setSize(width, height);

        float y = height;
        for (Actor actor : actorList) {
            y-=actor.getHeight();
            actor.setPosition(0, y);
            group.addActor(actor);
        }

        return group;
    }
}
