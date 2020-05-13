package com.anastasi.fit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class InitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //Action bar had to be hidden in this way since it is being used for the bottom navigation bar
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        //if user previously logged in this view will not show
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.fit", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","");
        String password = sharedPreferences.getString("password","");

        if(username != "" && password!=""){
            goToPage(MainActivity.class);
        }else{
            setContentView(R.layout.activity_initial);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public void startButton(View view){
        //User wants to use the app without logging in
        goToPage(MainActivity.class);
    }
    public void loginButton(View view){
        goToPage(LoginActivity.class);
    }

    public void goToPage(Class whereToGo){
        //intent handler
        Intent intent = new Intent(this, whereToGo);
        startActivity(intent);
    }
}
