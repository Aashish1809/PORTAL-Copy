package com.example.aashish.portal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.TokenWatcher;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.example.aashish.portal.helper.*;
import static com.example.aashish.portal.EndPoints.BASE_URL;
public class Login extends AppCompatActivity {
    private static String a, b, i, pin, pass,idp,status;
    private TextInputLayout id, password;
    private Button login, register,forgot;
    private int con, sy;
    private static final String KEY_ID = "id";
    private static final String KEY_DATA = "data";
    private static final String KEY_PIN = "pin";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_STATUS = "status";
    private ProgressDialog p;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        id = findViewById(R.id.pino);
        password = findViewById(R.id.pswin);
        login = findViewById(R.id.nextb);
        register = findViewById(R.id.register);
        forgot=findViewById(R.id.forgot);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
                pref.getString("change","LOGIN");
                SharedPreferences.Editor editor = pref.edit();
                String d=password.getEditText().getText().toString();
                editor.putString("passchange",d);
                editor.apply();
                String c=id.getEditText().getText().toString();
                editor.putString("change",c);
                editor.apply();
                a = id.getEditText().getText().toString();
                b = password.getEditText().getText().toString();
                if (a.length() == 0 || b.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Enter Proper Login Details..!", Toast.LENGTH_SHORT).show();
                } else if((a.matches(("([0-9]{5})-([A-Z]{2})-([0-9]{3})")))||(a.matches(("([0-9]{5})-([A-Z]{1})-([0-9]{3})")))) {
                    i = a.substring(0, 2);
                    con = Integer.parseInt(i);
                    sy = 2000 + con;
                    Date date = new Date();
                    SimpleDateFormat f = new SimpleDateFormat("yyyy");
                    String prey = f.format(date);
                    int preyy = Integer.parseInt(prey);
                    SimpleDateFormat f1 = new SimpleDateFormat("MM");
                    String prem = f1.format(date);
                    int premm = Integer.parseInt(prem);
                    int d1 = preyy - 3;
                    int check;
                    if ((premm >= 6) && (premm <= 12)) {
                        if (sy <= d1)
                            check = 1;
                        else if ((sy <= preyy) && (sy > d1))
                            check = 2;
                        else
                            check = 3;
                    } else {
                        if (sy < d1)
                            check = 1;
                        else if ((sy < preyy) && (sy >= d1))
                            check = 2;
                        else
                            check = 3;
                    }
                    if (check==2) {
                        if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                            new Fetching().execute();
                        } else {
                            Toast.makeText(Login.this, "UNABLE TO CONNECT...!",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                    else if(check==3)
                    {
                        Toast.makeText(Login.this, "PIN Invalid!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                            new Fetching1().execute();
                        } else {
                            Toast.makeText(Login.this, "UNABLE TO CONNECT...!",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                else if(a.length() != 0 || b.length() != 0)
                {
                    if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                        new Fetching2().execute();
                    } else {
                        Toast.makeText(Login.this, "UNABLE TO CONNECT...!",
                                Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter Proper Login Details...",Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);


            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            a = id.getEditText().getText().toString();
            if (a.length() == 0)
                Toast.makeText(getApplicationContext(), "Please enter your Login ID!", Toast.LENGTH_LONG).show();
            else {
                if ((a.equals("admin")) || (a.equals("manager"))) {
                    Intent i = new Intent(getApplicationContext(), securitycode.class);
                    i.putExtra("pass", a);
                    startActivity(i);
                } else if ((a.matches(("([0-9]{5})-([A-Z]{2})-([0-9]{3})"))) || (a.matches(("([0-9]{5})-([A-Z]{1})-([0-9]{3})")))) {
                    i = a.substring(0, 2);
                    con = Integer.parseInt(i);
                    sy = 2000 + con;
                    Date date = new Date();
                    SimpleDateFormat f = new SimpleDateFormat("yyyy");
                    String prey = f.format(date);
                    int preyy = Integer.parseInt(prey);
                    SimpleDateFormat f1 = new SimpleDateFormat("MM");
                    String prem = f1.format(date);
                    int premm = Integer.parseInt(prem);
                    int d = preyy - 3;
                    int check;
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
                    if (check==2) {
                        new getpins().execute();
                    } else if(check==1) {
                        new getpina().execute();
                    } else {
                        Toast.makeText(getApplicationContext(),"PIN Invalid!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Login ID Improper..!", Toast.LENGTH_LONG).show();

                }
            }
        }
});
    }

    private class Fetching2 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(Login.this);
            p.setMessage("Logging In. Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_ID, a);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "Fetching2.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject movie;
                if (success == 1) {
                    movie = jsonObject.getJSONObject(KEY_DATA);
                    idp = movie.getString(KEY_ID);
                    pass = movie.getString(KEY_PASSWORD);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            p.dismiss();
            if (a.equals(idp)) {
                if (b.equals(pass)) {
                    if (a.equals("admin")) {
                        Intent intent = new Intent(getApplicationContext(), AdminLogged.class);
                        startActivity(intent);
                        finish();


                    } else {
                       Intent intent = new Intent(getApplicationContext(), ManagerLogged.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Password Incorrect..!", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Login ID Incorrect..!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class Fetching1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(Login.this);
            p.setMessage("Logging In. Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, a);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "Fetching1.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject movie;
                if (success == 1) {
                    movie = jsonObject.getJSONObject(KEY_DATA);
                    pin=movie.getString(KEY_PIN);
                    pass = movie.getString(KEY_PASSWORD);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            p.dismiss();
            if (a.equals(pin)) {
                if (b.equals(pass)) {
                    Intent intent = new Intent(getApplicationContext(), AlumniLogged.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   startActivity(intent);
                   finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Password Entered", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Wrong Login ID Entered", Toast.LENGTH_SHORT).show();

            }

        }
    }
    private class Fetching extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(Login.this);
            p.setMessage("Logging In. Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, a);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "Fetching.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject obj;
                if (success == 1) {
                    obj = jsonObject.getJSONObject(KEY_DATA);
                    pin= obj.getString(KEY_PIN);
                    pass = obj.getString(KEY_PASSWORD);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch(RuntimeException e)
            {
                Toast.makeText(getApplicationContext(),"Unable to connect Wampserver",Toast.LENGTH_LONG).show();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            p.dismiss();
            if (a.equals(pin)) {
                if (b.equals(pass)) {
                    Intent intent = new Intent(getApplicationContext(), StudentLogged.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Password Incorrect..!", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Login ID Incorrect..!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class getpins extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(Login.this);
            p.setMessage("Logging In. Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, a);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "getpins.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject obj;
                if (success == 1) {
                    obj = jsonObject.getJSONObject(KEY_DATA);
                    pin= obj.getString(KEY_PIN);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch(RuntimeException e)
            {
                Toast.makeText(getApplicationContext(),"Unable to connect Wampserver",Toast.LENGTH_LONG).show();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            p.dismiss();
            if (a.equals(pin)) {
                fetchstatusstudent();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Login ID not Registered..!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class getpina extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(Login.this);
            p.setMessage("Logging In. Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, a);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "getpina.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject obj;
                if (success == 1) {
                    obj = jsonObject.getJSONObject(KEY_DATA);
                    pin= obj.getString(KEY_PIN);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch(RuntimeException e)
            {
                Toast.makeText(getApplicationContext(),"Unable to connect Wampserver",Toast.LENGTH_LONG).show();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            p.dismiss();
            if (a.equals(pin)) {
                    fetchstatusalumni();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Login ID not Registered..!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void fetchstatusstudent()
    {
        new fetchstatus().execute();
    }
    private class fetchstatus extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(Login.this);
            p.setMessage("Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, a);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "fetchstatusstudent.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject movie;
                if (success == 1) {
                    movie = jsonObject.getJSONObject(KEY_DATA);
                    status = movie.getString(KEY_STATUS);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            p.dismiss();
            if (status.equals("verified")) {
                Intent intent = new Intent(getApplicationContext(), otp1.class);
                intent.putExtra("pass", a);
                startActivity(intent);
                //finish();
            } else {
                Toast.makeText(getApplicationContext(),"Number not Verified!",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void fetchstatusalumni()
    {
        new fetchstatus1().execute();
    }
    private class fetchstatus1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(Login.this);
            p.setMessage("Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, a);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "fetchstatusalumni.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject movie;
                if (success == 1) {
                    movie = jsonObject.getJSONObject(KEY_DATA);
                    status = movie.getString(KEY_STATUS);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String result) {
            p.dismiss();
            if (status.equals("verified")) {
                Intent intent = new Intent(getApplicationContext(), otp1.class);
                intent.putExtra("pass", a);
                startActivity(intent);
                //finish();
            } else {
                Toast.makeText(getApplicationContext(),"Number not Verified!",Toast.LENGTH_SHORT).show();
            }
        }
    }

}