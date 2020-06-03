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
        // This is the initial page that users will be greeted with. If the user previously logged in this page will not be displayed
        super.onCreate(savedInstanceState);

        // Login details are stored as shared preferences so that the user does not need to log in every time s/he opens the application
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.fit", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","");
        String password = sharedPreferences.getString("password","");

        //if user previously logged in this view will not show
        if(username != "" && password!=""){
            goToPage(MainActivity.class);
        }else{
            setContentView(R.layout.activity_initial);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    //Intent actions
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
