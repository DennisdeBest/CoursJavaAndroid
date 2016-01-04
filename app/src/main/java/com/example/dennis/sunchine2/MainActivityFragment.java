package com.example.dennis.sunchine2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        //inflate our fragment view in the container
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //Create mockup values for the forecast array
        String[] forecastArray = {
                "Today - Raining - 75°C",
                "Tommorow - Overcast - 15°C",
                "Wednesday - Sunny - 152°C",
                "Thursday - Nuclear holocaust - 1052°C",
                "Friday - Sunny - 152°C",
                "Saturday - Sunny - 152°C",
                "Sunday - Sunny - 152°C",
        };
        //Put the string array into a list
        List<String> weekForecast = new ArrayList<String>(Arrays.asList(forecastArray));
        //Create a new adapter to display the information
        mForecastAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_forcast,
                R.id.list_item_forcast_textview,
                weekForecast);
        //Assign listview_forecast to listview and set the adapter to this view
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forcast);
        listView.setAdapter(mForecastAdapter);

        return rootView;
    }

    public class FetchWeatherTask extends AsyncTask<Void, Void, Void>{
        //define a tag for error logging
        final String LOG_TAG=this.getClass().getSimpleName();
        @Override
        protected Void doInBackground(Void... params) {

            //Connect to the API and retreive weather information
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJson = null;

            try {
                String baseURL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Bordeaux&units=metric&cnt=16";
                //String apiKey = "&appid=97af090884e0e22ec08a33ebd1440f7b";
                String apiKey = "&appid=" + BuildConfig.myAPI;
                URL url = new URL(baseURL.concat(apiKey));

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //read input into string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null){
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }
                if(buffer.length() == 0){
                    return null;
                }
                forecastJson = buffer.toString();

                Log.e("received data", forecastJson);
            }catch(IOException e){
                Log.e(LOG_TAG, "Error", e);

            }finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
                if (reader != null)
                {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

    }
}
