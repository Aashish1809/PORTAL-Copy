package com.example.aashish.portal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import com.example.aashish.portal.helper.*;

public class DetailsDisplayE extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_VENUE = "venue";
    private static final String KEY_TOKEN="token";
    private TextView titlee,tokene;
    private TextView descriptione;
    private TextView datee;
    private TextView timee;
    private TextView venuee;
    private  String title;
    private String description;
    private String date,token;
    private String time;
    private String venue;
    private int success;
    private ProgressDialog pDialog;
    private Button but_ver,but_dis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_display_e);
        titlee= findViewById(R.id.titlee);
        descriptione = findViewById(R.id.descriptione);
        datee = findViewById(R.id.datee);
        timee = findViewById(R.id.timee);
        venuee = findViewById(R.id.venuee);
        tokene=findViewById(R.id.tokene);
        Intent intent =getIntent();
        title = intent.getStringExtra(KEY_TITLE);
        but_ver=findViewById(R.id.verify2);
        but_dis=findViewById(R.id.dismiss2);
        new FetchdataAsyncTask().execute();
        but_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    Verification();
                } else {
                    Toast.makeText(DetailsDisplayE.this, "UNABLE TO CONNECT...!",
                            Toast.LENGTH_SHORT).show();

                }

            }
        });
        but_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    Deletion();
                } else {
                    Toast.makeText(DetailsDisplayE.this, "UNABLE TO CONNECT...!",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private class FetchdataAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailsDisplayE.this);
            pDialog.setMessage("Loading Event Details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_TITLE, title);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "Retrieve2event.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject object;
                if (success == 1) {
                    object = jsonObject.getJSONObject(KEY_DATA);
                    title = object.getString(KEY_TITLE);
                    description = object.getString(KEY_DESCRIPTION);
                    date = object.getString(KEY_DATE);
                    time = object.getString(KEY_TIME);
                    venue = object.getString(KEY_VENUE);
                    token=object.getString(KEY_TOKEN);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    //Populate the Edit Texts once the network activity is finished executing
                    titlee.setText(title);
                    descriptione.setText(description);
                    datee.setText(date);
                    timee.setText(time);
                    venuee.setText(venue);
                    tokene.setText(token);
                }
            });
        }

    }
    public void Verification(){
        new verifying().execute();
    }

    private class verifying extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailsDisplayE.this);
            pDialog.setMessage("Uploading Details, Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_TITLE, title);
            httpParams.put(KEY_DESCRIPTION, description);
            httpParams.put(KEY_DATE, date);
            httpParams.put(KEY_TIME, time);
            httpParams.put(KEY_VENUE, venue);
            httpParams.put(KEY_TOKEN,token);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "confirmed_details_event.php", "POST", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        //Display success message
                        Toast.makeText(DetailsDisplayE.this,
                                "Verified!", Toast.LENGTH_LONG).show();
                        Intent i=new Intent(DetailsDisplayE.this,SendPushNotificationevent.class);
                       i.putExtra("idpass",title);
                        startActivity(i);

                    } else {
                        Toast.makeText(DetailsDisplayE.this,
                                "Failed to Upload Details..!",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }

    }
    public void Deletion(){
        Intent intent=new Intent(DetailsDisplayE.this,sendpushondismissevent.class);
        intent.putExtra("idpass",title);
        startActivity(intent);
        finish();
    }

}


