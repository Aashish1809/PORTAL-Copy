package com.example.aashish.portal;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
public class otp3 extends AppCompatActivity {
    public Button b,changebutton,skip;
    public TextInputLayout e, e1;
    public String number,add="+91",finalnumber;
    FirebaseAuth mauth;
    String codesent;
    private static final String KEY_PIN = "pin";
    private static final String KEY_STATUS = "status";
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_PHONE = "phone";
    String pin, status = "verified",num,i;
    int success,con,sy,ey,check;
    ProgressDialog p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp2);
        pin = getIntent().getExtras().getString("pin");
        number=getIntent().getExtras().getString("phone");
        skip=findViewById(R.id.skip);
        e = (TextInputLayout) findViewById(R.id.phone);
        e1 = (TextInputLayout) findViewById(R.id.otpfield);
        b = (Button) findViewById(R.id.otpbutton);
        changebutton = (Button) findViewById(R.id.changenumber);
        finalnumber=add+number;
        e.getEditText().setText(finalnumber);
        mauth = FirebaseAuth.getInstance();
        changebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e.getEditText().setText(add);
                Toast.makeText(getApplicationContext(),"Enter your Number!",Toast.LENGTH_SHORT).show();
                e.requestFocus();
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = e.getEditText().getText().toString();
                    Toast.makeText(getApplicationContext(), "An OTP has been sent to your number!", Toast.LENGTH_SHORT).show();
                    sendVerificationcode(); }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Picture.class);
                i.putExtra("aash",pin);
                startActivity(i);
                finish();
                }
        });

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifycode();
            }

        });
    }
    private void verifycode() {
        i = pin.substring(0, 2);
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
        updatenumber();
        else
            updatenumber2();
        String sr = e1.getEditText().getText().toString();
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

    private void sendVerificationcode() {
         num = e.getEditText().getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                num,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codesent = s;
        }
    };

    void updatestatus() {
        new Update().execute();
    }
    private class Update extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(otp3.this);
            p.setMessage("Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, pin);
            httpParams.put(KEY_STATUS, status);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "updatestatus.php", "POST", httpParams);
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
                        Toast.makeText(otp3.this,
                                "Mobile Number Verified!", Toast.LENGTH_LONG).show();
                        Intent i=new Intent(getApplicationContext(),Picture.class);
                        i.putExtra("aash",pin);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(otp3.this,
                                "Error Occuring while Verifying Mobile Number!",
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
            p = new ProgressDialog(otp3.this);
            p.setMessage("Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, pin);
            httpParams.put(KEY_STATUS, status);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "updatestatus2.php", "POST", httpParams);
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
                        Toast.makeText(otp3.this,
                                "Mobile Number Verified!", Toast.LENGTH_LONG).show();
                        Intent i=new Intent(getApplicationContext(),Picture.class);
                        i.putExtra("aash",pin);
                        startActivity(i);

                    } else {
                        Toast.makeText(otp3.this,
                                "Error Occuring while Verifying Mobile Number!",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
    void updatenumber(){
        new Updatenumber().execute();
    }
    private class Updatenumber extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(otp3.this);
            p.setMessage("Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, pin);
            httpParams.put(KEY_PHONE, num);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "updatenumberindb.php", "POST", httpParams);
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
                    if (success != 1) {
                        Toast.makeText(otp3.this,
                                "Error Occuring while Updating Number",
                                Toast.LENGTH_LONG).show();
                    }
                }

            });
        }
    }
    void updatenumber2(){
        new Updatenumber2().execute();
    }
    private class Updatenumber2 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(otp3.this);
            p.setMessage("Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, pin);
            httpParams.put(KEY_PHONE, num);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "updatenumberindb2.php", "POST", httpParams);
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
                    if (success != 1) {
                        Toast.makeText(otp3.this,
                                "Error Occuring while Updating Number",
                                Toast.LENGTH_LONG).show();
                    }
                }

            });
        }
    }
}

