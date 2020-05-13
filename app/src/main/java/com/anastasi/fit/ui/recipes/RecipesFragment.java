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

public class RecipesFragment extends Fragment implements View.OnClickListener {

    private RecipesViewModel recipesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recipes, container, false);

        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        LinearLayout linearLayout = root.findViewById(R.id.recipesLinearLayout);

        try {
            SQLiteDatabase db = getActivity().openOrCreateDatabase("FIT", Context.MODE_PRIVATE, null);
            Cursor c = db.rawQuery("SELECT * FROM recipes",null);
            int titleIndex = c.getColumnIndex("title");
            int cookingMethodIndex = c.getColumnIndex("cooking_method");
            int imgIndex = c.getColumnIndex("image");

            c.moveToFirst();
            while(c!=null){
                //get image
                byte[] imageByte = c.getBlob(imgIndex);
                Bitmap bmp = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);
                bmp=Bitmap.createScaledBitmap(bmp, 100 ,100, true);

                //create card
                View card = vi.inflate(R.layout.card, null);
                //set image
                ImageView imageView = card.findViewById(R.id.card_image);
                imageView.setImageBitmap(bmp);
                //set title
                TextView title = card.findViewById(R.id.card_title);
                title.setText(c.getString(titleIndex));
                //set description
                TextView desc = card.findViewById(R.id.card_description);
                desc.setText(c.getString(cookingMethodIndex));
                Button action = card.findViewById(R.id.card_action);
                action.setText("Eat Meal");

                card.setOnClickListener(this);

                linearLayout.addView(card, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));




                c.moveToNext();
            }
        }catch (SQLiteException e){

            TextView titleTextView = new TextView(getActivity());
            titleTextView.setText("Sorry there are no recipes to show");
            linearLayout.addView(titleTextView);
        }
        catch (Exception e){
            Log.e("EXCEPTION: ", e.toString());
        }

        return root;
    }

    @Override
    public void onClick(View v) {
        Fragment details = new RecipeDetailsFragment();
        IMainActivity iMainActivity = (IMainActivity) getActivity();

        iMainActivity.inflateFragment(details, getString(R.string.fragment_recipe_details_tag), true, null);

    }

}
