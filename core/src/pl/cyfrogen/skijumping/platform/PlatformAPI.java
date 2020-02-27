package pl.cyfrogen.skijumping.platform;

import java.util.Map;

public interface PlatformAPI {
    boolean isSignedIn();
    void signInAsync(SignInListener listener);
    void submitScore(String scoreboard, long score);
    void showLeaderboard(String scoreboard, LeaderboardListener listener);
    void sendLoggingEvent(String id, Map<String, String> values);

    boolean areLeaderboardsSupported();

    void logCrash(Exception e);
    void logCrashMessage(String s);
    void logCrashString(String key, String value);

}
