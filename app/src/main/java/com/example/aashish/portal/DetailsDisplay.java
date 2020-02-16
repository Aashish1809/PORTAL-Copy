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

public class DetailsDisplay extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_PIN = "pin";
    private static final String KEY_NAME = "name";
    private static final String KEY_FATHERNAME = "fathername";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_STARTYEAR = "startyear";
    private static final String KEY_DATE = "date";
    private static final String KEY_ENDYEAR = "endyear";
    private static final String KEY_PASSWORD="password";
    private static final String KEY_IMAGE="image";
    private static final String KEY_TOKEN="token";
    private static final String KEY_STATUS="status";
    private TextView pind,imagehtd;
    private TextView named;
    private TextView fathernamed;
    private TextView passwordd;
    private TextView phoned;
    private TextView emaild;
    private TextView dated,tokend,statusd;
    private TextView startyeard;
    private TextView endyeard;
    private  String name,imght;
    private String pin,token,status;
    private String phone;
    private String password;
    private String email;
    private String startyear;
    private String endyear;
    private String date;
    private String fathername;
    private int success;
    private ProgressDialog pDialog;
    private Button but_ver,but_dis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_display);
        imagehtd=findViewById(R.id.imagehttp);
        pind = findViewById(R.id.pind);
        named = findViewById(R.id.named);
        fathernamed = findViewById(R.id.fathernamed);
        phoned = findViewById(R.id.phoned);
        emaild = findViewById(R.id.emaild);
        dated = findViewById(R.id.dated);
        startyeard = findViewById(R.id.startyeard);
        endyeard = findViewById(R.id.endyeard);
        passwordd =findViewById(R.id.passwordd);
        tokend=findViewById(R.id.token);
        statusd=findViewById(R.id.status);
        Intent intent =getIntent();
        pin = intent.getStringExtra(KEY_PIN);
        but_ver=findViewById(R.id.verify);
        but_dis=findViewById(R.id.dismiss);
        new FetchdataAsyncTask().execute();
        but_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    Verification();
                } else {
                    Toast.makeText(DetailsDisplay.this, "UNABLE TO CONNECT...!",
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
                    Toast.makeText(DetailsDisplay.this, "UNABLE TO CONNECT...!",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private class FetchdataAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailsDisplay.this);
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
                    EndPoints.BASE_URL + "Retrieve2.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject object;
                if (success == 1) {
                    object = jsonObject.getJSONObject(KEY_DATA);
                    name = object.getString(KEY_NAME);
                    pin = object.getString(KEY_PIN);
                    fathername = object.getString(KEY_FATHERNAME);
                    date =object.getString(KEY_DATE);
                    phone = object.getString(KEY_PHONE);
                    email = object.getString(KEY_EMAIL);
                    startyear = object.getString(KEY_STARTYEAR);
                    endyear = object.getString(KEY_ENDYEAR);
                    password=object.getString(KEY_PASSWORD);
                    imght=object.getString(KEY_IMAGE);
                    token=object.getString(KEY_TOKEN);
                    status=object.getString(KEY_STATUS);
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
                    phoned.setText(phone);
                    emaild.setText(email);
                    passwordd.setText(password);
                    fathernamed.setText(fathername);
                    startyeard.setText(startyear);
                    endyeard.setText(endyear);
                    dated.setText(date);
                    imagehtd.setText(imght);
                    tokend.setText(token);
                    statusd.setText(status);
                }
            });
        }

    }


    public void Verification(){

        new verifying().execute();
    }

    public class verifying extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailsDisplay.this);
            pDialog.setMessage("Uploading Details, Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_NAME, name);
            httpParams.put(KEY_FATHERNAME, fathername);
            httpParams.put(KEY_PIN, pin);
            httpParams.put(KEY_PHONE, phone);
            httpParams.put(KEY_EMAIL, email);
            httpParams.put(KEY_DATE, date);
            httpParams.put(KEY_PASSWORD, password);
            httpParams.put(KEY_STARTYEAR, startyear);
            httpParams.put(KEY_ENDYEAR, endyear);
            httpParams.put(KEY_IMAGE, imght);
            httpParams.put(KEY_TOKEN,token);
            httpParams.put(KEY_STATUS,status);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "confirmed_details.php", "POST", httpParams);
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
                        Toast.makeText(DetailsDisplay.this,
                                "Verified!", Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(),SendPushNotification.class);
                        intent.putExtra("pinpass",pin);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(DetailsDisplay.this,
                                "Failed to Upload Details..!",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }

    }
    public void Deletion(){
        Intent intent=new Intent(DetailsDisplay.this,sendpushondismiss.class);
        intent.putExtra("pinpass",pin);
        startActivity(intent);
        finish();
    }
}





