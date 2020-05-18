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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CreateIngredientActivity extends AppCompatActivity {
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //these were being used to delete data with every activity load to keep the ingredients fragment from cluttering up
//        try {
//            SQLiteDatabase db = this.openOrCreateDatabase("FIT", Context.MODE_PRIVATE, null);
//
//            db.execSQL("DROP TABLE ingredients");
//        }catch (Exception e){
//
//        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ingredient);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //Action bar had to be hidden in this way since it is being used for the bottom navigation bar
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        Toolbar topAppBar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(MainActivity.class);
            }
        });




        //if user previously logged in this view will not show
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.fit", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        addViewItem();
    }

    public void submitData(View view){
        //Initialize Values

        EditText ingredientTitle = (EditText) findViewById(R.id.ingredientTitle);
        String title = ingredientTitle.getText().toString();
        LinearLayout valuesLinearLayout = (LinearLayout) findViewById(R.id.valuesLinearLayout);
        EditText descriptionTextArea = (EditText) findViewById(R.id.descriptionTextArea);
        String description = descriptionTextArea.getText().toString();
        ImageView imageView = (ImageView) findViewById(R.id.ingredientChooseImageView);

        //store image

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        try {

            SQLiteDatabase db = this.openOrCreateDatabase("FIT", Context.MODE_PRIVATE, null);
        // creates table for nutritional values of ingredient if the table does not exist
            db.execSQL("CREATE TABLE IF NOT EXISTS ingredient_values (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ingredient_id INT, nutritional_value_title VARCHAR, nutritional_value_amount VARCHAR)");
        // creates table for ingredient if the table does not exist
            db.execSQL("CREATE TABLE IF NOT EXISTS ingredients (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title VARCHAR, description VARCHAR, image BLOB)");
        //insert values into database
            ContentValues contentValues = new ContentValues();
            contentValues.put("title",title);
            contentValues.put("description",description);
            contentValues.put("image",imageInByte);
            //db.execSQL("INSERT INTO ingredients (title,description,image) VALUES('"+title+"','"+description+"','"+imageInByte+"')");
            long confirmQuery = db.insert("ingredients",null, contentValues);

            if(confirmQuery != 0){



            //get id from last row to be used in ingredient_values
            Cursor c = db.rawQuery("SELECT * FROM ingredients",null);
            c.moveToLast();
            int idIndex = c.getColumnIndex("id");
            int ingredientId = c.getInt(idIndex);

            //insert values into ingredient_values
            for(int i = 0; i<valuesLinearLayout.getChildCount();i++){
                LinearLayout layout = (LinearLayout) valuesLinearLayout.getChildAt(i);
                String nutritionalValueTitle = ((EditText)layout.getChildAt(0)).getText().toString();
                String nutritionalValueAmount = ((EditText)layout.getChildAt(1)).getText().toString();
                db.execSQL("INSERT INTO ingredient_values (ingredient_id, nutritional_value_title, nutritional_value_amount) VALUES("+ingredientId+",'"+nutritionalValueTitle+"','"+nutritionalValueAmount+"')");
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

    public void goToPage(Class whereToGo){
        //intent handler
        Intent intent = new Intent(this, whereToGo);
        startActivity(intent);
    }

    public void addValueButton(View view){
        addViewItem();
    }

    public void addViewItem(){
        LinearLayout viewLinearLayout = (LinearLayout) findViewById(R.id.valuesLinearLayout);
        LinearLayout newLinearLayout = new LinearLayout(this);
        EditText value = new EditText(this);
        EditText amount = new EditText(this);
        //set value data
        newLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        value.setHint("e.g. Calories");
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
                ImageView imageView = (ImageView) findViewById(R.id.ingredientChooseImageView);
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
