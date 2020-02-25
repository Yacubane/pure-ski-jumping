package pl.cyfrogen.skijumping.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.gui.stage.SplashScreenStage;

public class ScreenHandler extends ScreenAdapter {

    private static ScreenHandler screenHandler;
    private Stage stage;

    public ScreenHandler() {
        stage = new SplashScreenStage();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Main.getInstance().getTimeoutHandler().update();

        if (stage != null) {
            stage.act(deltaTime);
            stage.draw();
        }
    }

    @Override
    public void dispose() {
    }


    @Override
    public void hide() {

    }


    public static ScreenHandler get() {
        if (screenHandler == null) {
            screenHandler = new ScreenHandler();
            return screenHandler;
        }
        return screenHandler;
    }

    public void showStageImmediately(Stage s) {
        stage = s;
        Gdx.input.setInputProcessor(s);
    }

    public static void reset() {
        screenHandler = null;
    }

    public enum Animation {
        BACK, NEXT
    }
}
