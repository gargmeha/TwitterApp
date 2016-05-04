package mehagarg.android.twitterapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import mehagarg.android.twitterapp.service.YambaService;

public class TimeLineActivity extends YambaActivity {

    private static final String DETAIL_FRAGMENT = "Timeline.DETAILS";

    private boolean usingFragments;

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        super.startActivityFromFragment(fragment, intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        usingFragments = (null != findViewById(R.id.timeline_details));

        if(usingFragments){
            addDetailFragment();
        }
    }

    private void addDetailFragment() {
        FragmentManager fm = getSupportFragmentManager();
        if(null!= fm.findFragmentByTag(DETAIL_FRAGMENT)){
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.timeline_details, new TimelineDetailFragment()
        , DETAIL_FRAGMENT).commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        YambaService.startPolling(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        YambaService.stopPolling(this);
    }
}
