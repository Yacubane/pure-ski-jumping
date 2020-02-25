package pl.cyfrogen.skijumping;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;

import pl.cyfrogen.skijumping.asset.Assets;
import pl.cyfrogen.skijumping.common.timeout.TimeoutHandler;
import pl.cyfrogen.skijumping.data.SaveData;
import pl.cyfrogen.skijumping.editor.BlankEditor;
import pl.cyfrogen.skijumping.editor.Editor;
import pl.cyfrogen.skijumping.gui.ScreenHandler;
import pl.cyfrogen.skijumping.platform.PlatformAPI;

public class Main extends com.badlogic.gdx.Game {

    private final PlatformAPI platformApi;
    private Assets assets;
    private Preferences preferences;
    private SaveData saveData;
    private Editor editor = new BlankEditor();
    private TimeoutHandler tHandler;

    public Main(PlatformAPI platformAPI) {
        this.platformApi = platformAPI;
    }

    @Override
    public void create() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        assets = new Assets();
        tHandler = new TimeoutHandler();
        saveData = new SaveData();

        ScreenHandler.reset();
        setScreen(ScreenHandler.get());
    }

    public static Main getInstance() {
        ApplicationListener applicationListener = Gdx.app.getApplicationListener();
        return (Main) applicationListener;
    }

    public Assets getAssets() {
        return assets;
    }

    public Preferences getPreferences() {
        if (preferences == null) {
            preferences = Gdx.app.getPreferences("cyfrogen_psj");
        }
        return preferences;
    }

    public void attachEditor(Editor editor) {
        this.editor = editor;
    }

    public Editor getEditor() {
        return editor;
    }

    public TimeoutHandler getTimeoutHandler() {
        return tHandler;
    }

    public SaveData getSaveData() {
        return saveData;
    }

    public PlatformAPI getPlatformAPI() {
        return platformApi;
    }

}
