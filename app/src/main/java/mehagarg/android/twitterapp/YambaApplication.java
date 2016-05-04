package mehagarg.android.twitterapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.marakana.android.yamba.clientlib.YambaClient;

/**
 * Created by meha on 4/20/16.
 */
public class YambaApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {


    public static final String TAG = "APP";
    public static final String DEF_HANDLE = "student";
    public static final String DEF_PASSWORD = "password";
    public static final String DEF_ENDPOINT = "http://yamba.newcircle.com/api";

    private YambaClient client;
    private String handleKey;
    private String pwdKey;
    private String uriKey;

    @Override
    public void onCreate() {
        super.onCreate();

        handleKey = getString(R.string.prefs_key_handle);
        pwdKey = getString(R.string.prefs_key_passwd);
        uriKey = getString(R.string.prefs_key_uri);

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        client = null;
    }

    public synchronized YambaClient getClient() {
        if (null == client) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

            String usr = pref.getString(handleKey, DEF_HANDLE);
            String pwd = pref.getString(pwdKey, DEF_PASSWORD);
            String uri = pref.getString(uriKey, DEF_ENDPOINT);

            try {
                client = new YambaClient(usr, pwd, uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return client;
    }
}
