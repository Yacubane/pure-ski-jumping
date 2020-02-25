package pl.cyfrogen.skijumping.gui.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.asset.MenuAssets;
import pl.cyfrogen.skijumping.common.interfaces.OnEnd;
import pl.cyfrogen.skijumping.gui.ScreenHandler;
import pl.cyfrogen.skijumping.gui.actors.common.CyfrogenLogo;

public class SplashScreenStage extends Stage {
    private final float CYFROGEN_LOGO_WIDTH = Gdx.graphics.getWidth() * 0.6f;
    private final CyfrogenLogo cyfrogenLogo;
    private final Image background;
    private MenuAssets menuAssets;
    private boolean end;

    public SplashScreenStage() {
        menuAssets = new MenuAssets();
        cyfrogenLogo = new CyfrogenLogo(CYFROGEN_LOGO_WIDTH, CYFROGEN_LOGO_WIDTH * 1 / CyfrogenLogo.CANVAS_WIDTH_HEIGHT_RATIO);
        cyfrogenLogo.setOnEnd(new OnEnd() {

            @Override
            public void end() {
                menuAssets.finishLoading();
                end = true;
            }
        });
        cyfrogenLogo.setPosition(Gdx.graphics.getWidth() / 2f - cyfrogenLogo.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f - cyfrogenLogo.getHeight() / 2f);

        Texture whiteDot = Main.getInstance().getAssets().getWhiteDot();
        background = new Image(whiteDot);
        background.setColor(Color.valueOf("#000000"));
        background.setSize(getWidth(), getHeight());
        addActor(background);
        addActor(cyfrogenLogo);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (menuAssets != null && menuAssets.update() && end) {
            cyfrogenLogo.dispose();
            MainMenuController mainMenuController = new MainMenuController(menuAssets);
            ScreenHandler.get().showStageImmediately(mainMenuController);
        }
    }
}
