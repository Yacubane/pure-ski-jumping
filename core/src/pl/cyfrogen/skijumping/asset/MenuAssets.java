package pl.cyfrogen.skijumping.asset;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.HashMap;
import java.util.Map;

public class MenuAssets {
    private TextureAtlas mainAtlas;

    public void finishLoading() {
        assetManager.finishLoading();
        onLoaded();
    }

    public enum Asset {
        WORLD_CUP_TEXTURE,
        ONLINE_TEXTURE,
        HELP_TEXTURE,
        EXIT_TEXTURE,
        START_TEXTURE,
        COMPETITORS_TEXTURE,
        HILLS_TEXTURE,
        LEADERBOARDS_TEXTURE,

        CHECKBOX_CHECKED_TEXTURE,
        CHECKBOX_UNCHECKED_TEXTURE,

        CPU_PLAYER_ENABLED_TEXTURE,
        CPU_PLAYER_DISABLED_TEXTURE,
        NORMAL_PLAYER_ENABLED_TEXTURE,
        NORMAL_PLAYER_DISABLED_TEXTURE,

        ADD_BUTTON_TEXTURE,
        BACK_BUTTON_TEXTURE,
        EDIT_ICON_TEXTURE,
        DELETE_ICON_TEXTURE,
        ACCEPT_ICON_TEXTURE,

        WEATHER_DAY_ICON,
        WEATHER_MORNING_ICON,
        WEATHER_NIGHT_ICON,

        SNOWING_NO_ICON,
        SNOWING_SOFT_ICON,
        SNOWING_HARD_ICON,

        ARROW_UP_TEXTURE,
        ARROW_DOWN_TEXTURE,
        ZAKOPANE_HILL_TEXTURE,
        TUTORIAL,
        GO_FOR_RECORD,
        DEV_NOTE,
        PRIVACY_POLICY,
        ABOUT,

        MENU_THEME,
        BACKGROUND
    }

    ;

    Map<Asset, Object> map = new HashMap<Asset, Object>();

    private final AssetManager assetManager;


    public MenuAssets() {
        assetManager = new AssetManager();
        assetManager.load("textures/menu/main/atlas.atlas", TextureAtlas.class);
        assetManager.load("music/menu_theme.ogg", Music.class);
    }

    public void onLoaded() {
        mainAtlas = assetManager.get("textures/menu/main/atlas.atlas", TextureAtlas.class);
        map.put(Asset.BACKGROUND, mainAtlas.findRegion("background"));

        map.put(Asset.COMPETITORS_TEXTURE, mainAtlas.findRegion("competitors"));
        map.put(Asset.LEADERBOARDS_TEXTURE, mainAtlas.findRegion("leaderboards"));
        map.put(Asset.TUTORIAL, mainAtlas.findRegion("tutorial"));
        map.put(Asset.GO_FOR_RECORD, mainAtlas.findRegion("go_for_record"));
        map.put(Asset.DEV_NOTE, mainAtlas.findRegion("dev_notes"));
        map.put(Asset.PRIVACY_POLICY, mainAtlas.findRegion("privacy_policy"));
        map.put(Asset.ABOUT, mainAtlas.findRegion("about"));


        map.put(Asset.EXIT_TEXTURE, mainAtlas.findRegion("exit"));
        map.put(Asset.HELP_TEXTURE, mainAtlas.findRegion("help"));
        map.put(Asset.HILLS_TEXTURE, mainAtlas.findRegion("hills"));
        map.put(Asset.ONLINE_TEXTURE, mainAtlas.findRegion("online"));
        map.put(Asset.START_TEXTURE, mainAtlas.findRegion("start"));
        map.put(Asset.WORLD_CUP_TEXTURE, mainAtlas.findRegion("solo"));
        map.put(Asset.BACK_BUTTON_TEXTURE, mainAtlas.findRegion("back"));
        map.put(Asset.ADD_BUTTON_TEXTURE, mainAtlas.findRegion("add_icon"));

        map.put(Asset.CHECKBOX_CHECKED_TEXTURE, mainAtlas.findRegion("checked_checkbox"));
        map.put(Asset.CHECKBOX_UNCHECKED_TEXTURE, mainAtlas.findRegion("unchecked_checkbox"));

        map.put(Asset.CPU_PLAYER_ENABLED_TEXTURE, mainAtlas.findRegion("cpu_player_icon"));
        map.put(Asset.CPU_PLAYER_DISABLED_TEXTURE, mainAtlas.findRegion("cpu_player_icon_unchecked"));
        map.put(Asset.NORMAL_PLAYER_ENABLED_TEXTURE, mainAtlas.findRegion("normal_player_icon"));
        map.put(Asset.NORMAL_PLAYER_DISABLED_TEXTURE, mainAtlas.findRegion("normal_player_icon_unchecked"));
        map.put(Asset.EDIT_ICON_TEXTURE, mainAtlas.findRegion("edit_icon"));
        map.put(Asset.DELETE_ICON_TEXTURE, mainAtlas.findRegion("delete_icon"));
        map.put(Asset.ACCEPT_ICON_TEXTURE, mainAtlas.findRegion("accept_icon"));

        map.put(Asset.ARROW_UP_TEXTURE, mainAtlas.findRegion("arrow_up"));
        map.put(Asset.ARROW_DOWN_TEXTURE, mainAtlas.findRegion("arrow_down"));

        map.put(Asset.WEATHER_DAY_ICON, mainAtlas.findRegion("weather_day_icon"));
        map.put(Asset.WEATHER_MORNING_ICON, mainAtlas.findRegion("weather_morning_icon"));
        map.put(Asset.WEATHER_NIGHT_ICON, mainAtlas.findRegion("weather_night_icon"));

        map.put(Asset.SNOWING_NO_ICON, mainAtlas.findRegion("snowing_no_icon"));
        map.put(Asset.SNOWING_SOFT_ICON, mainAtlas.findRegion("snowing_soft_icon"));
        map.put(Asset.SNOWING_HARD_ICON, mainAtlas.findRegion("snowing_hard_icon"));

        map.put(Asset.ZAKOPANE_HILL_TEXTURE, mainAtlas.findRegion("hill_zakopane"));
        map.put(Asset.MENU_THEME, assetManager.get("music/menu_theme.ogg", Music.class));

    }

    public <T> T get(Asset key, Class<T> tClass) {
        if (map.get(key) == null) {
            System.err.println("ASSET " + key + " NOT FOUND");
        }
        return (T) map.get(key);
    }

    public boolean update() {
        if(assetManager.update()){
            onLoaded();
            return true;
        }
        return false;
    }

    public void dispose() {
        assetManager.dispose();
    }
}
