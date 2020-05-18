package com.anastasi.fit.ui.workouts;



import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.anastasi.fit.IMainActivity;
import com.anastasi.fit.NetworkHelper;
import com.anastasi.fit.R;
import com.anastasi.fit.RecipeDetailsFragment;
import com.anastasi.fit.WorkoutDetailsFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WorkoutsFragment extends Fragment {

    private WorkoutsViewModel workoutsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        workoutsViewModel = ViewModelProviders.of(this).get(WorkoutsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_workouts, container, false);

        root = createCards(root);

        return root;
    }

    private View createCards(View iroot){

        View root = iroot;
        LinearLayout linearLayout = (LinearLayout) root.findViewById(R.id.workoutsLinearLayout);
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            String jsonDataString = readJSONDataFromFile();
            JSONArray jsonArray = new JSONArray(jsonDataString);

            for(int i = 0; i<jsonArray.length();i++){
                View card = vi.inflate(R.layout.card, null);
                JSONObject itemObj = jsonArray.getJSONObject(i);
                String youtubeID = itemObj.getString("video");
                String title = itemObj.getString("title");
                String url = "https://img.youtube.com/vi/"+youtubeID+"/default.jpg";

                ImageView imageView = card.findViewById(R.id.card_image);

                 new NetworkHelper(imageView).execute(url);
//                imageView.setImageDrawable(d);


                TextView cardTitle = card.findViewById(R.id.card_title);
                cardTitle.setText(title);

                setOnClick(card, itemObj);
                linearLayout.addView(card);
            }


        }catch (Exception e){
            Log.e("Exception", e.toString());
        }


        return root;
    }



    private String readJSONDataFromFile() throws IOException {
        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try{
            String jsonString = null;
            inputStream = getResources().openRawResource(R.raw.workouts);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            while ((jsonString = bufferedReader.readLine()) != null){
                builder.append(jsonString);
            }

        }finally {
            if(inputStream != null){
                inputStream.close();
            }
        }
        return new String(builder);
    }

   private void setOnClick(View card, final JSONObject itemObj){
       card.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               try{
                   WorkoutDetailsFragment details = new WorkoutDetailsFragment();
                   details.setTitle(itemObj.getString("title"));
                   details.setVideo(itemObj.getString("video"));
                   details.setReps(itemObj.getString("reps"));
                   details.setAmounts(itemObj.getJSONArray("amounts"));
                   details.setSets(itemObj.getJSONArray("sets"));
                   IMainActivity iMainActivity = (IMainActivity) getActivity();
                   iMainActivity.inflateFragment(details, getString(R.string.fragment_recipe_details_tag), true, null);
               }catch (Exception e){

               }

           }
       });
    }


}


