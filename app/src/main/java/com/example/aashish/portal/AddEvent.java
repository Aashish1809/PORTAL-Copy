package com.example.aashish.portal;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import com.example.aashish.portal.helper.*;
public class AddEvent extends Fragment{
    private static String a,b,c,d,e;
    private int success;
    private TextInputLayout title, description, date, time, venue;
    private Button button1;
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_VENUE = "venue";
    private static final String KEY_TOKEN="token";

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
        FragmentManager fm=getActivity().getSupportFragmentManager();
        for(int i=0;i<fm.getBackStackEntryCount();i++){
            fm.popBackStack();
        }
        View AddeventView = inflater.inflate(R.layout.activity_add_event, container, false);
        title = (TextInputLayout) AddeventView.findViewById(R.id.title);
        description = (TextInputLayout) AddeventView.findViewById(R.id.description);
        date = (TextInputLayout) AddeventView.findViewById(R.id.date);
        time = (TextInputLayout) AddeventView.findViewById(R.id.time);
        venue = (TextInputLayout) AddeventView.findViewById(R.id.venue);
        button1 = AddeventView.findViewById(R.id.nextb);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a = title.getEditText().getText().toString();
                b = description.getEditText().getText().toString();
                c = date.getEditText().getText().toString();
                d = time.getEditText().getText().toString();
                e = venue.getEditText().getText().toString();
                if (a.length() == 0 || b.length() == 0 || c.length() == 0 || d.length() == 0 || e.length() == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "Fill Out the Form Completely...!", Toast.LENGTH_LONG).show();
                } else {
                    if (CheckNetworkStatus.isNetworkAvailable(getActivity().getApplicationContext())) {
                        addinto();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "UNABLE TO CONNECT...!",
                                Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
        return AddeventView;
    }

    public void sendTokenToServer() {
        final String token = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getDeviceToken();
        if (token == null) {
            Toast.makeText(getActivity().getApplicationContext(), "Token not generated", Toast.LENGTH_LONG).show();
        }
        new Update().execute();
    }

    class Update extends AsyncTask<String, String, String> {
        final String token = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getDeviceToken();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_TITLE, a);
            httpParams.put(KEY_TOKEN, token);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL+"tokenevent.php", "POST", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
           getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                    } else {
                    }
                }
            });
        }
    }

    public void addinto()
    {
        new adding().execute();

    }
    private class adding extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_TITLE, a);
            httpParams.put(KEY_DESCRIPTION, b);
            httpParams.put(KEY_DATE, c);
            httpParams.put(KEY_TIME, d);
            httpParams.put(KEY_VENUE, e);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    EndPoints.BASE_URL + "addingevent.php", "POST", httpParams);
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
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        sendTokenToServer();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Details Uploaded", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Failed to Upload Details..!",
                                Toast.LENGTH_LONG).show();


                    }
                }
            });
        }
    }
}
