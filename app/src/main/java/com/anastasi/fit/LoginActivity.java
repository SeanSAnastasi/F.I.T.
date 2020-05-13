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

public class LoginActivity extends AppCompatActivity {
    TextView loginError ;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        loginError = (TextView) findViewById(R.id.login_error);
        loginError.setVisibility(View.INVISIBLE);
        db = openOrCreateDatabase("FIT", Context.MODE_PRIVATE, null);


    }

    public void loginButtonClicked(View view){
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
        }
        catch (Exception e){
            loginError.setText("System error!");
            loginError.setVisibility(View.VISIBLE);
        }




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