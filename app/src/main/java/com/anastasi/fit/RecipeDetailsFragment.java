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

public class RecipeDetailsFragment extends Fragment {
    String title = "";
    String cookingMethod = "";
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
        View view = inflater.inflate(R.layout.fragment_recipe_details,container,false);
        valuesContainer = (LinearLayout) view.findViewById(R.id.values_container);
        //set text
        if(title != ""){
            TextView textView = (TextView) view.findViewById(R.id.recipe_details_title);
            textView.setText(title);
        }
        if(img != null){
            ImageView imageView = (ImageView) view.findViewById(R.id.recipe_details_image);
            imageView.setImageBitmap(img);
        }
        if(cookingMethod != ""){
            TextView textView = (TextView) view.findViewById(R.id.cooking_method_text_view);
            textView.setText(cookingMethod);
        }

        populateValues();

        return view;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setCookingMethod(String cookingMethod){
        this.cookingMethod = cookingMethod;
    }

    public void setImg(Bitmap img){
        this.img = img;
    }
    public void setId(int id){
        this.id = id;
    }

    private void populateValues(){
        Log.i("ID", ""+id);
        db = getActivity().openOrCreateDatabase("FIT", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM recipe_values WHERE recipe_id="+id,null);
        c.moveToFirst();

        int valueIndex = c.getColumnIndex("ingredient_name");
        int amountIndex = c.getColumnIndex("ingredient_amount");
        int ingredientIdIndex = c.getColumnIndex("recipe_id");
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
