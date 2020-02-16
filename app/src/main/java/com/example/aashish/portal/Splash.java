package com.example.aashish.portal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import com.example.aashish.portal.helper.CheckNetworkStatus;
import com.example.aashish.portal.helper.HttpJsonParser;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import static java.lang.System.exit;


public class Splash extends Activity {
    private static int SPLASH_TIME_OUT = 2000;
    public static final String KEY_SUCCESS = "success";
    public int success,j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    SharedPreferences p = getSharedPreferences("MyPref", MODE_PRIVATE);
                    int i1 = p.getInt("key", 2);

                    if (i1 == 1) {
                        startActivity(new Intent(getApplicationContext(), AdminLogged.class));
                        finish();
                    } else if (i1 == 3) {
                        startActivity(new Intent(getApplicationContext(), ManagerLogged.class));
                        finish();
                    } else if (i1 == 5) {
                        startActivity(new Intent(getApplicationContext(), StudentLogged.class));
                        finish();
                    } else if (i1 == 4) {
                        startActivity(new Intent(getApplicationContext(), AlumniLogged.class));
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    }
                }
            }, SPLASH_TIME_OUT);
        }
        else
        {
            Toast.makeText(Splash.this,"Check your Network & Try Again..!",Toast.LENGTH_LONG).show();
           finish();
        }
    }
}

