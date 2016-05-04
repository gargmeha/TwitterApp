package mehagarg.android.twitterapp;

import android.content.Intent;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by meha on 4/20/16.
 */
public abstract class YambaActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.yamba, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_tweet:
                nextPage(TweetActivity.class);
                break;
            case R.id.menu_timeline:
                nextPage(TimeLineActivity.class);
                break;
            case R.id.menu_prefs:
                startActivity(new Intent(this, PrefsActivity.class));
//                nextPage(PrefsActivity.class); // i could have done it, but I dont want to set the flag here.
                break;
            case R.id.menu_contacts:
                Intent intent= new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);
                break;
            case R.id.menu_about:
                about();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void about() {
        Toast.makeText(this, R.string.title_about, Toast.LENGTH_LONG).show();

    }

    private void nextPage(Class<?> klass) {
        Intent i = new Intent(this, klass);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }
}
