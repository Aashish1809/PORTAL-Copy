package com.example.aashish.portal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aashish.portal.helper.*;
public class Register extends AppCompatActivity {
    private static String a,b,c,d,e,f,g,h,i,j,k;
    private int sy,ey,con,success;
    private TextInputLayout fulna, pi, fnam, mob, ema, db, ps, cps;
    private Button button1,button2;
    private static final String KEY_SUCCESS = "success";
    private ProgressDialog p;
    private static final String KEY_NAME = "name";
    private static final String KEY_FATHERNAME = "fathername";
    private static final String KEY_PIN = "pin";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DATE = "date";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_STARTYEAR = "startyear";
    private static final String KEY_ENDYEAR = "endyear";
    private static final String KEY_STATUS = "status";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fulna=(TextInputLayout)findViewById(R.id.fullin);
        fnam=(TextInputLayout)findViewById(R.id.fnamein);
        pi =(TextInputLayout)findViewById(R.id.pinin);
        mob=(TextInputLayout)findViewById(R.id.phnin);
        ema=(TextInputLayout)findViewById(R.id.emailin);
        db=(TextInputLayout)findViewById(R.id.dobin);
        ps=(TextInputLayout)findViewById(R.id.pswin);
        cps=(TextInputLayout)findViewById(R.id.cpswin);
        button1 = findViewById(R.id.nextb);
        button2=findViewById(R.id.plz);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: alumniportalmanagementgpt@gmail.com"));
                startActivity(Intent.createChooser(emailIntent, "Report a problem"));
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a = fulna.getEditText().getText().toString();
                b = pi.getEditText().getText().toString();
                c = fnam.getEditText().getText().toString();
                d = mob.getEditText().getText().toString();
                e = ema.getEditText().getText().toString();
                f = db.getEditText().getText().toString();
                g = ps.getEditText().getText().toString();
                h = cps.getEditText().getText().toString();
                if (a.length() == 0 || b.length() == 0 || c.length() == 0 || d.length() == 0 || e.length() == 0 || f.length() == 0 || g.length() == 0 || h.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Fill Out the Form Completely...!", Toast.LENGTH_LONG).show();
                } else if (!(g.equals(h))) {
                    Toast.makeText(getApplicationContext(), "Password Mismatch..!", Toast.LENGTH_LONG).show();
                } else if (d.length() != 10) {
                    Toast.makeText(getApplicationContext(), "Enter a Valid Phone Number..!", Toast.LENGTH_LONG).show();
                } else if (!((b.matches(("([0-9]{5})-([A-Z]{2})-([0-9]{3})"))) || (b.matches(("([0-9]{5})-([A-Z]{1})-([0-9]{3})"))))) {
                    Toast.makeText(getApplicationContext(), "Enter your PIN in Proper Format ", Toast.LENGTH_LONG).show();
                } else if (g.length() > 10) {
                    Toast.makeText(getApplicationContext(), "Password Too Long...", Toast.LENGTH_LONG).show();
                } else {
                    i = b.substring(0, 2);
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
                    j = Integer.toString(sy);
                    k = Integer.toString(ey);

                    if (check == 2) {
                        if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                            check();
                        } else {
                            Toast.makeText(Register.this, "UNABLE TO CONNECT...!",
                                    Toast.LENGTH_LONG).show();

                        }
                    } else if (check == 1) {
                        if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                            check1();
                        } else {
                            Toast.makeText(Register.this, "UNABLE TO CONNECT...!",
                                    Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(Register.this, "INVALID PIN..!",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

    }
    public void sendTokenToServer() {
        final String token = SharedPrefManager.getInstance(this).getDeviceToken();
        if (token == null) {
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
        }
        new Update().execute();
    }

        class Update extends AsyncTask<String, String, String> {
            final String token = SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                HttpJsonParser httpJsonParser = new HttpJsonParser();
                Map<String, String> httpParams = new HashMap<>();
                httpParams.put(KEY_PIN, b);
                httpParams.put(KEY_TOKEN, token);
                JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                        EndPoints.BASE_URL+"tokenstudent.php", "POST", httpParams);
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
                        if (success != 1) {
                            Toast.makeText(getApplicationContext(),"Error Occured!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }
    public void sendTokenToServer1() {
        final String token = SharedPrefManager.getInstance(this).getDeviceToken();
        if (token == null) {
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
        }
        new Update1().execute();
    }

    class Update1 extends AsyncTask<String, String, String> {
        final String token = SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, b);
            httpParams.put(KEY_TOKEN, token);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL+"tokenalumni.php", "POST", httpParams);
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
                    if (success != 1) {
                        Toast.makeText(getApplicationContext(),"Error Occured!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    public void check()
    {
        new checking().execute();
    }
    private class checking extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(Register.this);
            p.setMessage("Connecting to Server, Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, b);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL  +"acccheck.php","GET", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch(RuntimeException e)
            {
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 3) {
                        p.dismiss();
                        Toast.makeText(Register.this,
                                "PIN already associated to an USER!", Toast.LENGTH_LONG).show();
                        button2.setVisibility(View.VISIBLE);
                    }
                    else
                           p.dismiss();
                    {      addinto();
                    }
                }
            });
        }
    }

    public void addinto()
    {
        new adding().execute();

    }
    private class adding extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(Register.this);
            p.setMessage("Uploading Details, Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_NAME, a);
            httpParams.put(KEY_FATHERNAME, c);
            httpParams.put(KEY_PIN, b);
            httpParams.put(KEY_PHONE, d);
            httpParams.put(KEY_EMAIL, e);
            httpParams.put(KEY_DATE, f);
            httpParams.put(KEY_PASSWORD, g);
            httpParams.put(KEY_STARTYEAR, j);
            httpParams.put(KEY_ENDYEAR, k);
            httpParams.put(KEY_STATUS, "nverified");
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL  +"adding1.php","POST", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch(RuntimeException e)
            {
                e.printStackTrace();
            }


            return null;
        }
        protected void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        p.dismiss();
                        sendTokenToServer();
                        Toast.makeText(Register.this,
                                "Details Uploaded", Toast.LENGTH_LONG).show();
                        Intent i=new Intent(Register.this,otp3.class);
                        i.putExtra("pin",b);
                        i.putExtra("phone",d);
                        startActivity(i);
                        finish();
                    }
                    else if(success==3)
                    {
                        p.dismiss();
                        Toast.makeText(Register.this, "Account under Verification!", Toast.LENGTH_LONG).show();
                        button2.setVisibility(View.VISIBLE);

                    }

                    else
                    {
                        p.dismiss();
                        Toast.makeText(Register.this, "Failed to Upload Details..!",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
    public void check1()
    {
        new checking1().execute();
    }
    private class checking1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(Register.this);
            p.setMessage("Connecting to Server, Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, b);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL  +"acccheckal.php","GET", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch(RuntimeException e)
            {
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 3) {
                        p.dismiss();
                        Toast.makeText(Register.this,
                                "PIN already associated to an USER!", Toast.LENGTH_LONG).show();
                        button2.setVisibility(View.VISIBLE);
                    }
                    else
                        p.dismiss();
                    {      addinto1();
                    }
                }
            });
        }
    }
    public void addinto1()
    {
        new adding1().execute();

    }
    private class adding1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(Register.this);
            p.setMessage("Uploading Details, Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_NAME, a);
            httpParams.put(KEY_FATHERNAME, c);
            httpParams.put(KEY_PIN, b);
            httpParams.put(KEY_PHONE, d);
            httpParams.put(KEY_EMAIL, e);
            httpParams.put(KEY_DATE, f);
            httpParams.put(KEY_PASSWORD, g);
            httpParams.put(KEY_STARTYEAR, j);
            httpParams.put(KEY_ENDYEAR, k);
            httpParams.put(KEY_STATUS, "nverified");
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL  +"adding.php","POST", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch(RuntimeException e)
            {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        p.dismiss();
                        sendTokenToServer1();
                        Toast.makeText(Register.this,
                                "Details Uploaded", Toast.LENGTH_LONG).show();
                        Intent i=new Intent(Register.this,otp3.class);
                        i.putExtra("pin",b);
                        i.putExtra("phone",d);
                        startActivity(i);
                        finish();
                    }
                    else if(success==3)
                    {
                        p.dismiss();
                        Toast.makeText(Register.this,
                                "Account under Verification!", Toast.LENGTH_LONG).show();
                        button2.setVisibility(View.VISIBLE);

                    }
                    else
                    {
                        p.dismiss();
                        Toast.makeText(Register.this,
                                "Failed to Upload Details..!",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
}


