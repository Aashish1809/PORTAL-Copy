package com.example.aashish.portal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.io.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.example.aashish.portal.helper.*;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.squareup.picasso.Picasso;

import static com.example.aashish.portal.Picture.KEY_ID;
import static com.example.aashish.portal.Picture.UPLOAD_KEY;

public class profilepic extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_PIN = "pin";
    private static final String KEY_IMAGE = "image";
    private TextView non;
    private Context c;
    private ImageView display;
    Bundle bundle;
    String a, b;
    private int PICK_IMAGE_REQUEST = 1,check;
    ProgressDialog dialog;
    Button update;
    public Bitmap bitmap;
    public Uri filePath;

    public ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepic);
        update = findViewById(R.id.update);
        non = findViewById(R.id.picpath);
        display = findViewById(R.id.picview);
        bundle = getIntent().getExtras();
        a = bundle.getString("pass");
        String i = a.substring(0, 2);
        int con = Integer.parseInt(i);
        int sy = 2000 + con;
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
        new FetchdataAsyncTask().execute();
        else
        new FetchdataAsyncTask2().execute();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }

    class FetchdataAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(profilepic.this);
            dialog.setMessage("Loading Details. Please wait...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, a);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "final.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject object;
                if (success == 1) {
                    object = jsonObject.getJSONObject(KEY_DATA);
                    b = object.getString(KEY_IMAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            dialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    non.setText(b);
                    //UrlImageViewHelper.setUrlDrawable(display, b);
                    Picasso.with(getApplicationContext()).load(b).into(display);
                }
            });
        }

    }
    class FetchdataAsyncTask2 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(profilepic.this);
            dialog.setMessage("Loading Details. Please wait...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PIN, a);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "finalalumni.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject object;
                if (success == 1) {
                    object = jsonObject.getJSONObject(KEY_DATA);
                    b = object.getString(KEY_IMAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            dialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    non.setText(b);
                    //UrlImageViewHelper.setUrlDrawable(display, b);
                    Picasso.with(getApplicationContext()).load(b).into(display);
                }
            });
        }

    }

    public void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                display.setImageBitmap(bitmap);
                updateimage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateimage() {
        class updateImageAndText extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                p = new ProgressDialog(profilepic.this);
                p.setMessage("Please wait...");
                p.setIndeterminate(false);
                p.setCancelable(false);
                p.show();
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                p.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_ID, String.valueOf(a));
                if (filePath != null) {
                    hashMap.put(UPLOAD_KEY, getStringImage(filePath));
                }
                requesthandler rh = new requesthandler();
                if (check == 2) {
                    String m = rh.sendPostRequest(EndPoints.BASE_URL + "imageupdatestudent.php", hashMap);
                    return m;
                } else {
                    String m = rh.sendPostRequest(EndPoints.BASE_URL + "imageupdatealumni.php", hashMap);
                    return m;
                }
            }
        }

        updateImageAndText ue = new updateImageAndText();
        ue.execute();
    }

    public String getStringImage(Uri imgUri) {

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        } catch (Exception e) {
        }

        return "";
    }
}




