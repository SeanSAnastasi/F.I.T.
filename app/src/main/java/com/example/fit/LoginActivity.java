package com.example.fit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //Action bar had to be hidden in this way since it is being used for the bottom navigation bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //if user previously logged in this view will not show


    }

    public void loginButtonClicked(View view){
        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.fit", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("username",username).apply();
        sharedPreferences.edit().putString("password",password).apply();


        String user = sharedPreferences.getString("username","");

        //Todo login logic for database

        goToPage(MainActivity.class);
    }
    public void signupButton(View view){
        goToPage(SignUpActivity.class);
    }

    public void goToPage(Class whereToGo){
        //intent handler
        Intent intent = new Intent(this, whereToGo);
        startActivity(intent);
    }
}
