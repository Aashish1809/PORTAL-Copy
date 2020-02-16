package com.example.aashish.portal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.aashish.portal.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class forgotmaster extends AppCompatActivity {
    String c,a,b;
    Button button;
    TextInputLayout a1,b1;
    int success;
    private static final String KEY_ID = "id";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotmaster);
        c= getIntent().getStringExtra("pass");
        a1=findViewById(R.id.new1);
        b1=findViewById(R.id.new2);
        button=findViewById(R.id.nextb);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=a1.getEditText().getText().toString();
                b=b1.getEditText().getText().toString();
                if(a.length()==0||b.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Fill all the feilds!", Toast.LENGTH_LONG).show();
                }
                else if(!(a.equals(b)))
                {
                    Toast.makeText(getApplicationContext(),"Password Mismatch!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    setpassword();
                }
            }
        });
    }
    public void setpassword()
    {
            new Update().execute();
    }

        private class Update extends AsyncTask<String, String, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                HttpJsonParser httpJsonParser = new HttpJsonParser();
                Map<String, String> httpParams = new HashMap<>();
                httpParams.put(KEY_ID, c);
                httpParams.put(KEY_PASSWORD, a);
                JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                        EndPoints.BASE_URL + "updatepass.php", "POST", httpParams);
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
                            Toast.makeText(forgotmaster.this,
                                    "Password Changed", Toast.LENGTH_LONG).show();
                            Intent i=new Intent(getApplicationContext(),Login.class);
                            startActivity(i);

                        } else {
                            Toast.makeText(forgotmaster.this,
                                    "Error Occurred while Updating Password",
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        }
    }

