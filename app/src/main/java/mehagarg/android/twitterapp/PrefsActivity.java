package mehagarg.android.twitterapp;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by meha on 4/20/16.
 */
public class PrefsActivity extends YambaActivity {

    public static class Prefs extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new Prefs())
                .commit();
    }
}
