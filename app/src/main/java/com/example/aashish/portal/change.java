package com.example.aashish.portal;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import com.example.aashish.portal.helper.*;

public class change extends Activity {
    public TextInputLayout pre,newp;
    public Button b1;
    public static String a,b,c,d;
    private ProgressDialog p;
    private static final String KEY_ID = "id";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    int success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        c= getIntent().getExtras().getString("check");
        d= getIntent().getExtras().getString("check2");
        pre=findViewById(R.id.current);
        newp=findViewById(R.id.newp);
        b1=findViewById(R.id.nextb);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=pre.getEditText().getText().toString();
                b=newp.getEditText().getText().toString();
                if(a.length()==0||b.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Fill Out the Fields First...!",Toast.LENGTH_LONG).show();
                }
                else if(a.equals(b))
                {
                    Toast.makeText(getApplicationContext(),"Both Can't be Same..!",Toast.LENGTH_LONG).show();
                }
                else if(!(a.equals(d)))
                {
                    Toast.makeText(getApplicationContext(),"Current Password Incorrect",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                        update();
                    } else {
                        Toast.makeText(change.this, "UNABLE TO CONNECT...!",
                                Toast.LENGTH_LONG).show();

                    }

                }

            }
        });

    }
    public void update()
    {
        new Update().execute();
    }

    private class Update extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(change.this);
            p.setMessage("Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_ID, c);
            httpParams.put(KEY_PASSWORD, b);
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
            p.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        Toast.makeText(change.this,
                                "Please Re-Login using New Password..", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(),Login.class);
                       startActivity(i);

                    } else {
                        Toast.makeText(change.this,
                                "Error Occuring while Updating Password",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
}
