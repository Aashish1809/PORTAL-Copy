package com.example.aashish.portal;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.aashish.portal.helper.HttpJsonParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.aashish.portal.EndPoints.BASE_URL;
public class otp1 extends AppCompatActivity {
    String b, c, e,i,status="verified11";
    ScrollView g;
    public TextInputLayout e2;
    int success,con,sy,check;
    String codesent;
    FirebaseAuth mauth;
    Button b1;
    ProgressDialog p;
    private static final String KEY_PIN = "pin";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_STATUS = "status";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp1);
        g = findViewById(R.id.constraintLayout);
        e2 = findViewById(R.id.otpenterf);
        b1=findViewById(R.id.check);
        b = getIntent().getStringExtra("pass");
        i = b.substring(0, 2);
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
                fetchnumberstudent();
        else if(check==1)
                 fetchnumberalumni();
        else
            Toast.makeText(getApplicationContext(),"PIN Invalid",Toast.LENGTH_SHORT).show();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifycode();
            }

        });
   }
 void fetchnumberstudent()
 {
    new fetch().execute();
 }
    private class fetch extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(otp1.this);
            p.setMessage("Fetching... Please wait...");
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
                    BASE_URL + "getnumberstudent.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject obj;
                if (success == 1) {
                    obj = jsonObject.getJSONObject(KEY_DATA);
                    c = obj.getString(KEY_PHONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                Toast.makeText(getApplicationContext(), "Unable to connect to Server", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            p.dismiss();
            String f = c.substring(9, 13);
            String m = c.substring(0, 3);
            final Snackbar a2 = Snackbar.make(g, "OTP sent to " + m + "******" + f, 5000);
            a2.show();
            sendVerificationcode();
        }
    }
    void fetchnumberalumni()
    {
        new fetch1().execute();
    }
    private class fetch1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(otp1.this);
            p.setMessage("Fetching... Please wait...");
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
                    BASE_URL + "getnumberalumni.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject obj;
                if (success == 1) {
                    obj = jsonObject.getJSONObject(KEY_DATA);
                    c = obj.getString(KEY_PHONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                Toast.makeText(getApplicationContext(), "Unable to connect to Server", Toast.LENGTH_LONG).show();
            }
            return null;
        }
        protected void onPostExecute(String result) {
            p.dismiss();
            String f = c.substring(9, 13);
            String m = c.substring(0, 3);
            final Snackbar a2 = Snackbar.make(g, "OTP sent to " + m +"******" +f, 5000);
            a2.show();
            sendVerificationcode();
        }
    }
    private void  sendVerificationcode(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                c,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codesent=s;
        }
    };
    private void verifycode() {
        String sr = e2.getEditText().getText().toString();
        if(sr.length()==0)
            Toast.makeText(getApplicationContext(),"Enter OTP!",Toast.LENGTH_SHORT).show();
        else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesent, sr);
            signInWithPhoneAuthCredential(credential);
        }
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(check==2)
                                updatestatus();
                            else
                                updatestatus2();

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), "Enter Correct OTP", Toast.LENGTH_LONG).show();

                            }
                        }
                    }

                });
    }
    void updatestatus() {
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
            httpParams.put(KEY_PIN, b);
            httpParams.put(KEY_STATUS, status);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "updatestatusf.php", "POST", httpParams);
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
                        Toast.makeText(otp1.this,
                                "Mobile Number Verified!", Toast.LENGTH_LONG).show();
                        Intent i=new Intent(getApplicationContext(),forgotpassword.class);
                        i.putExtra("pass",b);
                        startActivity(i);

                    } else {
                        Toast.makeText(otp1.this,
                                "Error Occured!",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    void updatestatus2() {
        new Update2().execute();
    }

    private class Update2 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(otp1.this);
            p.setMessage("Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, b);
            httpParams.put(KEY_STATUS, status);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "updatestatus2f.php", "POST", httpParams);
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
                        Toast.makeText(otp1.this,
                                "Mobile Number Verified!", Toast.LENGTH_LONG).show();
                        Intent i=new Intent(getApplicationContext(),forgotpassworda.class);
                        i.putExtra("pass",b);
                        startActivity(i);

                    } else {
                        Toast.makeText(otp1.this,
                                "Error Occuring while Verifying Mobile Number!",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}

