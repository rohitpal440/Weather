package com.example.rohit.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private ArrayAdapter<String> mForecastAdapter;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mForecastAdapter =
                        new ArrayAdapter<String>(
                                getActivity(), // The current context (this activity)
                                R.layout.list_item_forecast, // The name of the layout ID.
                                R.id.list_item_forecast_textview, // The ID of the textview to populate.
                                new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

        return rootView;
    }

    private void updateWeather() {
        FetchWeatherAsyncTask weatherTask = new FetchWeatherAsyncTask(getActivity(), mForecastAdapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));
        weatherTask.execute(location);

//        Uncomment following lines to fetch weather data using intent service
//        Intent intent = new Intent(getActivity(), FetchWeatherService.class);
//        intent.putExtra("messenger", new Messenger(handler));
//        intent.putExtra("lqe","560076");
//        getActivity().startService(intent);
//        Log.d("MainActivityFragment","Updating weather");
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle reply = msg.getData();
            String [] result = reply.getStringArray("result");
            if (result != null) {
                mForecastAdapter.clear();
                for(String dayForecastStr : result) {
                    mForecastAdapter.add(dayForecastStr);
                }
            }
        }
    };


    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

}