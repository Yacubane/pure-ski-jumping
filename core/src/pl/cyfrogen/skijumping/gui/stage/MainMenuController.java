package pl.cyfrogen.skijumping.gui.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;

import pl.cyfrogen.skijumping.asset.MenuAssets;
import pl.cyfrogen.skijumping.gui.ScreenHandler;

public class MainMenuController extends Stage {
    private Stage backgroundImageStage;
    private MenuAssets menuAssets;
    private Music music;

    private Stage backgroundStage;
    private Stage foregroundStage;

    public MainMenuController(MenuAssets menuAssets) {
        super();
        this.menuAssets = menuAssets;
        init(false);
    }

    public MainMenuController() {
        super();
        init(true);
    }

    public void init(boolean load) {
        if (load) {
            menuAssets = new MenuAssets();
            menuAssets.finishLoading();
        }

        backgroundImageStage = new Stage();
        Image img = new Image(menuAssets.get(MenuAssets.Asset.BACKGROUND, TextureRegion.class));
        img.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        img.setScaling(Scaling.fill);
        backgroundImageStage.addActor(img);

        music = menuAssets.get(MenuAssets.Asset.MENU_THEME, Music.class);
        music.setLooping(true);
        music.play();

        foregroundStage = new MainMenuStage(this);

    }


    @Override
    public void draw() {

        backgroundImageStage.draw();
        if (backgroundStage != null) {
            backgroundStage.act();
            backgroundStage.draw();
        }
        if (foregroundStage != null) {
            foregroundStage.act();
            foregroundStage.draw();
        }

    }

    public MenuAssets getMenuAssets() {
        return menuAssets;
    }

    public void closeMenuAndShowStage(Stage stage) {
        music.stop();
        menuAssets.dispose();
        ScreenHandler.get().showStageImmediately(stage);
    }

    public void showNextMenuStage(Stage stage) {
        if(backgroundStage != null) {
            backgroundStage.dispose();
        }
        backgroundStage = foregroundStage;
        foregroundStage = stage;
    }

    public boolean keyDown(int keycode) {
        return foregroundStage.keyDown(keycode);
    }

    public boolean keyUp(int keycode) {
        return foregroundStage.keyUp(keycode);
    }

    public boolean keyTyped(char character) {
        return foregroundStage.keyTyped(character);
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return foregroundStage.touchDown(screenX, screenY, pointer, button);
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return foregroundStage.touchUp(screenX, screenY, pointer, button);
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return foregroundStage.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return foregroundStage.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return foregroundStage.scrolled(amount);
    }

    public void reshow() {
        init(true);
        ScreenHandler.get().showStageImmediately(this);
    }
}
