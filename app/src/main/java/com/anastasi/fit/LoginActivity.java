package com.anastasi.fit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    TextView loginError ;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        //This is where a user can log in once an account has been created.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        loginError = (TextView) findViewById(R.id.login_error);
        loginError.setVisibility(View.INVISIBLE);
        db = openOrCreateDatabase("FIT", Context.MODE_PRIVATE, null);


    }

    public void loginButtonClicked(View view){
        //once the login button is pressed, the database is checked to see if a user exists with that username/password combination.
        // If an account exists the username and password are added to shared preferences signifying that a user is logged in
        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.fit", Context.MODE_PRIVATE);

        try{
            Cursor c = db.rawQuery("SELECT * FROM users WHERE username='"+username+"' AND password='"+password+"'",null);
            if(c.getCount() > 0){
                sharedPreferences.edit().putString("username",username).apply();
                sharedPreferences.edit().putString("password",password).apply();


                String user = sharedPreferences.getString("username","");



                goToPage(MainActivity.class);
            }else{
                loginError.setText("Incorrect username or password");
                loginError.setVisibility(View.VISIBLE);
            }
        }catch (SQLException e){
            loginError.setText("You must sign up first");
            loginError.setVisibility(View.VISIBLE);
        }
        catch (Exception e){
            Log.e("Login Error: " , e.toString());
            loginError.setText("System error!");
            loginError.setVisibility(View.VISIBLE);
        }




    }

    //Intent actions
    public void signupButton(View view){
        goToPage(SignUpActivity.class);
    }

    public void goToPage(Class whereToGo){
        //intent handler
        Intent intent = new Intent(this, whereToGo);
        startActivity(intent);
    }
}
