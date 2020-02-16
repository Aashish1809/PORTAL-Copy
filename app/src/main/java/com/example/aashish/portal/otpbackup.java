package com.example.aashish.portal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;
public class otpbackup extends Activity {

    public Button b,c;
    public TextInputLayout e2,e1;
    public String number, mystr="OTP has been sent to your number",g;
    FirebaseAuth mauth;
    String codesent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpbackup);
        e1=(TextInputLayout)findViewById(R.id.phone);
        e2=(TextInputLayout)findViewById(R.id.otpfield);
        b=findViewById(R.id.otpbutton);
        c=findViewById(R.id.skip);
        mauth = FirebaseAuth.getInstance();
        g=getIntent().getExtras().getString("aash");
        c.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i=new Intent(getApplicationContext(),Picture.class);
        startActivity(i);
    }
});
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number=e1.getEditText().getText().toString();
                if (number.isEmpty())
                {
                    e1.setError("Enter valid number");
                    e1.requestFocus();
                    return;
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "An OTP has been sent to your number!!", Toast.LENGTH_SHORT).show();
                    sendVerificationcode();
                }

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
        String sr=e2.getEditText().getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesent, sr);
        signInWithPhoneAuthCredential(credential);
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "logged in succesfully", Toast.LENGTH_SHORT).show();
                            Intent i= new Intent(otpbackup.this,Picture.class);
                            i.putExtra("pass",g);
                            startActivity(i);

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), "Enter crct otp", Toast.LENGTH_LONG).show();
                                // The verification code entered was invalid
                            }


                        }
                    }

                });
    }
    private void  sendVerificationcode(){
        String num=e1.getEditText().getText().toString();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                num,        // Phone number to verify
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
}
