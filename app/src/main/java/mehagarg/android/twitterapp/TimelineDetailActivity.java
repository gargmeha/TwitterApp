package mehagarg.android.twitterapp;

import android.os.Bundle;

/**
 * Created by meha on 4/20/16.
 */
public class TimelineDetailActivity extends YambaActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_detail);
        ((TimelineDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline_detail)).setDetails(getIntent().getExtras());

    }
}
