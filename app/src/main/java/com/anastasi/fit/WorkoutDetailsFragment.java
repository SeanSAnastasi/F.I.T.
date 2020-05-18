package com.anastasi.fit;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class WorkoutDetailsFragment extends Fragment {
    String video = "";
    String title = "";
    JSONArray sets = null;
    JSONArray amounts = null;
    String reps = null;
    TextView descriptionTextView;
    //Youtube vars
    YouTubePlayerView youTubePlayerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_details,container,false);

        //set youtube video
        view = youtubeCreate(view);

        //set description
        descriptionTextView = view.findViewById(R.id.workout_description_text_view);
        String url = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id="+video+"&key=AIzaSyDay3AY7Qv5u5-ScIry3V-4LacN_bL32FE";
        DownloadTask task = new DownloadTask();
        task.execute(url);

        //set title
        TextView textView = (TextView) view.findViewById(R.id.workout_details_title);
        textView.setText(title);

        //set rep details
        LinearLayout valuesContainer = (LinearLayout) view.findViewById(R.id.sets_container);
        for(int i = 0; i< sets.length(); i++){
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.HORIZONTAL);
            try {
            //create text views
            TextView valueText = new TextView(getActivity());

            valueText.setText(sets.getString(i)+":");
            valueText.setGravity(Gravity.LEFT);
            valueText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,4f));

            TextView amountText = new TextView(getActivity());
            amountText.setText(amounts.getString(i));
            amountText.setGravity(Gravity.RIGHT);
            amountText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1f));

            //add to linear layout
            layout.addView(valueText);
            layout.addView(amountText);

            //add to container
            valuesContainer.addView(layout);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setVideo(String video){this.video = video;}
    public void setReps(String reps){this.reps = reps;}
    public void setSets(JSONArray sets){this.sets = sets;}
    public void setAmounts(JSONArray amounts){this.amounts = amounts;}

    private View youtubeCreate(View iview){
        View view = iview;

        youTubePlayerView = (YouTubePlayerView) view.findViewById(R.id.youtube_player);

        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = video;
                youTubePlayer.cueVideo(videoId, 0);
            }
        });

        return view;
    }

    private View setDescription(View iview){
        View view = iview;

        //get JSON data from Youtube Data API



        return view;
    }
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(inputStream);

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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray= jsonObject.getJSONArray("items");
                String description = jsonArray.getJSONObject(0).getJSONObject("snippet").getString("description");
                descriptionTextView.setText(description);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
