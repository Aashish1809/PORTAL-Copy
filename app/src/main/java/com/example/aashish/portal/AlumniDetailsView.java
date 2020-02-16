package com.example.aashish.portal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static com.example.aashish.portal.EndPoints.BASE_URL;

public class AlumniDetailsView extends Fragment{
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_PIN = "pin";
    private static final String KEY_NAME = "name";
    private ArrayList<HashMap<String, String>> movieList;
    private ListView list;
    private ProgressDialog pDialog;
    private TextView msg;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View StudentView    = inflater.inflate(R.layout.activity_alumni_details_view,container,false);
        msg=StudentView.findViewById(R.id.msg);
        list = (ListView) StudentView.findViewById(R.id.movieList);
        new FetchStudentDetailsAsyncTask().execute();
        return StudentView;
    }
    private class FetchStudentDetailsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "RetrieveConfirmedAlumniDetails2.php", "GET", null);
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
            getActivity().runOnUiThread(new Runnable() {
                public void run(){

                    populateMovieList();
                }
            });
        }

    }
    private void populateMovieList() {
        final ListAdapter adapter = new SimpleAdapter(
                getActivity().getApplicationContext(), movieList,
                R.layout.datalist, new String[]{KEY_PIN, KEY_NAME},
                new int[]{R.id.pin, R.id.Name});
        list.setAdapter(adapter);
        if(movieList.size()==0){
            msg.setVisibility(View.VISIBLE);
        }
        else {
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (CheckNetworkStatus.isNetworkAvailable(getActivity().getApplicationContext())) {
                        String pin = ((TextView) view.findViewById(R.id.pin)).getText().toString();
                        Intent intent = new Intent(getActivity().getApplicationContext(), ConfirmedAlumiDisplay.class);
                        intent.putExtra(KEY_PIN, pin);
                        startActivityForResult(intent, 20);
                        //getActivity().finish();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Unable to connect to internet",
                                Toast.LENGTH_LONG).show();

                    }


                }
            });
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 20) {
            Intent intent = getActivity().getIntent();
         //   getActivity().finish();
            startActivity(intent);
        }
    }
}
