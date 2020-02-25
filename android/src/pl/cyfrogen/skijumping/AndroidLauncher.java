package pl.cyfrogen.skijumping;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Map;

import pl.cyfrogen.skijumping.platform.LeaderboardListener;
import pl.cyfrogen.skijumping.platform.PlatformAPI;
import pl.cyfrogen.skijumping.platform.SignInListener;


public class AndroidLauncher extends AndroidApplication implements PlatformAPI {
    private static final int RC_SIGN_IN = 123;
    private static final int RC_LEADERBOARD_UI = 124;
    GoogleSignInOptions signInOptions =
            new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                    .build();
    private SignInListener signInListener;
    private FirebaseAnalytics firebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        initialize(new Main(this), config);

    }

    private void signInSilently() {
        GoogleSignInOptions signInOptions = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (GoogleSignIn.hasPermissions(account, signInOptions.getScopeArray())) {
            // Already signed in.
            // The signed in account is stored in the 'account' variable.
            GoogleSignInAccount signedInAccount = account;
        } else {
            // Haven't been signed-in before. Try the silent sign-in first.
            GoogleSignInClient signInClient = GoogleSignIn.getClient(this, signInOptions);
            signInClient
                    .silentSignIn()
                    .addOnCompleteListener(
                            this,
                            new OnCompleteListener<GoogleSignInAccount>() {
                                @Override
                                public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                                    if (task.isSuccessful()) {
                                        // The signed in account is stored in the task's result.
                                        GoogleSignInAccount signedInAccount = task.getResult();
                                    } else {
                                        // Player will need to sign-in explicitly using via UI.
                                        // See [sign-in best practices](http://developers.google.com/games/services/checklist) for guidance on how and when to implement Interactive Sign-in,
                                        // and [Performing Interactive Sign-in](http://developers.google.com/games/services/android/signin#performing_interactive_sign-in) for details on how to implement
                                        // Interactive Sign-in.
                                    }
                                }
                            });
        }
    }

    private void startSignInIntent(SignInListener listener) {
        this.signInListener = listener;
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // The signed in account is stored in the result.
                GoogleSignInAccount signedInAccount = result.getSignInAccount();
                signInListener.success();
            } else {
                String message = result.getStatus().getStatusMessage();
                signInListener.error();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        signInSilently();
    }

    @Override
    public boolean isSignedIn() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (GoogleSignIn.hasPermissions(account, signInOptions.getScopeArray())) {
            return true;
        }
        return false;
    }

    @Override
    public void signInAsync(SignInListener listener) {
        if (!isSignedIn()) {
            startSignInIntent(listener);
        }
    }

    @Override
    public void submitScore(String scoreboard, long score) {
        if (isSignedIn()) {
            Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .submitScore(scoreboard, score);
        }
    }

    @Override
    public void showLeaderboard(String scoreboard, final LeaderboardListener listener) {
        Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getLeaderboardIntent(scoreboard)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {

                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_LEADERBOARD_UI);
                        listener.success();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
				        listener.error();
                    }
                });
    }

    @Override
    public void sendLoggingEvent(String id, Map<String, String> values) {
        Bundle params = new Bundle();
        for(Map.Entry<String, String> entry : values.entrySet()){
            params.putString(entry.getKey(), entry.getValue());
        }
        firebaseAnalytics.logEvent(id, params);
    }

    @Override
    public void logCrash(Exception e) {
        Crashlytics.logException(e);
    }

    @Override
    public void logCrashMessage(String s) {
        Crashlytics.log(s);
    }

    @Override
    public void logCrashString(String key, String value) {
        Crashlytics.setString(key, value);
    }
}
