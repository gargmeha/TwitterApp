package mehagarg.android.twitterapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mehagarg.android.twitterapp.data.YambaContract;

/**
 * Created by meha on 4/20/16.
 */
public class TimelineDetailFragment extends Fragment {

    private View details;

    public static Intent marshallDetails(Context context, long ts, String handle, String tweet) {
        Intent i = new Intent(context, TimelineDetailActivity.class);
        i.putExtra(YambaContract.Timeline.Columns.TIMESTAMP, ts);
        i.putExtra(YambaContract.Timeline.Columns.HANDLE, handle);
        i.putExtra(YambaContract.Timeline.Columns.TWEET, tweet);
//        i.putExtra(YambaContract.Timeline.Columns.IMAGE_URL, imageURL);
        return i;
    }

    public static Fragment newInstance(Bundle args) {
        Fragment details = new TimelineDetailFragment();
        details.setArguments(args);
        return details;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timeline_detail, container, false);
        details = v.findViewById(R.id.timeline_details);
        setDetails(getArguments());
        return v;
    }

    public void setDetails(Bundle args) {
        if ((null == args) || (null == details)) { return; }

        ((TextView) details.findViewById(R.id.timeline_detail_timestamp))
                .setText(DateUtils.getRelativeTimeSpanString(
                        args.getLong(YambaContract.Timeline.Columns.TIMESTAMP, 0L)));
        ((TextView) details.findViewById(R.id.timeline_detail_handle)).setText(
                args.getString(YambaContract.Timeline.Columns.HANDLE));
        ((TextView) details.findViewById(R.id.timeline_detail_tweet)).setText(
                args.getString(YambaContract.Timeline.Columns.TWEET));
    }
}
