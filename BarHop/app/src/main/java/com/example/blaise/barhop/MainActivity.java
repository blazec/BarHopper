package com.example.blaise.barhop;

import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private TextView barNameView;
    private TextView barAddressView;
    private ImageView refreshImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        double latitude = 0;  // assign latitude (fixed for now)
        double longitude = 0;  // assign longitude (fixed for now)
        final String type = "bar";
        final String keywords = "pub";
        int radius = 500;  // assign search radiuse (fixed for now)

        getRandomBar(latitude, longitude, type, keywords, radius);
        refreshImageView = (ImageView) findViewById(R.id.refreshImageView);
        refreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomBar(latitude, longitude, type, keywords, radius);
            }
        });

    }

    private void getRandomBar(double latitude, double longitude, String type, String keywords,
                             int radius) {

        String serverKey = "";  // fill in Googe Play Sevices server key
        String apiKey = "";  // fill in Google Play Services API key

        String url = "https://maps.googleapis.com/maps/api/place/search/json" +
                "?keyword=" + keywords +
                "&location=" + latitude + "," + longitude +
                "&radius=" + radius +
                "&types=" + type +
                "&key=" + serverKey;

        Log.v(TAG, url);


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();



        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(TAG, "HTTP Request Failure");

            }

            @Override
            public void onResponse(Response response) throws IOException {

                try {
                    final String placesData = response.body().string();
                    Log.v(TAG, placesData);
                    final JSONObject placesDetails = new JSONObject(placesData);
                    JSONArray results = placesDetails.getJSONArray("results");

                    int numResults = results.length();
                    int randInt;
                    Random index = new Random();
                    randInt = index.nextInt(numResults);

                    JSONObject nextBar = results.getJSONObject(randInt);
                    final NearbyBar barDetails = getBarDetails(nextBar);
                    //final String barName = nextBar.getString("name");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            updateDisplay(barDetails);

                        }
                    });
                }
                catch (IOException e) {
                    Log.e(TAG, "Exception");
                }

                catch (JSONException e) {
                    Log.e(TAG, "JSON Exception");
                }

            }
        });


    }

    private void updateDisplay(NearbyBar nearbyBar) {

        barNameView = (TextView)findViewById(R.id.placesDetailsTextView);
        barNameView.setText(nearbyBar.getName());
        barAddressView = (TextView)findViewById(R.id.barAddress);
        barAddressView.setText(nearbyBar.getAddress());

    }

    private NearbyBar getBarDetails(JSONObject barData) throws JSONException{

        NearbyBar nearbyBar = new NearbyBar();
        nearbyBar.setName(barData.getString("name"));
        nearbyBar.setAddress(barData.getString("vicinity"));
        //nearbyBar.setOpenNow(barData.getBoolean(""));

        return nearbyBar;
    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
