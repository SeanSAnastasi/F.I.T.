package com.example.fit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MyRecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //Action bar had to be hidden in this way since it is being used for the bottom navigation bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //if user previously logged in this view will not show
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.fit", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");

    }
    //Todo create my recipes
}
