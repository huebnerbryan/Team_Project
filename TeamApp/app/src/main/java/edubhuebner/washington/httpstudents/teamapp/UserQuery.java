package edubhuebner.washington.httpstudents.teamapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;


public class UserQuery extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_query);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_query, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class User extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            try {
                MongoClientURI uri = new MongoClientURI("mongodb://Team_2:Team_2@ds031701.mongolab.com:31701/gameapp");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());

                DBCollection game = db.getCollection("game");

                DBCursor Results = game.find().sort(new BasicDBObject("Username", "John Doe"));

                if (Results.count() < 0) {
                    return "Enter a Username";
                } else {
                    return "Welcome to the game";
                }
            } catch (UnknownHostException e) {
                return "Unknown Host Exception";
            }
        }
    }
}
