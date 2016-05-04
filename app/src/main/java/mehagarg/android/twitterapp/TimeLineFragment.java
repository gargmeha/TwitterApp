package mehagarg.android.twitterapp;


import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import mehagarg.android.twitterapp.data.YambaContract;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimeLineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimeLineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeLineFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int TIMELINE_LOADER = 666;

    public static final String[] FROM = new String[]{
            YambaContract.Timeline.Columns.HANDLE,
            YambaContract.Timeline.Columns.TIMESTAMP,
            YambaContract.Timeline.Columns.TWEET,
    };

    public static final int[] TO = new int[]{
            R.id.timeline_handle,
            R.id.timeline_time,
            R.id.timeline_tweet,
    };

    static class TimelineBinder implements SimpleCursorAdapter.ViewBinder{
        @Override
        public boolean setViewValue(View view, Cursor c, int idx) {
            if (R.id.timeline_time != view.getId()) { return false; }

            CharSequence s = "long ago";
            long t = c.getLong(idx);
            if (0 < t) { s = DateUtils.getRelativeTimeSpanString(t); }
            ((TextView) view).setText(s);
            return true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.row_timeline,
                null,
                FROM,
                TO,
                0);
        adapter.setViewBinder(new TimelineBinder());
        setListAdapter(adapter);
        getLoaderManager().initLoader(TIMELINE_LOADER, null, this);
        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int p, long id) {
        Cursor c = (Cursor) l.getItemAtPosition(p);

        Intent i = TimelineDetailFragment.marshallDetails(
                getActivity(),
                c.getLong(c.getColumnIndex(YambaContract.Timeline.Columns.TIMESTAMP)),
                c.getString(c.getColumnIndex(YambaContract.Timeline.Columns.HANDLE)),
                c.getString(c.getColumnIndex(YambaContract.Timeline.Columns.TWEET)));

        startActivity(i);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                YambaContract.Timeline.URI,
                null,
                null,
                null,
                YambaContract.Timeline.Columns.TIMESTAMP + " DESC");
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {@link FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p/>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p/>
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link Cursor}
     * and you place it in a {@link CursorAdapter}, use
     * the {@link CursorAdapter#CursorAdapter(Context,
     * Cursor, int)} constructor <em>without</em> passing
     * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link Cursor} from a {@link CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link CursorAdapter}, you should use the
     * {@link CursorAdapter#swapCursor(Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ((SimpleCursorAdapter) getListAdapter()).swapCursor(data);
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((SimpleCursorAdapter) getListAdapter()).swapCursor(null);
    }
}
