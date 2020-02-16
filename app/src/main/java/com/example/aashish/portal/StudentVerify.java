package com.example.aashish.portal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import com.example.aashish.portal.helper.*;

public class StudentVerify extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_PIN = "pin";
    private static final String KEY_NAME = "name";
    private ArrayList<HashMap<String, String>> movieList;
    private ListView list;
    private ProgressDialog pDialog;
    private TextView msg;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_verify);
        msg=findViewById(R.id.msg);
        list = (ListView) findViewById(R.id.movieList);
        new FetchMoviesAsyncTask().execute();
    }
    private class FetchMoviesAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //Display progress bar
            pDialog = new ProgressDialog(StudentVerify.this);
            pDialog.setMessage("Loading Data. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "Retrieve.php", "GET", null);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray movies;
                if (success == 1) {
                    movieList = new ArrayList<>();
                    movies = jsonObject.getJSONArray(KEY_DATA);
                    //Iterate through the response and populate movies list
                    for (int i = 0; i < movies.length(); i++) {
                        JSONObject movie = movies.getJSONObject(i);
                        String pin = movie.getString(KEY_PIN);
                        String name = movie.getString(KEY_NAME);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_PIN, pin);
                        map.put(KEY_NAME, name);
                        movieList.add(map);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run(){

                    populateMovieList();
                }
            });
        }

    }
    private void populateMovieList() {
        final ListAdapter adapter = new SimpleAdapter(
                StudentVerify.this, movieList,
                R.layout.datalist, new String[]{KEY_PIN, KEY_NAME},
                new int[]{R.id.pin, R.id.Name});
        list.setAdapter(adapter);
        if(movieList.size()==0){
            msg.setVisibility(View.VISIBLE);
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    String pin = ((TextView) view.findViewById(R.id.pin)).getText().toString();
                    Intent intent = new Intent(getApplicationContext(), DetailsDisplay.class);
                    intent.putExtra(KEY_PIN, pin);
                    startActivityForResult(intent, 20);
                    finish();
                }
                else {
                    Toast.makeText(StudentVerify.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }


            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 20) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}
