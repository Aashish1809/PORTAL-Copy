package com.example.aashish.portal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.example.aashish.portal.helper.HttpJsonParser;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Waiting extends Activity {
    ProgressDialog dialog;
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_PIN = "pin";
    private static final String KEY_IMAGE = "image";
    private  String a,b,i;
    private int success,con,sy,ey,check;
    private String m="hello";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        a= getIntent().getExtras().getString("pass");
        i = a.substring(0, 2);
        con = Integer.parseInt(i);
        sy = 2000 + con;
        ey = 3 + sy;
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy");
        String prey = f.format(date);
        int preyy = Integer.parseInt(prey);
        SimpleDateFormat f1 = new SimpleDateFormat("MM");
        String prem = f1.format(date);
        int premm = Integer.parseInt(prem);
        int d = preyy - 3;
        if ((premm >= 6) && (premm <= 12)) {
            if (sy <= d)
                check = 1;
            else if ((sy <= preyy) && (sy > d))
                check = 2;
            else
                check = 3;
        } else {
            if (sy < d)
                check = 1;
            else if ((sy < preyy) && (sy >= d))
                check = 2;
            else
                check = 3;
        }
        if(check==2)
        new FetchdataAsyncTask().execute();
        else
        new FetchdataAsyncTask2().execute();

    }
    class FetchdataAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Waiting.this);
            dialog.setMessage("Loading Details. Please wait...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, a);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "final2.php", "GET", httpParams);
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
                    if(b.length()==0)
                    {
                        imageupdate();
                    }
                }
            });
        }
    }
    class FetchdataAsyncTask2 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Waiting.this);
            dialog.setMessage("Loading Details. Please wait...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, a);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "final2alumni.php", "GET", httpParams);
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
                    if(b.length()==0)
                    {
                        imageupdate2();
                    }
                }
            });
        }
    }
    void imageupdate()
    {
        new Update().execute();
    }
    class Update extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, a);
            httpParams.put(KEY_IMAGE, m);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL+ "imageset.php", "POST", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Not Done",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    void imageupdate2()
    {
        new Update2().execute();
    }
    class Update2 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, a);
            httpParams.put(KEY_IMAGE, m);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL+ "imagesetalumni.php", "POST", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Not Done",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}
