package com.example.aashish.portal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class balumni extends AppCompatActivity {
    private Button buttonSendPush;
    private ProgressDialog pDialog;
    private String pin;
    private int success;
    private static final String KEY_PIN = "pin";
    private static final String KEY_SUCCESS = "success";
    private EditText editTextTitle, editTextMessage, editTextImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balumni);
        buttonSendPush = (Button) findViewById(R.id.buttonSendPush);
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextMessage = (EditText) findViewById(R.id.code);
        editTextImage = (EditText) findViewById(R.id.editTextImageUrl);
        buttonSendPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMultiplePush();
            }
        });
    }
    private void sendMultiplePush() {
        final String title = editTextTitle.getText().toString();
        final String message = editTextMessage.getText().toString();
        final String image = editTextImage.getText().toString();
        if (title.length() == 0 || message.length() == 0)
            Toast.makeText(getApplicationContext(), "Fill out the Mandatory Feilds", Toast.LENGTH_LONG).show();
        else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_MULTIPLE_TO_ALUMNI,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(balumni.this, response, Toast.LENGTH_LONG).show();
                            Intent i=new Intent(getApplicationContext(),AdminLogged.class);
                            startActivity(i);
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("title", title);
                    params.put("message", message);

                    if (!TextUtils.isEmpty(image))
                        params.put("image", image);
                    return params;
                }
            };

            MyVolley.getInstance(this).addToRequestQueue(stringRequest);
        }

    }
}
