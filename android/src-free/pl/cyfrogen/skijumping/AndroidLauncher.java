package pl.cyfrogen.skijumping;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.Map;

import pl.cyfrogen.skijumping.platform.LeaderboardListener;
import pl.cyfrogen.skijumping.platform.PlatformAPI;
import pl.cyfrogen.skijumping.platform.SignInListener;


public class AndroidLauncher extends AndroidApplication implements PlatformAPI {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;
        initialize(new Main(this), config);

    }

    @Override
    public boolean isSignedIn() {
        return false;
    }

    @Override
    public void signInAsync(SignInListener listener) {

    }

    @Override
    public void submitScore(String scoreboard, long score) {

    }

    @Override
    public void showLeaderboard(String scoreboard, LeaderboardListener listener) {

    }

    @Override
    public void sendLoggingEvent(String id, Map<String, String> values) {

    }

    @Override
    public boolean areLeaderboardsSupported() {
        return false;
    }

    @Override
    public void logCrash(Exception e) {

    }

    @Override
    public void logCrashMessage(String s) {

    }

    @Override
    public void logCrashString(String key, String value) {

    }

}
