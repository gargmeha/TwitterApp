package mehagarg.android.twitterapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.Map;

/**
 * Created by meha on 4/20/16.
 */
public class YambaProvider extends ContentProvider {
    private YambaDbHelper dbHelper;
    public static final int MAX_TIMELINE_ITEM_TYPE = 1;
    public static final int TIMELINE_ITEM_TYPE = 2;
    public static final int TIMELINE_DIR_TYPE = 3;

    public static final UriMatcher MATCHER =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(
                YambaContract.AUTHORITY,
                YambaContract.MaxTimeline.TABLE,
                MAX_TIMELINE_ITEM_TYPE
        );

        MATCHER.addURI(YambaContract.AUTHORITY,
                YambaContract.Timeline.TABLE + "/#",
                TIMELINE_ITEM_TYPE);

        MATCHER.addURI(YambaContract.AUTHORITY,
                YambaContract.Timeline.TABLE,
                TIMELINE_DIR_TYPE);

    }

    private static final Map<String, String> PROJ_MAP_MAX_TIMELINE = new ProjectionMap.Builder()
            .addColumn(
                    YambaContract.MaxTimeline.Columns.TIMESTAMP,
                    "max(" + YambaDbHelper.COL_TIMESTAMP + ")")
            .build()
            .getProjectionMap();

    private static final ColumnMap COL_MAP_TIMELINE = new ColumnMap.Builder()
            .addColumn(
                    YambaContract.Timeline.Columns.ID,
                    YambaDbHelper.COL_ID,
                    ColumnMap.Type.LONG)
            .addColumn(
                    YambaContract.Timeline.Columns.TIMESTAMP,
                    YambaDbHelper.COL_TIMESTAMP,
                    ColumnMap.Type.LONG)
            .addColumn(
                    YambaContract.Timeline.Columns.HANDLE,
                    YambaDbHelper.COL_HANDLE,
                    ColumnMap.Type.STRING)
            .addColumn(
                    YambaContract.Timeline.Columns.TWEET,
                    YambaDbHelper.COL_TWEET,
                    ColumnMap.Type.STRING)
            .build();

    private static final Map<String, String> PROJ_MAP_TIMELINE = new ProjectionMap.Builder()
            .addColumn(YambaContract.Timeline.Columns.ID, YambaDbHelper.COL_ID)
            .addColumn(YambaContract.Timeline.Columns.TIMESTAMP, YambaDbHelper.COL_TIMESTAMP)
            .addColumn(YambaContract.Timeline.Columns.HANDLE, YambaDbHelper.COL_HANDLE)
            .addColumn(YambaContract.Timeline.Columns.TWEET, YambaDbHelper.COL_TWEET)
            .build()
            .getProjectionMap();

    /**
     * Implement this to initialize your content provider on startup.
     * This method is called for all registered content providers on the
     * application main thread at application launch time.  It must not perform
     * lengthy operations, or application startup will be delayed.
     * <p/>
     * <p>You should defer nontrivial initialization (such as opening,
     * upgrading, and scanning databases) until the content provider is used
     * (via {@link #query}, {@link #insert}, etc).  Deferred initialization
     * keeps application startup fast, avoids unnecessary work if the provider
     * turns out not to be needed, and stops database errors (such as a full
     * disk) from halting application launch.
     * <p/>
     * <p>If you use SQLite, {@link SQLiteOpenHelper}
     * is a helpful utility class that makes it easy to manage databases,
     * and will automatically defer opening until first use.  If you do use
     * SQLiteOpenHelper, make sure to avoid calling
     * {@link SQLiteOpenHelper#getReadableDatabase} or
     * {@link SQLiteOpenHelper#getWritableDatabase}
     * from this method.  (Instead, override
     * {@link SQLiteOpenHelper#onOpen} to initialize the
     * database when it is first opened.)
     *
     * @return true if the provider was successfully loaded, false otherwise
     */
    @Override
    public boolean onCreate() {
        dbHelper = new YambaDbHelper(getContext());
        return true;
    }

    /**
     * Implement this to handle query requests from clients.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     * <p/>
     * Example client call:<p>
     * <pre>// Request a specific record.
     * Cursor managedCursor = managedQuery(
     * ContentUris.withAppendedId(Contacts.People.CONTENT_URI, 2),
     * projection,    // Which columns to return.
     * null,          // WHERE clause.
     * null,          // WHERE clause value substitution
     * People.NAME + " ASC");   // Sort order.</pre>
     * Example implementation:<p>
     * <pre>// SQLiteQueryBuilder is a helper class that creates the
     * // proper SQL syntax for us.
     * SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
     * <p/>
     * // Set the table we're querying.
     * qBuilder.setTables(DATABASE_TABLE_NAME);
     * <p/>
     * // If the query ends in a specific record number, we're
     * // being asked for a specific record, so set the
     * // WHERE clause in our query.
     * if((URI_MATCHER.match(uri)) == SPECIFIC_MESSAGE){
     * qBuilder.appendWhere("_id=" + uri.getPathLeafId());
     * }
     * <p/>
     * // Make the query.
     * Cursor c = qBuilder.query(mDb,
     * projection,
     * selection,
     * selectionArgs,
     * groupBy,
     * having,
     * sortOrder);
     * c.setNotificationUri(getContext().getContentResolver(), uri);
     * return c;</pre>
     *
     * @param uri           The URI to query. This will be the full URI sent by the client;
     *                      if the client is requesting a specific record, the URI will end in a record number
     *                      that the implementation should parse and add to a WHERE or HAVING clause, specifying
     *                      that _id value.
     * @param projection    The list of columns to put into the cursor. If
     *                      {@code null} all columns are included.
     * @param selection     A selection criteria to apply when filtering rows.
     *                      If {@code null} then all rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     *                      the values from selectionArgs, in order that they appear in the selection.
     *                      The values will be bound as Strings.
     * @param sortOrder     How the rows in the cursor should be sorted.
     *                      If {@code null} then the provider is free to define the sort order.
     * @return a Cursor or {@code null}.
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] proj, String sel, String[] selArgs, String sort) {
        long pk = -1;
        Map<String, String> projMap;
        switch (MATCHER.match(uri)) {
            case MAX_TIMELINE_ITEM_TYPE:
                projMap = PROJ_MAP_MAX_TIMELINE;
                break;
            case TIMELINE_ITEM_TYPE:
                pk = ContentUris.parseId(uri);
            case TIMELINE_DIR_TYPE:
                projMap = PROJ_MAP_TIMELINE;
                break;
            default:
                throw new IllegalArgumentException("Unexpected uri: " + uri);
        }

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(YambaDbHelper.TABLE_TIMELINE);

        qb.setProjectionMap(projMap);

        if (0 < pk) {
            qb.appendWhere(YambaDbHelper.COL_ID + "=" + pk);
        }

        Cursor c = qb.query(getDb(), proj, sel, selArgs, null, null, sort);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    /**
     * Implement this to handle requests for the MIME type of the data at the
     * given URI.  The returned MIME type should start with
     * <code>vnd.android.cursor.item</code> for a single record,
     * or <code>vnd.android.cursor.dir/</code> for multiple items.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     * <p/>
     * <p>Note that there are no permissions needed for an application to
     * access this information; if your content provider requires read and/or
     * write permissions, or is not exported, all applications can still call
     * this method regardless of their access permissions.  This allows them
     * to retrieve the MIME type for a URI when dispatching intents.
     *
     * @param uri the URI to query.
     * @return a MIME type string, or {@code null} if there is no type.
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (MATCHER.match(uri)) {
            case MAX_TIMELINE_ITEM_TYPE:
                return YambaContract.MaxTimeline.ITEM_TYPE;
            case TIMELINE_ITEM_TYPE:
                return YambaContract.Timeline.ITEM_TYPE;
            case TIMELINE_DIR_TYPE:
                return YambaContract.Timeline.DIR_TYPE;
        }
        return null;
    }

    /**
     * Implement this to handle requests to insert a new row.
     * As a courtesy, call {@link ContentResolver#notifyChange(Uri, ContentObserver) notifyChange()}
     * after inserting.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     *
     * @param uri    The content:// URI of the insertion request. This must not be {@code null}.
     * @param values A set of column_name/value pairs to add to the database.
     *               This must not be {@code null}.
     * @return The URI for the newly inserted item.
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("insert not supported");
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] rows) {
        switch (MATCHER.match(uri)) {
            case TIMELINE_DIR_TYPE:
                break;
            default:
                throw new IllegalArgumentException("Unrecognized uri: " + uri);
        }

        SQLiteDatabase db = getDb();
        int count = 0;
        try {
            db.beginTransaction();

            for (ContentValues row : rows) {
                row = COL_MAP_TIMELINE.translateCols(row);
                if (0 < db.insert(YambaDbHelper.TABLE_TIMELINE, null, row)) {
                    count++;
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        if (0 < count) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    /**
     * Implement this to handle requests to delete one or more rows.
     * The implementation should apply the selection clause when performing
     * deletion, allowing the operation to affect multiple rows in a directory.
     * As a courtesy, call {@link ContentResolver#notifyChange(Uri, ContentObserver) notifyChange()}
     * after deleting.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     * <p/>
     * <p>The implementation is responsible for parsing out a row ID at the end
     * of the URI, if a specific row is being deleted. That is, the client would
     * pass in <code>content://contacts/people/22</code> and the implementation is
     * responsible for parsing the record number (22) when creating a SQL statement.
     *
     * @param uri           The full URI to query, including a row ID (if a specific record is requested).
     * @param selection     An optional restriction to apply to rows when deleting.
     * @param selectionArgs
     * @return The number of rows affected.
     * @throws SQLException
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Delete not supported");
    }

    /**
     * Implement this to handle requests to update one or more rows.
     * The implementation should update all rows matching the selection
     * to set the columns according to the provided values map.
     * As a courtesy, call {@link ContentResolver#notifyChange(Uri, ContentObserver) notifyChange()}
     * after updating.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     *
     * @param uri           The URI to query. This can potentially have a record ID if this
     *                      is an update request for a specific record.
     * @param values        A set of column_name/value pairs to update in the database.
     *                      This must not be {@code null}.
     * @param selection     An optional filter to match rows to update.
     * @param selectionArgs
     * @return the number of rows affected.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("update not supported");

    }

    private SQLiteDatabase getDb() {
        return dbHelper.getWritableDatabase();
    }
}
