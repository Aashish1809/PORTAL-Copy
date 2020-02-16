package com.example.aashish.portal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import com.example.aashish.portal.helper.*;

public class ConfirmedEventDisplay extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_VENUE = "venue";
    private static final String KEY_TOKEN = "token";
    private TextView titlee,tokene;
    private TextView descriptione;
    private TextView datee;
    private TextView timee;
    private TextView venuee;
    private  String title;
    private String description;
    private String date;
    private String time;
    private String venue;
    private int success;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed_event_display);
        titlee = findViewById(R.id.titlee);
        descriptione = findViewById(R.id.descriptione);
        datee = findViewById(R.id.datee);
        timee = findViewById(R.id.timee);
        venuee = findViewById(R.id.venuee);
        Intent intent = getIntent();
        title = intent.getStringExtra(KEY_TITLE);
        new FetchDataAsyncTask().execute();
    }
    private class FetchDataAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_TITLE, title);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "RetrieveConfirmedEvents2.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject object;
                if (success == 1) {
                    object = jsonObject.getJSONObject(KEY_DATA);
                    title = object.getString(KEY_TITLE);
                    description = object.getString(KEY_DESCRIPTION);
                    date = object.getString(KEY_DATE);
                    time =object.getString(KEY_TIME);
                    venue = object.getString(KEY_VENUE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                public void run() {
                    //Populate the Edit Texts once the network activity is finished executing
                    titlee.setText(title);
                    descriptione.setText(description);
                    datee.setText(date);
                    timee.setText(time);
                    venuee.setText(venue);
                }
            });
        }

    }

}
