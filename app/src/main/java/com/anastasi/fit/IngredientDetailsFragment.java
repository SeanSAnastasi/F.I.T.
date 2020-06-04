package com.anastasi.fit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
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

import java.util.ArrayList;

public class IngredientDetailsFragment extends Fragment {
    String title = "";
    String description = "";
    ArrayList values = null;
    Bitmap img = null;
    int id = -1;
    SQLiteDatabase db;
    LinearLayout valuesContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //This view will show the details of a single ingredient namely the title, image, nutritional values and description

        View view = inflater.inflate(R.layout.fragment_ingredient_details,container,false);

         valuesContainer = (LinearLayout) view.findViewById(R.id.values_container);

        //set text
        if(title != ""){
            TextView textView = (TextView) view.findViewById(R.id.ingredient_details_title);
            textView.setText(title);
        }
        if(img != null){
            ImageView imageView = (ImageView) view.findViewById(R.id.ingredient_details_image);
            imageView.setImageBitmap(img);
        }
        if(description != ""){
            TextView textView = (TextView) view.findViewById(R.id.description_text_view);
            textView.setText(description);
        }

        populateValues();

        return view;
    }

    //Getter and setter functions to be used by the IngredientsFragment to pass data to the details fragment
    public void setTitle(String title){
        this.title = title;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public void setImg(Bitmap img){
        this.img = img;
    }
    public void setId(int id){
        this.id = id;
    }

    // Uses the variable contents set by the setter functions, gets the nutritional value data (labelled with foreign key) and populates the view with the data gathered
    private void populateValues(){
        Log.i("SET ID: ", ""+id);
        db = getActivity().openOrCreateDatabase("FIT", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM ingredient_values WHERE ingredient_id="+id,null);
        c.moveToFirst();

        int valueIndex = c.getColumnIndex("nutritional_value_title");
        int amountIndex = c.getColumnIndex("nutritional_value_amount");
        int ingredientIdIndex = c.getColumnIndex("ingredient_id");
        while(c!=null){
            try{



                //create container for values
                LinearLayout layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.HORIZONTAL);

                //create text views
                TextView valueText = new TextView(getActivity());
                valueText.setText(c.getString(valueIndex)+":");
                valueText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,4f));

                TextView amountText = new TextView(getActivity());
                amountText.setText(c.getString(amountIndex));
                amountText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1f));

                //add to linear layout
                layout.addView(valueText);
                layout.addView(amountText);

                //add to container
                valuesContainer.addView(layout);

                c.moveToNext();

            }catch (Exception e){
                break;
            }

        }
    }
}

