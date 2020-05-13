package com.anastasi.fit.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.anastasi.fit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;


    public class DownloadTask extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1){
                    char current = (char) data;
                    result += current;

                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView quoteOfTheDay =(TextView) getActivity().findViewById(R.id.quoteOfTheDay);
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.example.fit", Context.MODE_PRIVATE);
            //reached hourly quota of 10 requests per hour
            if(s == null){
                quoteOfTheDay.setText("With the new day comes new strength and new thoughts - Eleanor Roosevelt");
            }else{

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String quoteContents = jsonObject.getString("contents");
                    JSONObject quoteContentsObject = new JSONObject(quoteContents);
                    String quotes = quoteContentsObject.getString("quotes");

                    JSONArray jsonArray = new JSONArray(quotes);
                    JSONObject quoteObject = jsonArray.getJSONObject(0);
                    String quote = quoteObject.getString("quote");
                    String author = quoteObject.getString("author");
                    quoteOfTheDay.setText("\""+quote+"\" - "+author);
                    sharedPreferences.edit().putString("quote",quote).apply();
                    sharedPreferences.edit().putString("author",author).apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.example.fit", Context.MODE_PRIVATE);
        String quote = sharedPreferences.getString("quote","");
        String author = sharedPreferences.getString("author","");


        if(quote == "" || author ==""){
            DownloadTask task = new DownloadTask();
            task.execute("https://quotes.rest/qod.json");

        }
        else{
            TextView quoteOfTheDay =(TextView) root.findViewById(R.id.quoteOfTheDay);
            quoteOfTheDay.setText("\""+quote+"\" - "+author);

        }








        return root;
    }

}
