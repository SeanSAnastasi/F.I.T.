package com.anastasi.fit.ui.recipes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.anastasi.fit.IMainActivity;
import com.anastasi.fit.MainActivity;
import com.anastasi.fit.R;
import com.anastasi.fit.RecipeDetailsFragment;

public class RecipesFragment extends Fragment  {


    Bitmap bmp;
    String cookingMethod;
    String titleText;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //This view shows all of the recipes inside of the database by means of a material card

        View root = inflater.inflate(R.layout.fragment_recipes, container, false);

        //inflate card view
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        LinearLayout linearLayout = root.findViewById(R.id.recipesLinearLayout);

        try {
            //Gets data from database
            SQLiteDatabase db = getActivity().openOrCreateDatabase("FIT", Context.MODE_PRIVATE, null);
            Cursor c = db.rawQuery("SELECT * FROM recipes",null);
            int titleIndex = c.getColumnIndex("title");
            int cookingMethodIndex = c.getColumnIndex("cooking_method");
            int imgIndex = c.getColumnIndex("image");
            int idIndex = c.getColumnIndex("id");
            c.moveToFirst();
            while(c!=null){
                //get image bytes
                byte[] imageByte = c.getBlob(imgIndex);
                //converts to bitmap
                bmp = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);


                //create card
                View card = inflater.inflate(R.layout.card, null);
                //set image
                ImageView imageView = card.findViewById(R.id.card_image);
                imageView.setImageBitmap(bmp);

                //set title
                titleText = c.getString(titleIndex);
                TextView title = card.findViewById(R.id.card_title);

                title.setText(titleText);
                //set description
                TextView desc = card.findViewById(R.id.card_description);
                cookingMethod = c.getString(cookingMethodIndex);
                desc.setText(cookingMethod);


                setOnClick(card, c.getInt(idIndex));

                linearLayout.addView(card, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));




                c.moveToNext();
            }
        }catch (SQLiteException e){
            //shows only if database is empty
            TextView titleTextView = new TextView(getActivity());
            titleTextView.setText("Sorry there are no recipes to show");
            linearLayout.addView(titleTextView);
        }
        catch (Exception e){
            Log.e("Exception", e.toString());
        }

        return root;
    }

    //This on click method is added to every card.
    // A custom on click method was required so that data can be passed to a new fragment therefore passing the necessary db data and reducing database calls
    public void setOnClick(View card, final int id){
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = getActivity().openOrCreateDatabase("FIT", Context.MODE_PRIVATE, null);
                Cursor c = db.rawQuery("SELECT * FROM recipes WHERE id="+id,null);
                int titleIndex = c.getColumnIndex("title");
                int cookingMethod = c.getColumnIndex("cooking_method");
                int imgIndex = c.getColumnIndex("image");
                c.moveToFirst();

                //get image bytes
                byte[] imageByte = c.getBlob(imgIndex);
                //converts to bitmap
                Bitmap this_bmp = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);

                String this_titleText = c.getString(titleIndex);
                String this_cookingMethod = c.getString(cookingMethod);
                //pass data to fragment
                RecipeDetailsFragment details = new RecipeDetailsFragment();
                details.setTitle(this_titleText);
                details.setImg(this_bmp);
                details.setCookingMethod(this_cookingMethod);
                details.setId(id);
                //inflate fragment from main activity interface
                IMainActivity iMainActivity = (IMainActivity) getActivity();
                iMainActivity.inflateFragment(details, getString(R.string.fragment_recipe_details_tag), true, null);
            }
        });
    }



}
