package mehagarg.android.twitterapp.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import mehagarg.android.twitterapp.R;

/**
 * Created by meha on 4/20/16.
 */
public class YambaService extends IntentService {
    private static final String TAG = "SERVICE";
    private static final String PARAM_OP = "YambaService.OP";
    private static final String PARAM_TWEET = "YambaService.TWEET";
    private static final int OP_POLL = -1;
    private static final int OP_POST = -2;
    private static final int POLL_REQ = 42;

    private volatile YambaLogic helper;

    public YambaService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        helper = new YambaLogic(this);
    }

    public static void startPolling(Context context) {
        long t = 1000 * context.getResources().getInteger(R.integer.poll_interval);
        ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE))
                .setInexactRepeating(AlarmManager.RTC,
                        System.currentTimeMillis() + 100,
                        t, getPollingIntent(context));
    }

    private static PendingIntent getPollingIntent(Context context) {
        Intent i = new Intent(context, YambaService.class);
        i.putExtra(PARAM_OP, OP_POLL);
        return PendingIntent.getService(context, POLL_REQ, i, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void stopPolling(Context context) {
        ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE))
                .cancel(getPollingIntent(context));
    }


    public static void postTweet(Context context, String tweet) {
        Intent i = new Intent(context, YambaService.class);
        i.putExtra(PARAM_OP, OP_POLL);
        i.putExtra(PARAM_TWEET, tweet);
        context.startService(i);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int op = intent.getIntExtra(PARAM_OP, 0);
        switch (op) {
            case OP_POLL:
                helper.doPoll();
                break;
            case OP_POST:
                helper.doPost(intent.getStringExtra(PARAM_TWEET));
                break;
            default:
                throw new IllegalArgumentException("");
        }
    }
}
