package com.example.aashish.portal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import com.example.aashish.portal.helper.*;

public class PendingEvents extends Fragment {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_TITLE = "title";
    private static final String KEY_VENUE = "venue";
    private ArrayList<HashMap<String, String>> movieList;
    private ListView list;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View FinalEventView = inflater.inflate(R.layout.pendingevents, container, false);
        msg=FinalEventView.findViewById(R.id.msg);
        list = (ListView) FinalEventView.findViewById(R.id.movieList);
        new FetchMoviesAsyncTask().execute();
        return FinalEventView;
    }
    private class FetchMoviesAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "Retrieveevent.php", "GET", null);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray datas;
                if (success == 1) {
                    movieList = new ArrayList<>();
                    datas = jsonObject.getJSONArray(KEY_DATA);
                    for (int i = 0; i < datas.length(); i++) {
                        JSONObject obj = datas.getJSONObject(i);
                        String title = obj.getString(KEY_TITLE);
                        String venue = obj.getString(KEY_VENUE);
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
        if (movieList.size() == 0) {
            msg.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 20) {
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
        }
    }
}
