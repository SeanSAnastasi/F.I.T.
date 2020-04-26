package com.example.fit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;



import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CreateRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //Action bar had to be hidden in this way since it is being used for the bottom navigation bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //if user previously logged in this view will not show
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.fit", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        addViewItem();
    }

    public void addValue(View view){
        //Initialize Values
        addViewItem();
    }

    public void goToPage(Class whereToGo){
        //intent handler
        Intent intent = new Intent(this, whereToGo);
        startActivity(intent);
    }

    public void addViewItem(){
        LinearLayout viewLinearLayout = (LinearLayout) findViewById(R.id.valuesLinearLayout);
        LinearLayout newLinearLayout = new LinearLayout(this);
        EditText value = new EditText(this);
        EditText amount = new EditText(this);
        //set value data
        newLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        value.setHint("Ingredient");
        amount.setHint("Amount");
        value.setEms(10);

        amount.setEms(10);
        //Add to horizontal view
        newLinearLayout.addView(value);
        newLinearLayout.addView(amount);
        //Add to vertical view
        viewLinearLayout.addView(newLinearLayout);
    }
    public void goHome(View view){
        goToPage(MainActivity.class);
    }

    public void submitData(View view){
//        Todo handle create recipe submit data
    }
}
