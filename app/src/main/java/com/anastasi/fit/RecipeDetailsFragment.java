package com.anastasi.fit;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_details,container,false);

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

    }
}
