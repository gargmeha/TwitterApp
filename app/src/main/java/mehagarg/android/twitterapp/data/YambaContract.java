package mehagarg.android.twitterapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by meha on 4/20/16.
 */
public final class YambaContract {

    private YambaContract() {}

    public static final long VERSION = 1;

    // Package name
    public static final String AUTHORITY = "mehagarg.android.twitterapp";

    // content://mehagarg.android.twitterapp
    public static final Uri BASE_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .build();

    public static final String PERMISSION_READ =
            "mehagarg.android.twitterapp.permission.READ";

    public static final String PERMISSION_WRITE =
            "mehagarg.android.twitterapp.permission.WRITE";

    public static class MaxTimeline{
        private MaxTimeline() {
        }

        public static final String TABLE = "maxTimeLine";
        public static final Uri URI = BASE_URI.buildUpon().appendPath(TABLE).build();
        public static final String MINOR_TYPE = "/vnd." + TABLE + AUTHORITY;
        public static final String ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + MINOR_TYPE;
//        public static final String DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + MINOR_TYPE;

        public static final class Columns{
            private Columns(){}
            public static final String TIMESTAMP = "timestamp";
        }
    }

    public static class Timeline{
        private Timeline() {}

        public static final String TABLE = "timeline";
        public static final Uri URI = BASE_URI.buildUpon().appendPath(TABLE).build();
        private static final String MINOR_TYPE = "/vnd." + AUTHORITY + "." + TABLE;
//        vnd.android.cursor.item/vnd.#authority.#table
        public static final String ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + MINOR_TYPE;

        //VENDOR MIME TYPE vnd.android.cursor.dir/vnd.#authority.#table
        public static final String DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + MINOR_TYPE;

        public static class Columns{
            private Columns(){}
            public static final String ID = BaseColumns._ID;
            public static final String HANDLE = "handle";
            public static final String TIMESTAMP = "timestamp";
            public static final String TWEET = "tweet";
        }
    }


}
