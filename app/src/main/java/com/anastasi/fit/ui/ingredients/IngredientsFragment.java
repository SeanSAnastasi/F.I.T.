package com.anastasi.fit.ui.ingredients;

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
import com.anastasi.fit.IngredientDetailsFragment;
import com.anastasi.fit.R;

import org.w3c.dom.Text;

public class IngredientsFragment extends Fragment implements View.OnClickListener {

    private IngredientsViewModel ingredientsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ingredientsViewModel = ViewModelProviders.of(this).get(IngredientsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_ingredients, container, false);

        //inflate card view
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        LinearLayout linearLayout = root.findViewById(R.id.ingredientsLinearLayout);

        try {
            SQLiteDatabase db = getActivity().openOrCreateDatabase("FIT", Context.MODE_PRIVATE, null);
            Cursor c = db.rawQuery("SELECT * FROM ingredients",null);
            int titleIndex = c.getColumnIndex("title");
            int descIndex = c.getColumnIndex("description");
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
                desc.setText(c.getString(descIndex));
                Button action = card.findViewById(R.id.card_action);
                action.setText("Use in recipe");

                card.setOnClickListener(this);

                linearLayout.addView(card, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                ///////////////////////
//                TextView titleTextView = new TextView(getActivity());
//                TextView descriptionTextView = new TextView(getActivity());
//                ImageView imageView = new ImageView(getActivity());
//                titleTextView.setText(c.getString(titleIndex));
//                descriptionTextView.setText(c.getString(descIndex));
//
//
//
//
//                //create horizontal layout
//
//                LinearLayout horLayout = new LinearLayout(getActivity());
//                horLayout.setOrientation(LinearLayout.HORIZONTAL);
//                horLayout.addView(imageView);
//                horLayout.addView(titleTextView);
//
//                //add to vertical layout
//
//                linearLayout.addView(horLayout);
//                linearLayout.addView(descriptionTextView);


                c.moveToNext();
            }
        }catch (SQLiteException e){

            TextView titleTextView = new TextView(getActivity());
            titleTextView.setText("Sorry there are no ingredients to show");
            linearLayout.addView(titleTextView);
        }
        catch (Exception e){
            Log.e("EXCEPTION: ", e.toString());
        }




        return root;
    }
    @Override
    public void onClick(View v) {
        Fragment details = new IngredientDetailsFragment();
        IMainActivity iMainActivity = (IMainActivity) getActivity();

        iMainActivity.inflateFragment(details, getString(R.string.fragment_recipe_details_tag), true, null);

    }
}
