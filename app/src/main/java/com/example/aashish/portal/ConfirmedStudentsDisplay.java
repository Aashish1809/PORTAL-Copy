package com.example.aashish.portal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import com.example.aashish.portal.helper.*;
import com.squareup.picasso.Picasso;

public class ConfirmedStudentsDisplay extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_PIN = "pin";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_STARTYEAR = "startyear";
    private static final String KEY_ENDYEAR = "endyear";
    private static final String KEY_IMAGE = "image";
    private TextView pind,abc;
    private TextView named;
    private TextView emaild;
    private TextView startyeard;
    private TextView endyeard;
    private  String name;
    private String pin;
    private String email;
    private String startyear;
    private String endyear;
    String  b;
    public ImageView iv;
    private ProgressDialog pDialog;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed_students_display);
        abc=findViewById(R.id.abc);
        iv=findViewById(R.id.iv);
        pind = findViewById(R.id.pind);
        named = findViewById(R.id.named);
        emaild = findViewById(R.id.emaild);
        startyeard = findViewById(R.id.startyeard);
        endyeard = findViewById(R.id.endyeard);
        Intent intent = getIntent();
        pin = intent.getStringExtra(KEY_PIN);
        new FetchdataAsyncTask().execute();
        new FetchStudentDisplayAsyncTask().execute();

    }
    class FetchdataAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ConfirmedStudentsDisplay.this);
            dialog.setMessage("Loading Details. Please wait...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, pin);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "final.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject object;
                if (success == 1) {
                    object = jsonObject.getJSONObject(KEY_DATA);
                    b = object.getString(KEY_IMAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            dialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    abc.setText(b);
                    //UrlImageViewHelper.setUrlDrawable(display, b);
                    Picasso.with(getApplicationContext()).load(b).into(iv);
                }
            });
        }

    }
    private class FetchStudentDisplayAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(ConfirmedStudentsDisplay.this);
            pDialog.setMessage("Loading Details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, pin);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "RetrieveConfirmedStudentDetails.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject movie;
                if (success == 1) {
                    movie = jsonObject.getJSONObject(KEY_DATA);
                    name = movie.getString(KEY_NAME);
                    pin = movie.getString(KEY_PIN);
                    email = movie.getString(KEY_EMAIL);
                    startyear = movie.getString(KEY_STARTYEAR);
                    endyear = movie.getString(KEY_ENDYEAR);

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
                    pind.setText(pin);
                    named.setText(name);
                    emaild.setText(email);
                    startyeard.setText(startyear);
                    endyeard.setText(endyear);

                }
            });
        }


    }
    public void show(View view){


        Intent i = new Intent(ConfirmedStudentsDisplay.this,ImageFullDisplay.class);
        startActivity(i);


    }
}



