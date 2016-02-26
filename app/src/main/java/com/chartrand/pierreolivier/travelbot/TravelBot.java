package com.chartrand.pierreolivier.travelbot;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.geonames.Toponym;
import org.geonames.WebService;
import org.geonames.WikipediaArticle;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

/**
 * Created by Admin on 21/01/2016.
 */
public class TravelBot {
    private final String GEOUSERNAME = "miniconco";
    private LatLng position;
    private boolean sleeping;
    private float rotation;
    private float speed;
    private boolean letterSent;
    private final float CHECK_DISTANCE = 100000f;
    public float totalTraveled;



    public Vector<LatLng> checkPos;
    Geocoder geocoder;
    private Context context;

    public TravelBot() {
    }

    public TravelBot(LatLng position, boolean sleeping, float rotation, float speed, Context context) {
        this.position = position;
        this.sleeping = sleeping;
        this.rotation = rotation;
        this.speed = speed;
        totalTraveled = 0;

        checkPos = new Vector<LatLng>();
        this.context = context;
        geocoder = new Geocoder(context);
        WebService.setUserName(GEOUSERNAME);
        letterSent = false;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public boolean isSleeping() {
        return sleeping;
    }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public LatLng move(float lastMovementMin) throws IOException {
        float distance = lastMovementMin * speed;

        if (totalTraveled + distance > CHECK_DISTANCE) {
            checkPos.add(new LatLng(position.latitude, position.longitude));

        }

        totalTraveled += distance;
        position = LatLongUtil.extrapolate(position.latitude, position.longitude, 0, distance);
        return position;
    }


}