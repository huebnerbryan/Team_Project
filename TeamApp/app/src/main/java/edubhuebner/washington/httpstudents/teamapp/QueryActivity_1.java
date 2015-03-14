package edubhuebner.washington.httpstudents.teamapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;


public class QueryActivity_1 extends Activity {

    protected TextView queryTime;
    protected TextView querystatus;
    protected TextView queryresult;

    protected Status mgamestatus;

    public String passlat;
    public String passlong;
    public String passtime;
    public String passstatus;
    public String passresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_activity_1);

        QueryLocation querylocation = new QueryLocation();
        querylocation.execute();

        queryTime = (TextView) findViewById((R.id.time_query));
        querystatus = (TextView) findViewById((R.id.status_query));
        queryresult = (TextView) findViewById((R.id.result_query));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_query_activity_1, menu);
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

// QUERY LOCATION OF HIDER!!!

    private class QueryLocation extends AsyncTask<Void, Void, String> {

       protected String doInBackground(Void... voids) {

            try {
                MongoClientURI uri = new MongoClientURI("mongodb://Team_2:Team_2@ds031701.mongolab.com:31701/gameapp");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());

                DBCollection game = db.getCollection("game");
                DBCursor cursor = game.find().sort(new BasicDBObject("$natural", -1));

                passlat = String.valueOf(cursor.one().get("Latitude"));
                passlong = String.valueOf(cursor.one().get("Longitude"));
                passtime = String.valueOf(cursor.one().get("Time"));
                passstatus = String.valueOf(cursor.one().get("Status"));
                passresult = String.valueOf(cursor.one().get("Results"));

                cursor.close();
                client.close();

                return "Location Submitted";

            } catch (UnknownHostException e) {
                return "Unknown host exception";
            }
       }

        @Override
        protected void onPostExecute(String status) {

            queryTime.setText(passtime);
            querystatus.setText(passstatus);
            queryresult.setText(passresult);
        }
    }
}


/*
Pull last entry (most current) from database

<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

public String getMacAddress(Context context) {
        WifiManager wimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String macAddress = wimanager.getConnectionInfo().getMacAddress();
        if (macAddress == null) {
        macAddress = "Device don't have mac address or wi-fi is disabled";
        }
        return macAddress;
        }
*/
