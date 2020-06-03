package com.anastasi.fit;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CreateRecipeActivity extends AppCompatActivity {
    Bitmap bitmap;
    String username = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //This activity is being used to allow a user to create an recipe item. A form is presented with the ability to input a title, image, cooking method and ingredient values.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Toolbar topAppBar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);


        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(MainActivity.class);
            }
        });

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.fit", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
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

    //This method is used to add ingredient items to the form allowing for the correct amount of ingredients to be inputted.
    // Both values were decided to be strings instead of a string and a number to allow for metric values to be added aswell e.g. Tomatoes 200g instead of Tomatoes 200.
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

    // Submit button on click. adds data to the database
    public void submitData(View view){
        EditText recipeTitle = (EditText) findViewById(R.id.recipeTitle);
        String title = recipeTitle.getText().toString();
        LinearLayout valuesLinearLayout = (LinearLayout) findViewById(R.id.valuesLinearLayout);
        EditText cookingMethodTextArea = (EditText) findViewById(R.id.cookingMethodTextArea);
        String description = cookingMethodTextArea.getText().toString();
        ImageView imageView = (ImageView) findViewById(R.id.recipeChooseImageView);

        //store image

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] imageInByte = stream.toByteArray();
        try {

            SQLiteDatabase db = this.openOrCreateDatabase("FIT", Context.MODE_PRIVATE, null);
            // creates table for nutritional values of ingredient if the table does not exist
            db.execSQL("CREATE TABLE IF NOT EXISTS recipe_values (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ingredient_name VARCHAR,ingredient_amount VARCHAR, recipe_id INT)");
            // creates table for ingredient if the table does not exist
            db.execSQL("CREATE TABLE IF NOT EXISTS recipes (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title VARCHAR, cooking_method VARCHAR, image BLOB, username VARCHAR)");
            //insert values into database
            ContentValues contentValues = new ContentValues();
            contentValues.put("title",title);
            contentValues.put("cooking_method",description);
            contentValues.put("image",imageInByte);
            contentValues.put("username", username);

            long confirmQuery = db.insert("recipes",null, contentValues);

            if(confirmQuery != 0){



                //get id from last row to be used in ingredient_values
                Cursor c = db.rawQuery("SELECT * FROM recipes",null);
                c.moveToLast();
                int idIndex = c.getColumnIndex("id");
                int recipeId = c.getInt(idIndex);

                //insert values into recipe_values
                for(int i = 0; i<valuesLinearLayout.getChildCount();i++){
                    LinearLayout layout = (LinearLayout) valuesLinearLayout.getChildAt(i);
                    //todo: create dropdown for ingredient values
                    String ingredientName = ((EditText)layout.getChildAt(0)).getText().toString();
                    String ingredientAmount = ((EditText)layout.getChildAt(1)).getText().toString();
                    db.execSQL("INSERT INTO recipe_values (ingredient_name, ingredient_amount, recipe_id) VALUES('"+ingredientName+"','"+ingredientAmount+"','"+recipeId+"')");
                }
                goToPage(MainActivity.class);
            }else{
                //debug info
                Log.e("Database input","Failed");
            }
        }catch (Exception e){
            Log.e("EXCEPTION: ", e.toString());
        }
    }
    public void goHome(View view){
        goToPage(MainActivity.class);
    }

    // From here onwards are all functions to allow for a user to input an image from their gallery

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void openImageGallery(View view){
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else{
            openImageGallery();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==1 && resultCode == RESULT_OK && data!=null){
            Uri image = data.getData();
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(),image);
                bitmap = bmp;
                ImageView imageView = (ImageView) findViewById(R.id.recipeChooseImageView);
                imageView.setImageBitmap(bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openImageGallery();
            }
        }
    }

    public void openImageGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }
}
