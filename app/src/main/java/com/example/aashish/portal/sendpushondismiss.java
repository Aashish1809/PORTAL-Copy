package com.example.aashish.portal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class sendpushondismiss extends AppCompatActivity  {

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
        setContentView(R.layout.activity_sendpushondismiss);
        buttonSendPush = (Button) findViewById(R.id.buttonSendPush);
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextMessage = (EditText) findViewById(R.id.code);
        editTextImage = (EditText) findViewById(R.id.editTextImageUrl);
        Bundle d=getIntent().getExtras();
        pin=d.getString("pinpass");
        editTextTitle.setText("Dismissal Message");
        editTextMessage.setText(pin+", has been Dismissed");
        editTextTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && TextUtils.isEmpty(editTextTitle.getText().toString())){
                    editTextTitle.setText("Dismissal Message");
                } else if (hasFocus && editTextTitle.getText().toString().equals("Dismissal Message")){
                    editTextTitle.setText("");
                }
            }
        });
        editTextMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && TextUtils.isEmpty(editTextMessage.getText().toString())){
                    editTextMessage.setText(pin+", has been Dismissed");
                } else if (hasFocus && editTextMessage.getText().toString().equals(pin+", has been Dismissed")){
                    editTextMessage.setText("");
                }
            }
        });


        buttonSendPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSinglePush();
            }
        });
    }
    private void sendSinglePush() {
        final String title = editTextTitle.getText().toString();
        final String message = editTextMessage.getText().toString();
        final String image = editTextImage.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_SINGLE_PUSH_TO_STUDENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(sendpushondismiss.this, response, Toast.LENGTH_LONG).show();
                        Deletion();
                        Intent i=new Intent(getApplicationContext(),AdminLogged.class);
                        startActivity(i);
                        finish();
                    }


                    void Deletion()
                    {
                        new Delete().execute();
                    }
                         class Delete extends AsyncTask<String,String,String> {

                         @Override
                         public void onPreExecute() {
                         super.onPreExecute();
                          pDialog = new ProgressDialog(sendpushondismiss.this);
                          pDialog.setMessage("Uploading Details, Please wait...");
                          pDialog.setIndeterminate(false);
                          pDialog.setCancelable(false);
                          pDialog.show();
                          }
                        @Override
                        public String doInBackground(String... params) {
                         HttpJsonParser httpJsonParser = new HttpJsonParser();
                         Map<String, String> httpParams = new HashMap<>();
                         httpParams.put(KEY_PIN,pin);
                        JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                              EndPoints.BASE_URL + "Delete_details.php", "POST", httpParams);
                        try {
                            success = jsonObject.getInt(KEY_SUCCESS);
                         } catch (JSONException e) {
                             e.printStackTrace();
                        }
                         catch (RuntimeException e){
                          e.printStackTrace();
                             }
                         return null;
                           }
                          public void onPostExecute(String result) {
                          pDialog.dismiss();
                         runOnUiThread(new Runnable() {
                          public void run() {
                               if (success == 1) {

                        } else {
                            Toast.makeText(sendpushondismiss.this,
                                       "Failed to Dismiss Details..!",
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

                params.put("pin", pin);
                return params;
            }
        };

        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

}
