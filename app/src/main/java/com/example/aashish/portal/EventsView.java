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

public class EventsView extends Fragment{
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_TITLE = "title";
    private static final String KEY_VENUE = "venue";
    private ArrayList<HashMap<String, String>> movieList;
    private ListView list;
    private ProgressDialog pDialog;
    private TextView msg;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        View Eventview = inflater.inflate(R.layout.activity_events_view,container,false);
        msg=Eventview.findViewById(R.id.msg);
        list = (ListView) Eventview.findViewById(R.id.movieList);
        new FetchdataAsyncTask().execute();
        return Eventview;
    }
    private class FetchdataAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "RetrieveConfirmedEvents.php", "GET", null);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray objects;
                if (success == 1) {
                    movieList = new ArrayList<>();
                    objects = jsonObject.getJSONArray(KEY_DATA);
                    for (int i = 0; i < objects.length(); i++) {
                        JSONObject object = objects.getJSONObject(i);
                        String title = object.getString(KEY_TITLE);
                        String venue = object.getString(KEY_VENUE);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_TITLE, title);
                        map.put(KEY_VENUE, venue);
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
                R.layout.datalist, new String[]{KEY_TITLE, KEY_VENUE},
                new int[]{R.id.pin, R.id.Name});
           list.setAdapter(adapter);
           if(movieList.size()==0){
            msg.setVisibility(View.VISIBLE);
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (CheckNetworkStatus.isNetworkAvailable(getActivity().getApplicationContext())) {
                    String title = ((TextView) view.findViewById(R.id.pin)).getText().toString();
                    Intent intent = new Intent(getActivity().getApplicationContext(), ConfirmedEventDisplay.class);
                    intent.putExtra(KEY_TITLE, title);
                    startActivityForResult(intent, 20);
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }


            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 20) {
            Intent intent = getActivity().getIntent();
     //       getActivity().finish();
            startActivity(intent);
        }
    }
}
