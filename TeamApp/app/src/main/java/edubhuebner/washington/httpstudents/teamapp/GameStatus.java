package edubhuebner.washington.httpstudents.teamapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;


public class GameStatus extends Activity {

    public TextView statusquery;
    public TextView timequery;
    public String passtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_status);

        statusquery = (TextView) findViewById(R.id.status_query);
        Status status = new Status();

        timequery = (TextView) findViewById((R.id.time_query));
        Time time = new Time();

        status.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_status, menu);
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

    private class Status extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            try {
                MongoClientURI uri = new MongoClientURI("mongodb://Team_2:Team_2@ds031701.mongolab.com:31701/gameapp");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());

                DBCollection game = db.getCollection("game");

                DBCursor Status = game.find(new BasicDBObject("Status", " In Progress"));

                if (Status.count() < 1) {
                    return " Start New Game";
                } else {
                    return " In Progress";
                }
            } catch (UnknownHostException e) {
                return "Unknown Host Exception";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            statusquery.setText(result);
        }
    }

    private class Time extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            try {
                MongoClientURI uri = new MongoClientURI("mongodb://Team_2:Team_2@ds031701.mongolab.com:31701/gameapp");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());

                DBCollection game = db.getCollection("game");

                DBObject cursor = game.findOne();
                passtime = String.valueOf(cursor.get("Time"));
                client.close();

                return String.valueOf(passtime);
            } catch (UnknownHostException e) {
                return "NO!";
            }
        }

        @Override
        protected void onPostExecute(String result) { timequery.setText(result); }
    }
}



