package com.example.aashish.portal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Picture extends AppCompatActivity implements View.OnClickListener {

    public static final String UPLOAD_KEY = "image";
    public static final String KEY_ID = "pin";
    public String a;
    public Bundle d;
    private int PICK_IMAGE_REQUEST = 1;
    public Button buttonChoose;
    public Button buttonUpload,bf;
    public ImageView imageView;
    public Bitmap bitmap;
    public Uri filePath;
    public ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        a= getIntent().getExtras().getString("aash");
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        imageView = (ImageView) findViewById(R.id.imageView);
        bf = findViewById(R.id.nextb);
        bf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageView.getDrawable()==null)
                {
                    uploadImage();
                }
                Intent i=new Intent(getApplicationContext(),Waiting.class);
                i.putExtra("pass",a);
                startActivity(i);
                finish();
            }
        });
        buttonChoose.setOnClickListener((View.OnClickListener) this);
        buttonUpload.setOnClickListener((View.OnClickListener) this);
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
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
        public void uploadImage() {

                class updateImageAndText extends AsyncTask<Void, Void, String> {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        dialog = new ProgressDialog(Picture.this);
                        dialog.setMessage("Uploading Details. Please wait...");
                        dialog.setIndeterminate(false);
                        dialog.setCancelable(false);
                        dialog.show();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(KEY_ID, String.valueOf(a));
                        if (filePath != null) {
                            hashMap.put(UPLOAD_KEY, getStringImage(filePath));
                        }
                        requesthandler rh = new requesthandler();
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
                        int check;
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
                        if (check==2) {
                            String m = rh.sendPostRequest(EndPoints.BASE_URL+"aa.php", hashMap);
                            return m;
                        }
                        else
                        {
                            String m = rh.sendPostRequest(EndPoints.BASE_URL+"aa2.php", hashMap);
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

    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }

        if(v == buttonUpload){
            uploadImage();
        }

    }
}
