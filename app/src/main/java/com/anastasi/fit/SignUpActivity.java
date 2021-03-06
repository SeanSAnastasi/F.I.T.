package com.anastasi.fit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    SQLiteDatabase db;
    TextView signupError ;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        //This page allows a user to create an account
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        signupError = (TextView) findViewById(R.id.signup_error);
        signupError.setVisibility(View.INVISIBLE);
        db = openOrCreateDatabase("FIT", Context.MODE_PRIVATE, null);



    }
    // Performs the required checks to see if valid data is inputted. If correct the data is added to the database, shared preferences are set(signifying that a user is logged in) and an intent to the MainActivity is created
    public void signUpButtonClicked(View view){
        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        EditText confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        if(validatePassword(password,username)) {

            if (password.equals(confirmPassword)) {
                //Database functions
                db.execSQL("CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY, username VARCHAR, password VARCHAR)");
                Cursor c = db.rawQuery("SELECT * FROM users WHERE username='" + username + "'", null);
                if (c.getCount() == 0) {

                    db.execSQL("INSERT INTO users (username,password) VALUES('" + username + "','" + password + "')");
                    SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.fit", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("username", username).apply();
                    sharedPreferences.edit().putString("password", password).apply();


                    goToPage(MainActivity.class);

                } else {
                    signupError.setText("Username is taken by another person");
                    signupError.setVisibility(View.VISIBLE);
                }


            } else {
                signupError.setText("Passwords do not match");
                signupError.setVisibility(View.VISIBLE);
            }
        }


    }

    public void loginButton(View view){
        goToPage(LoginActivity.class);
    }

    public void goToPage(Class whereToGo){
        //intent handler
        Intent intent = new Intent(this, whereToGo);
        startActivity(intent);
    }

    //Password string validation function
    public boolean validatePassword(String password, String username){
        if(password.length() < 8){
            signupError.setText("Password must be at least 8 characters long");
            signupError.setVisibility(View.VISIBLE);
            return false;
        }
        if(password.toLowerCase().equals(username.toLowerCase())){
            signupError.setText("Password should not be the same as the username");
            signupError.setVisibility(View.VISIBLE);
            return false;
        }
        // makes use of regular expressions
        String uppercaseRegex = "(.*[A-Z].*)";
        if(!password.matches(uppercaseRegex)){
            signupError.setText("Password should have at least 1 uppercase character");
            signupError.setVisibility(View.VISIBLE);
            return false;
        }
        String lowercaseRegex = "(.*[a-z].*)";
        if(!password.matches(lowercaseRegex)){
            signupError.setText("Password should have at least 1 lowercase character");
            signupError.setVisibility(View.VISIBLE);
            return false;
        }

        return true;
    }
}
