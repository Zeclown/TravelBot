package com.chartrand.pierreolivier.travelbot;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MainActivity extends Activity {
    MainDBHelper dbhelp;
    public static final String MY_PREFS_NAME = "TBOT_Prefs";
    SharedPreferences prefs;
    String accountName;
    TextView testText;
    String apiKeySummary="AIzaSyAn5n5F9DD6hM_KwJqf03UAp9WzWQZHP_w";
    String appId="9fa60d11";
    EditText countryNameEdit;
    //String urlEndPoint = "https://en.wikipedia.org/w/api.php";
    String urlEndPoint = "https://api.aylien.com/api/v1/summarize";
    String urlTemplate="?url=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FCountry&title=Country";
    String urlReal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        testText=(TextView)findViewById(R.id.emailText);
        countryNameEdit=(EditText)findViewById(R.id.main_countryNameEdit);
        countryNameEdit.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    return true;
                }
                return false;
            }
        });
    }

    protected void onResume() {
        super.onResume();
        dbhelp = new MainDBHelper(this);
        Intent intentWelcome = new Intent(this, WelcomeActivity.class);
        accountName = prefs.getString("name", null);
        Intent intentMap = new Intent(this, MapsActivity.class);
        if (accountName == null)
            startActivityForResult(intentWelcome, 0);
        else
            startMainMap();

    }
    public void onActivityResult()
    {
        startMainMap();
    }
    public void startMainMap()
    {
            Intent intentMap = new Intent(this, MapsActivity.class);
            startActivity(intentMap);
    }
    public void searchCountry(){

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest req = new JsonObjectRequest(urlEndPoint+urlReal, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("err","error");
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-AYLIEN-TextAPI-Application-Key", apiKeySummary);
                headers.put("X-AYLIEN-TextAPI-Application-ID", appId);
                return headers;
            }
        };

// Add the request to the RequestQueue.
        queue.add(req);


    }
}
