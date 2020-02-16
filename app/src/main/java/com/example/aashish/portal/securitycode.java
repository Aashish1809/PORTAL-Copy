package com.example.aashish.portal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aashish.portal.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class securitycode extends AppCompatActivity {
    private String a,b,resetcode;
    public EditText code1;
    public Button b1;
    private static final String KEY_DATA = "data";
    private static final String KEY_ID = "id";
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_RESETCODE = "resetcode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_securitycode);
        b1=findViewById(R.id.submit);
        code1=findViewById(R.id.code);
        b= getIntent().getStringExtra("pass");
        new FetchdataAsyncTask().execute();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=code1.getText().toString();
                if(a.length()==0)
                    Toast.makeText(getApplicationContext(),"Please enter SRC",Toast.LENGTH_LONG).show();
                else
                {
                    check();
                }
            }
        });
    }
         private  class FetchdataAsyncTask extends AsyncTask<String, String, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                HttpJsonParser httpJsonParser = new HttpJsonParser();
                Map<String, String> httpParams = new HashMap<>();
                httpParams.put(KEY_ID, b);
                JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                        EndPoints.BASE_URL + "forgotmaster.php", "GET", httpParams);
                try {
                    int success = jsonObject.getInt(KEY_SUCCESS);
                    JSONObject object;
                    if (success == 1) {
                        object = jsonObject.getJSONObject(KEY_DATA);
                        resetcode = object.getString(KEY_RESETCODE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String result) {
                runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });
            }
        }
        void check() {
            if (a.equals(resetcode)) {
                Intent i = new Intent(getApplicationContext(), forgotmaster.class);
                i.putExtra("pass", b);
                startActivity(i);
        }
            else
            {
                Toast.makeText(getApplicationContext(),"Wrong SRC", Toast.LENGTH_LONG).show();
            }
        }
    }

