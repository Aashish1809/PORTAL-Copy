package com.example.aashish.portal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.aashish.portal.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendPushNotificationevent extends AppCompatActivity {
    private Button buttonSendPush;
    private static final String KEY_TITLE = "title";
    private ProgressDialog pDialog;
    private String pin;
    private int success;
    private static final String KEY_SUCCESS = "success";
    public String title2;
    private EditText editTextTitle, editTextMessage, editTextImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_push_notificationevent);
        buttonSendPush = (Button) findViewById(R.id.buttonSendPush);
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextMessage = (EditText) findViewById(R.id.code);
        editTextImage = (EditText) findViewById(R.id.editTextImageUrl);
        title2= getIntent().getStringExtra("idpass");
        editTextTitle.setText("Notification");
        editTextMessage.setText("An Event placed into the portal");
        editTextTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && TextUtils.isEmpty(editTextTitle.getText().toString())) {
                    editTextTitle.setText("Notification");
                } else if (hasFocus && editTextTitle.getText().toString().equals("Notification")) {
                    editTextTitle.setText("");
                }
            }
        });
        editTextMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && TextUtils.isEmpty(editTextMessage.getText().toString())) {
                    editTextMessage.setText("An Event placed into the portal");
                } else if (hasFocus && editTextMessage.getText().toString().equals("An Event placed into the portal")) {
                    editTextMessage.setText("");
                }
            }
        });

        buttonSendPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPush();
                sendPush2();
            }
        });
    }

    void sendPush() {
                final String title = editTextTitle.getText().toString();
                final String message = editTextMessage.getText().toString();
                final String image = editTextImage.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_MULTIPLE_TO_STUDENT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(SendPushNotificationevent.this, response, Toast.LENGTH_LONG).show();
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
    void sendPush2() {
        final String title = editTextTitle.getText().toString();
        final String message = editTextMessage.getText().toString();
        final String image = editTextImage.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_MULTIPLE_TO_ALUMNI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SendPushNotificationevent.this, response, Toast.LENGTH_LONG).show();
                        Deletion();
                        Intent i = new Intent(getApplicationContext(), AdminLogged.class);
                        startActivity(i);
                        finish();
                    }
                    void Deletion() {
                        new Delete().execute();
                    }

                    class Delete extends AsyncTask<String, String, String> {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            pDialog = new ProgressDialog(SendPushNotificationevent.this);
                            pDialog.setMessage("Uploading Details, Please wait...");
                            pDialog.setIndeterminate(false);
                            pDialog.setCancelable(false);
                            pDialog.show();
                        }

                        @Override
                        protected String doInBackground(String... params) {
                            HttpJsonParser httpJsonParser = new HttpJsonParser();
                            Map<String, String> httpParams = new HashMap<>();
                            httpParams.put(KEY_TITLE, title2);
                            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                                    EndPoints.BASE_URL + "Delete_details_event.php", "POST", httpParams);
                            try {
                                success = jsonObject.getInt(KEY_SUCCESS);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (RuntimeException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        protected void onPostExecute(String result) {
                            pDialog.dismiss();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (success == 1) {

                                        Toast.makeText(SendPushNotificationevent.this,
                                                "Deleted", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(SendPushNotificationevent.this,
                                                "Failed to Upload Details..!",
                                                Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                        }

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


