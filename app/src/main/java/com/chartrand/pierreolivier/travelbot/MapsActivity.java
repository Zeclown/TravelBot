package com.chartrand.pierreolivier.travelbot;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.geonames.Toponym;
import org.geonames.WebService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button mailButton;
    private TravelBot mTb;
    private Marker botOnMap;
    TextView countryName;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    public static final String MY_PREFS_NAME = "TBOT_Prefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        //mMap.getUiSettings().setZoomControlsEnabled(false);
        //mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setTrafficEnabled(false);
        try {
            findNewPos();
        } catch (IOException e) {
            e.printStackTrace();
        }


        botOnMap=mMap.addMarker(new MarkerOptions().position(mTb.getPosition()).title("bot"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mTb.getPosition()));
        final Handler h = new Handler();


        h.postDelayed(new Runnable() {
            public void run() {
                savePos();
                try {
                    updateTBPos();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                h.postDelayed(this, 100);
            }
        }, 100);
    }
    public void updateTBPos() throws IOException {
        botOnMap.setPosition(mTb.move(0.01f));
    }
    private void findNewPos() throws IOException {
        String lastTime=prefs.getString("LastMapTime", null);
        float lastLat=prefs.getFloat("LastLatCoord", 0);
        float lastLong=prefs.getFloat("LastLongCoord", 0);
        if(lastTime==null)
        {
            LatLng sydney = new LatLng(-34, 151);
            mTb=new TravelBot(sydney,false,0,50000,this);
        }
        else
        {
            mTb=new TravelBot(new LatLng(lastLat,lastLong),false,0,50000,this);
            mTb.move((System.nanoTime()-Long.parseLong(lastTime))/ 1000000000.0f /60);
        }
        Geocoder geoCoder = new Geocoder(this);
        List<Address> matches = geoCoder.getFromLocation(mTb.getPosition().latitude, mTb.getPosition().longitude, 1);
        Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
        if(bestMatch!=null)
            countryName.setText(bestMatch.getCountryName());
    }
    public void OnResume()  {

        try {
            findNewPos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void OnPause()
    {
        savePos();
    }
    public void savePos()
    {
        //stores the last position of the bot and the time of that last position
        Long tsLong = System.nanoTime();//using nano so we can't change the time using the phone, ha!
        String ts = tsLong.toString();
        editor.putString("LastMapTime", ts);///add the new name to prefs
        editor.putFloat("LastLatCoord", ((float) mTb.getPosition().latitude));
        editor.putFloat("LastLongCoord", ((float) mTb.getPosition().longitude));
        editor.commit();
    }
    public void getEmail(View v)
    {
        Intent intentMail = new Intent(this, EmailActivity.class);
        startActivity(intentMail);
    }
    public void newEmail(Letter mail)
    {

    }
    class LetterBuilder extends AsyncTask<String, String, String> {
        private String buildLetter() {



            String rawText = "Hi! I went to : ";
            List<Toponym> nearby = new ArrayList<Toponym>();
            for (int i = 0; i < mTb.checkPos.size(); i++) {
                try {
                    nearby = WebService.findNearbyPlaceName(mTb.checkPos.get(i).latitude, mTb.checkPos.get(i).longitude, 260f, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (nearby.size() != 0) {
                    rawText += nearby.get(0).getName();

                }
            }
            return rawText;


        }

        @Override
        protected String doInBackground(String... params) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String dateFormated = dateFormat.format(date);

            String letter=buildLetter();
            newEmail(new Letter(dateFormated,letter));
            return null;
        }
    }

    }
