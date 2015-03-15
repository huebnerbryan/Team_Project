package edubhuebner.washington.httpstudents.teamapp;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected static final String TAG = "basic-location-sample";

    protected GoogleApiClient mGoogleApiClient;
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
    protected Location mCurrentLocation;
    protected Location mLastLocation;
    protected Status mLaststatus;
    /*protected TextView mLatitudeText;
    protected TextView mLongitudeText;*/
    protected Boolean mRequestingLocationUpdates;
    protected String mLastUpdateTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        mLongitudeText = (TextView) findViewById((R.id.longitude_text));*/
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            /*mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));*/
        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onPostClick(View view) {
        PostLocation postlocation = new PostLocation();
        postlocation.execute();
        Toast.makeText(this, "You have joined - verify game status", Toast.LENGTH_SHORT).show();
    }

    private class PostLocation extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            try{
                MongoClientURI uri = new MongoClientURI("mongodb://Team_2:Team_2@ds031701.mongolab.com:31701/gameapp");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());

                DBCollection game = db.getCollection("game");

                SimpleDateFormat time = new SimpleDateFormat(" EEE MMM dd ' at: ' hh:mm a " );
                String now = time.format(new Date());

                if (mLastLocation != null) {
                    BasicDBObject LastLocation = new BasicDBObject();
                    LastLocation.put("Latitude", String.valueOf(mLastLocation.getLatitude()));
                    LastLocation.put("Longitude", String.valueOf(mLastLocation.getLongitude()));
                    LastLocation.put("Time", String.valueOf( now));
                    LastLocation.put("Status", String.valueOf(" In Progress"));
                    LastLocation.put("Results", String.valueOf(" Winner! Winner!"));

                    game.insert(LastLocation);
                    client.close();

                    return "You have joined - verify game status";
                } else {
                    client.close();
                    return "Coordinates undetected - turn on device location";
                }
            } catch (UnknownHostException e) {
                return "Unknown Host Exception";
            }

        }
    }

    public void onStatusClick(View view) {
        Intent intent = new Intent(this, GameStatus.class);
        startActivity(intent);
    }
}

